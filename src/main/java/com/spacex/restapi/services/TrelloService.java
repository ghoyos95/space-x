package com.spacex.restapi.services;

import com.spacex.restapi.exceptions.TrelloApiException;
import com.spacex.restapi.models.TrelloCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class TrelloService {

    @Value("${trello.api.key}")
    private String trelloApiKey;

    @Value("${trello.api.token}")
    private String trelloApiToken;

    @Value("${trello.api.listId}")
    private String trelloListId;

    @Value("${trello.api.members}")
    private List<String> trelloMembers;
    
    @Value("${trello.api.bugLabelId}") 
    private String bugLabelId;
    
    @Value("${trello.api.uri}")
    private String apiUrl;
    private WebClient webClient;
    private final Map<String, String> trelloParameters;
    private final String BUG_TYPE = "bug";
    private final String ISSUE_TYPE = "issue";
    private final String TASK_TYPE = "task";
    @Autowired
    public TrelloService(
            @Value("${trello.api.maintenanceLabelId}") String maintenanceLabelId,
            @Value("${trello.api.researchLabelId}") String researchLabelId,
            @Value("${trello.api.testLabelId}") String testLabelId
    ) {
        this.webClient = WebClient.create();
        this.trelloParameters = buildLabelsMap(maintenanceLabelId, researchLabelId, testLabelId);
    }
    
    public Mono<String> createCard(TrelloCard trelloCard) throws TrelloApiException,IllegalArgumentException {
       
        validateCardType(trelloCard.getType());
        
        Object requestBody = buildRequestBody(trelloCard);
        
        return webClient.post()
                .uri(apiUrl)
                .headers(this::addCommonHeaders)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.error("Failed to create card: " + e.getMessage()))
                .onErrorMap(e -> new TrelloApiException("Failed to create card: " + e.getMessage()));
    }

    private void validateCardType(String type) {
        List<String> validTypes = Arrays.asList(ISSUE_TYPE, BUG_TYPE, TASK_TYPE);
        if (!validTypes.contains(type)) {
            throw new IllegalArgumentException("Invalid card type. Supported types: issue, bug, task");
        }
    }
    private Object buildRequestBody(TrelloCard trelloCard) {
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("idList", this.trelloListId);
        requestBodyMap.put("token", this.trelloApiToken);
        requestBodyMap.put("key", this.trelloApiKey);
        
        if(BUG_TYPE.equals(trelloCard.getType())){
            buildBugBody(trelloCard, requestBodyMap);
            return requestBodyMap;
        } else if (TASK_TYPE.equals(trelloCard.getType())) {
            requestBodyMap.put("idLabels", Collections.singletonList(trelloParameters.get(trelloCard.getCategory())));
        }
        requestBodyMap.put("name", trelloCard.getTitle());
        requestBodyMap.put("desc", trelloCard.getDescription());

        return requestBodyMap;
    }
    
    private Map buildBugBody(TrelloCard trelloCard, Map requestBodyMap){
        requestBodyMap.put("name", this.buildBugName());
        requestBodyMap.put("desc", trelloCard.getDescription());
        requestBodyMap.put("idLabels", Collections.singletonList(this.bugLabelId));
        //se debe generar metodo para obtener id de usuario random y setearlo en el body para que se asignen los usuarios
        //TODO requestBodyMap.put("USERS", getRandomUserList());
        return requestBodyMap;
    }

    private String buildBugName() {
        return new StringBuilder().append("bug-")
                .append(getRandomWord()).append("-")
                .append(getRandomNumber()).toString();
    }

    private String getRandomWord() {
        // random logic to stablish a random word
        return "randomWord";
    }

    private int getRandomNumber() {
        return new Random().nextInt(1000);
    }

    private void addCommonHeaders(HttpHeaders httpHeaders) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    private Map<String, String> buildLabelsMap(
            String maintenanceLabelId,
            String researchLabelId,
            String testLabelId
    ) {
        Map<String, String> trelloParameters = new HashMap<>();
        trelloParameters.put("Maintenance", maintenanceLabelId);
        trelloParameters.put("Research", researchLabelId);
        trelloParameters.put("Test", testLabelId);
        return trelloParameters;
    }
}

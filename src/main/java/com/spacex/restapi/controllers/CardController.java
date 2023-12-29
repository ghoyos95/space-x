package com.spacex.restapi.controllers;

import com.spacex.restapi.exceptions.TrelloApiException;
import com.spacex.restapi.models.TrelloCard;
import com.spacex.restapi.services.TrelloService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/trello")
public class CardController {
    
    private final TrelloService trelloService;
    @Autowired
    public CardController(TrelloService trelloService) {
        this.trelloService = trelloService;
    }

    @PostMapping("/createCard")
    public ResponseEntity<String> createCard(@RequestBody TrelloCard trelloCard) {
        try {
            
            String cardInfo = trelloService.createCard(trelloCard).block();
            log.debug("Card created successfully: " + cardInfo);
            return ResponseEntity.ok("Card created successfully: " + cardInfo);
            
        } catch (TrelloApiException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Failed to create card: " + e.getMessage());
            
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}

package com.spacex.restapi.controllers;

import com.spacex.restapi.models.TrelloCard;
import com.spacex.restapi.services.TrelloService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class CardController {
    
    private final TrelloService trelloService;
    @Autowired
    public CardController(TrelloService trelloService) {
        this.trelloService = trelloService;
    }

    @PostMapping("/createCard")
    public ResponseEntity<String> createCard(@RequestBody TrelloCard trelloCard) {
        try {
            
            trelloService.createCard(trelloCard);
            return ResponseEntity.ok("Card created successfully with");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

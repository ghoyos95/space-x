package com.spacex.restapi.models;

import lombok.Data;

@Data
public class TrelloCard {
    private String type;
    private String title;
    private String description;
    private String category;
}

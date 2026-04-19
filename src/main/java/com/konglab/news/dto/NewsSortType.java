package com.konglab.news.dto;

import lombok.Getter;

public enum NewsSortType {
    LATEST("최신순"),
    RELEVANCE("관련도순");

    @Getter
    private final String description;

    NewsSortType(String description) {
        this.description = description;
    }

    public static NewsSortType from(String description) {
        String lowerCaseDescription = description.toLowerCase();
        return switch(lowerCaseDescription) {
            case "latest" -> LATEST;
            case "relevance" -> RELEVANCE;
            default -> throw new IllegalArgumentException("Invalid news sort type: " + description);
        };
    }
}

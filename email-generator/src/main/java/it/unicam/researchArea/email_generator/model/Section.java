package it.unicam.researchArea.email_generator.model;

public record Section (String section,
                       String title,
                       String deadline,
                       String description,
                       String link,
                       String directApplicationLink,
                       String type,
                       boolean isYoung,
                       boolean isUrgent) {}
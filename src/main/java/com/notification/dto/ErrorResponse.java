package com.notification.dto;

public record ErrorResponse(
        String message,
        Integer statusCode,
        Long timestamp
) { }

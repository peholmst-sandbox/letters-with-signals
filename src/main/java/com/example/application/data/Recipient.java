package com.example.application.data;

public record Recipient(
        String name,
        String emailAddress,
        boolean requireResponse
) {
}

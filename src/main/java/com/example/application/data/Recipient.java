package com.example.application.data;

import java.util.UUID;

public record Recipient(
        UUID id,
        String name,
        String emailAddress,
        boolean requireResponse
) {

    public Recipient withName(String name) {
        return new Recipient(id, name, emailAddress, requireResponse);
    }

    public Recipient withEmailAddress(String emailAddress) {
        return new Recipient(id, name, emailAddress, requireResponse);
    }

    public Recipient withRequireResponse(boolean requireResponse) {
        return new Recipient(id, name, emailAddress, requireResponse);
    }

    public static Recipient empty() {
        return new Recipient(UUID.randomUUID(), "", "", false);
    }
}

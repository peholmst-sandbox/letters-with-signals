package com.example.application.data;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record Recipient(
        UUID id,
        String name,
        @Nullable EmailAddress emailAddress,
        boolean requireResponse
) {

    public Recipient withName(String name) {
        return new Recipient(id, name, emailAddress, requireResponse);
    }

    public Recipient withEmailAddress(EmailAddress emailAddress) {
        return new Recipient(id, name, emailAddress, requireResponse);
    }

    public Recipient withRequireResponse(boolean requireResponse) {
        return new Recipient(id, name, emailAddress, requireResponse);
    }

    public static Recipient empty() {
        return new Recipient(UUID.randomUUID(), "", null, false);
    }

    public boolean isValid() {
        return emailAddress != null;
    }
}

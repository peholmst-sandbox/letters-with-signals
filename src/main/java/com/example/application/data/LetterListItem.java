package com.example.application.data;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record LetterListItem(
        UUID id,
        String subject,
        Instant lastUpdated,
        LetterState state
) {
}

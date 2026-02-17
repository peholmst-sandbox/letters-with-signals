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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LetterListItem that = (LetterListItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

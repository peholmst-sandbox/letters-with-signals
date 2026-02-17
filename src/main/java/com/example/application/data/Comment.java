package com.example.application.data;

import java.time.Instant;
import java.util.UUID;

public record Comment(
        UUID commentID, Instant timestamp, String comment
) {
}

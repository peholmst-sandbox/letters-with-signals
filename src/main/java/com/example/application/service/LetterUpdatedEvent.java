package com.example.application.service;

import com.example.application.data.Letter;

import java.time.Instant;
import java.util.UUID;

public record LetterUpdatedEvent(UUID eventId, Instant eventInstant, Letter letter) {
}

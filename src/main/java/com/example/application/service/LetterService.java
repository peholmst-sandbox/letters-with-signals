package com.example.application.service;

import com.example.application.data.Letter;
import com.example.application.data.LetterListItem;
import com.example.application.data.LetterState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class LetterService {

    private static final Logger log = LoggerFactory.getLogger(LetterService.class);

    // This service simulates a traditional database.

    private final Clock clock = Clock.systemUTC();
    private final ConcurrentMap<UUID, Letter> letters = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    public LetterService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public List<LetterListItem> findLetters() {
        introduceArtificialLatency();
        log.info("Finding all letters");
        return letters.values().stream()
                .sorted(Comparator.comparing(Letter::lastUpdated))
                .map(Letter::toLetterListItem)
                .toList();
    }

    public Optional<Letter> findLetter(UUID id) {
        introduceArtificialLatency();
        log.info("Finding letter by ID {}", id);
        return Optional.ofNullable(letters.get(id));
    }

    public Letter saveLetter(Letter letter) {
        introduceArtificialLatency();
        if (letter.state() != LetterState.DRAFT) {
            throw new IllegalArgumentException("Letter state is not DRAFT");
        }
        return doSaveLetter(letter);
    }

    public Letter markReadyForConfirmation(Letter letter) {
        introduceArtificialLatency();
        return doSaveLetter(letter.readyForConfirmation());
    }

    public Letter confirmLetter(Letter letter) {
        introduceArtificialLatency();
        return doSaveLetter(letter.confirm());
    }

    public Letter requestChanges(Letter letter, String reviewComment) {
        introduceArtificialLatency();
        return doSaveLetter(letter.requestChanges(reviewComment));
    }

    public Letter sendLetter(Letter letter) {
        introduceArtificialLatency();
        return doSaveLetter(letter.sent());
    }

    private Letter doSaveLetter(Letter letter) {
        var newLetter = letters.compute(letter.id(), (key, value) -> {
            if (value != null && value.version() != letter.version()) {
                throw new IllegalStateException("Optimistic locking error");
            }
            var saved = letter.incrementVersion(clock);
            log.info("Saving letter {} (old version {})", saved, letter.version());
            return saved;
        });
        eventPublisher.publishEvent(new LetterUpdatedEvent(UUID.randomUUID(), clock.instant(), newLetter));
        return newLetter;
    }

    public Letter createLetter() {
        var id = UUID.randomUUID();
        log.info("Creating letter {}", id);
        return saveLetter(new Letter(id, 1, "", "", clock.instant(), LetterState.DRAFT,
                Collections.emptyList(), Collections.emptyList(), null));
    }

    private void introduceArtificialLatency() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

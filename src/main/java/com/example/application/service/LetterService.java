package com.example.application.service;

import com.example.application.data.Letter;
import com.example.application.data.LetterListItem;
import com.example.application.data.LetterState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public List<LetterListItem> findLetters() {
        introduceArtificialLatency();
        log.info("Finding all letters");
        return letters.values().stream()
                .sorted(Comparator.comparing(Letter::lastUpdated))
                .map(Letter::toLetterListItem)
                .toList();
    }

    public Optional<Letter> findLetterByItem(LetterListItem item) {
        introduceArtificialLatency();
        log.info("Finding letter by ID {}", item.id());
        return Optional.ofNullable(letters.get(item.id()));
    }

    public Letter saveLetter(Letter letter) {
        introduceArtificialLatency();
        return letters.compute(letter.id(), (key, value) -> {
            if (value != null && value.version() != letter.version()) {
                throw new IllegalStateException("Optimistic locking error");
            }
            var saved = letter.incrementVersion(clock);
            log.info("Saving letter {} (old version {})", saved, letter.version());
            return saved;
        });
    }

    public LetterListItem createLetter() {
        var id = UUID.randomUUID();
        log.info("Creating letter {}", id);
        return saveLetter(new Letter(id, 1, "", "", clock.instant(), LetterState.DRAFT,
                Collections.emptyList(), Collections.emptyList())).toLetterListItem();
    }

    private void introduceArtificialLatency() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

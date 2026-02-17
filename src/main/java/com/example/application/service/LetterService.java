package com.example.application.service;

import com.example.application.data.Letter;
import com.example.application.data.LetterListItem;
import com.example.application.data.LetterState;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class LetterService {

    // This service simulates a traditional database.

    private final Clock clock = Clock.systemUTC();
    private final ConcurrentMap<UUID, Letter> letters = new ConcurrentHashMap<>();

    public List<LetterListItem> findLetters() {
        return letters.values().stream()
                .sorted(Comparator.comparing(Letter::lastUpdated))
                .map(Letter::toLetterListItem)
                .toList();
    }

    public Optional<Letter> findLetterByItem(LetterListItem item) {
        return Optional.ofNullable(letters.get(item.id()));
    }

    public Letter saveLetter(Letter letter) {
        return letters.compute(letter.id(), (key, value) -> {
            if (value != null && value.version() != letter.version()) {
                throw new IllegalStateException("Optimistic locking error");
            }
            return letter.incrementVersion(clock);
        });
    }

    public LetterListItem createLetter() {
        return saveLetter(new Letter(UUID.randomUUID(), 1, "", "", clock.instant(), LetterState.DRAFT,
                Collections.emptyList(), Collections.emptyList())).toLetterListItem();
    }
}

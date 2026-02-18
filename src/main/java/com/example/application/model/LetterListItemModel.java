package com.example.application.model;

import com.example.application.data.Letter;
import com.example.application.data.LetterListItem;
import com.example.application.data.LetterState;
import com.example.application.service.LetterService;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.local.ValueSignal;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.example.application.util.CustomSignalUtil.nullSafe;

public class LetterListItemModel {

    private final LetterService letterService;
    private final ValueSignal<LetterListItem> item;
    private final Signal<String> subject;
    private final Signal<LetterState> state;
    private final Signal<Instant> lastUpdated;

    LetterListItemModel(LetterService letterService, LetterListItem letterListItem) {
        this.letterService = letterService;
        item = new ValueSignal<>(letterListItem);
        subject = item.map(nullSafe(LetterListItem::subject, ""));
        state = item.map(nullSafe(LetterListItem::state, null));
        lastUpdated = item.map(nullSafe(LetterListItem::lastUpdated, null));
    }

    LetterModel toLetterModel() {
        var letterSignal = item.map(
                item -> Optional.ofNullable(item).flatMap(letterService::findLetterByItem).orElse(null),
                (item, letter) -> Optional.ofNullable(letter).map(Letter::toLetterListItem).orElse(null));
        return new LetterModel(letterService, letterSignal);
    }

    public UUID id() {
        return Objects.requireNonNull(item.peek()).id();
    }


    public Signal<String> subject() {
        return subject;
    }

    public Signal<LetterState> state() {
        return state;
    }

    public Signal<Instant> lastUpdated() {
        return lastUpdated;
    }
}

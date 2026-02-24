package com.example.application.model;

import com.example.application.data.LetterListItem;
import com.example.application.data.LetterState;
import com.example.application.service.LetterService;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.local.ValueSignal;

import java.time.Instant;
import java.util.UUID;

import static com.example.application.util.CustomSignalUtil.nullSafe;
import static java.util.Objects.requireNonNull;

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
        var letterSignal = new ValueSignal<>(letterService.findLetterByItem(requireNonNull(item.peek())).orElse(null));
        return new LetterModel(letterService, letterSignal, letter -> {
            letterSignal.set(letter);
            item.set(letter.toLetterListItem());
        });
    }

    public UUID id() {
        return requireNonNull(item.peek()).id();
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

package com.example.application.model;

import com.example.application.data.Letter;
import com.example.application.data.LetterListItem;
import com.example.application.service.LetterService;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.local.ValueSignal;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class LetterListItemModel {

    private final LetterService letterService;
    private final ValueSignal<LetterListItem> item;

    LetterListItemModel(LetterService letterService, LetterListItem letterListItem) {
        this.letterService = letterService;
        item = new ValueSignal<>(letterListItem);
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

    public Signal<LetterListItem> item() {
        return item;
    }
}

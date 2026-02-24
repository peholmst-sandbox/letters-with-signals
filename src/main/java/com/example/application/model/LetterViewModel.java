package com.example.application.model;

import com.example.application.service.LetterService;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.local.ValueSignal;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class LetterViewModel {

    private final LetterService letterService;
    private final ValueSignal<List<LetterListItemModel>> listItems = new ValueSignal<>(List.of());
    private final ValueSignal<LetterListItemModel> listSelection = new ValueSignal<>(null);
    private final Signal<LetterModel> letter;

    public LetterViewModel(LetterService letterService) {
        this.letterService = letterService;
        letter = listSelection.map(itemListModel -> Optional.ofNullable(itemListModel)
                .map(LetterListItemModel::toLetterModel).orElse(null));
    }

    public void refresh() {
        listItems.set(letterService.findLetters().stream().map(item -> new LetterListItemModel(letterService, item)).toList());
    }

    public Signal<List<LetterListItemModel>> listItems() {
        return listItems;
    }

    public ValueSignal<LetterListItemModel> listSelection() {
        return listSelection;
    }

    public void selectById(UUID letterId) {
        listSelection.set(requireNonNull(listItems.peek()).stream().filter(itemModel -> itemModel.id().equals(letterId)).findFirst().orElse(null));
    }

    public void deselect() {
        listSelection.set(null);
    }

    public Signal<LetterModel> letter() {
        return letter;
    }

    public void addLetter() {
        var newLetter = new LetterListItemModel(letterService, letterService.createLetter());
        var newEntry = new ArrayList<>(requireNonNull(listItems.peek()));
        newEntry.addFirst(newLetter);
        listItems.set(Collections.unmodifiableList(newEntry));
        listSelection.set(newLetter);
    }
}

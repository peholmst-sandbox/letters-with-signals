package com.example.application.model;

import com.example.application.service.LetterService;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.local.ValueSignal;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class LetterViewModel {

    private final LetterService letterService;
    private final ValueSignal<List<LetterListItemModel>> listItems = new ValueSignal<>(List.of());
    private final ValueSignal<LetterListItemModel> listSelection = new ValueSignal<>();
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
        var selectedItem = listSelection.peek();
        // Need this check to avoid an infinite loop when called by setParameter(..)
        if (selectedItem == null || !selectedItem.id().equals(letterId)) {
            listSelection.set(requireNonNull(listItems.peek()).stream().filter(itemModel -> itemModel.id().equals(letterId)).findFirst().orElse(null));
        }
    }

    public void deselect() {
        // Need this check to avoid an infinite loop when called by setParameter(..)
        if (listSelection.peek() != null) {
            listSelection.set(null);
        }
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

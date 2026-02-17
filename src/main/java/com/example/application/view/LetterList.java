package com.example.application.view;

import com.example.application.model.LetterListItemModel;
import com.example.application.model.LetterViewModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import static com.example.application.util.CustomSignalUtil.bindItems;
import static com.example.application.util.CustomSignalUtil.bindSelection;

final class LetterList extends VerticalLayout {

    LetterList(LetterViewModel letterViewModel) {
        var addLetterButton = new Button("New Letter", event -> letterViewModel.addLetter());
        addLetterButton.addThemeVariants(ButtonVariant.PRIMARY);

        Grid<LetterListItemModel> grid = new Grid<>();
        grid.addColumn(new ComponentRenderer<>(LetterListCard::new));
        bindItems(grid, letterViewModel.listItems());
        bindSelection(grid, letterViewModel.listSelection());

        setSizeFull();
        add(addLetterButton);
        add(grid);
        grid.setSizeFull();
    }
}

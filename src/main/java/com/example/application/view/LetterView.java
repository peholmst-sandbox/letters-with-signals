package com.example.application.view;

import com.example.application.model.LetterViewModel;
import com.example.application.service.LetterService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.signals.Signal;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Route("letter")
public class LetterView extends MasterDetailLayout implements HasUrlParameter<String> {

    private final LetterViewModel viewModel;
    private static final int MASTER_WIDTH_PX = 350;

    LetterView(LetterService letterService) {
        viewModel = new LetterViewModel(letterService);
        var letterList = new LetterList(viewModel);
        letterList.setWidth(MASTER_WIDTH_PX, Unit.PIXELS);
        setMaster(letterList);
        setMasterSize(MASTER_WIDTH_PX, Unit.PIXELS);
        setSizeFull();

        // Bind detail to current letter
        Signal.effect(this, () -> {
            var letterModel = viewModel.letter().get();
            if (letterModel != null) {
                setDetail(new LetterEditor(letterModel));
            } else {
                setDetail(null);
            }
        });

        // Bind current selection to URL parameter
        Signal.effect(this, () -> {
            var selectedItem = viewModel.listSelection().get();
            if (selectedItem != null) {
                UI.getCurrent().navigate(LetterView.class, selectedItem.id().toString());
            } else {
                UI.getCurrent().navigate(LetterView.class);
            }
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        viewModel.refresh();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter @Nullable String letterId) {
        // Bind URL parameter to current selection
        if (letterId != null) {
            viewModel.selectById(UUID.fromString(letterId));
        } else {
            viewModel.deselect();
        }
    }
}

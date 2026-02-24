package com.example.application.view;

import com.example.application.data.Recipient;
import com.example.application.model.LetterModel;
import com.example.application.model.RecipientModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.signals.Signal;

class RecipientSubview extends VerticalLayout {

    private final LetterModel letterModel;

    RecipientSubview(LetterModel letterModel) {
        this.letterModel = letterModel;
        var addButton = new Button("Add Recipient", e -> letterModel.addRecipient());

        var recipients = new VerticalLayout();
        recipients.bindChildren(letterModel.recipients(), this::createRecipientCard);
        recipients.setPadding(false);
        recipients.setSpacing(true);
        recipients.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        var scroller = new Scroller(recipients);
        scroller.setSizeFull();

        add(addButton, scroller);
        setSizeFull();
        setPadding(false);
    }

    private Component createRecipientCard(Signal<Recipient> recipient) {
        var recipientModel = new RecipientModel(letterModel, recipient, letterModel::updateRecipient);
        var card = new Div();
        card.addClassNames("recipient-card");

        var name = new TextField();
        name.setPlaceholder("Name");
        name.setValueChangeMode(ValueChangeMode.LAZY);
        name.bindValue(recipientModel.name(), recipientModel::setName);
        name.bindReadOnly(recipientModel.readOnly());
        name.setWidthFull();

        var email = new EmailField();
        email.setPlaceholder("Email");
        email.setValueChangeMode(ValueChangeMode.LAZY);
        email.bindValue(recipientModel.emailAddress(), recipientModel::setEmailAddress);
        email.bindReadOnly(recipientModel.readOnly());
        email.setWidthFull();

        var requireResponse = new Checkbox("Require Response");
        requireResponse.bindValue(recipientModel.requireResponse(), recipientModel::setRequireResponse);
        requireResponse.bindReadOnly(recipientModel.readOnly());

        var remove = new Button(VaadinIcon.CLOSE.create(),e -> recipientModel.remove());
        remove.bindVisible(Signal.not(recipientModel.readOnly()));

        var nameAndEmail = new Div(name, email);
        nameAndEmail.addClassNames("name-and-email");

        var topRow = new Div(nameAndEmail, remove);
        topRow.addClassNames("row");
        topRow.setWidthFull();

        card.add(topRow, requireResponse);

        return card;
    }
}

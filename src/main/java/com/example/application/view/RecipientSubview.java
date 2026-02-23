package com.example.application.view;

import com.example.application.data.Recipient;
import com.example.application.model.LetterModel;
import com.example.application.model.RecipientModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.signals.Signal;

class RecipientSubview extends VerticalLayout {

    private final LetterModel letterModel;

    RecipientSubview(LetterModel letterModel) {
        this.letterModel = letterModel;
        var addButton = new Button("Add Recipient", e -> letterModel.addRecipient());

        // TODO Fix scrolling, margins, etc.
        var recipients = new VerticalLayout();
        recipients.bindChildren(letterModel.recipients(), this::createRecipientCard);

        add(addButton, recipients);
    }

    private Component createRecipientCard(Signal<Recipient> recipient) {
        var recipientModel = new RecipientModel(letterModel, recipient, letterModel::updateRecipient);
        var card = new VerticalLayout();

        var name = new TextField();
        name.bindValue(recipientModel.name(), recipientModel::setName);
        name.bindReadOnly(recipientModel.readOnly());

        var email = new EmailField();
        email.bindValue(recipientModel.emailAddress(), recipientModel::setEmailAddress);
        email.bindReadOnly(recipientModel.readOnly());

        var requireResponse = new Checkbox();
        requireResponse.bindValue(recipientModel.requireResponse(), recipientModel::setRequireResponse);
        requireResponse.bindReadOnly(recipientModel.readOnly());

        var remove = new Button("Remove", e -> recipientModel.remove());
        remove.bindVisible(Signal.not(recipientModel.readOnly()));

        // TODO Fix visual layout
        card.add(name, email, requireResponse, remove);
        return card;
    }
}

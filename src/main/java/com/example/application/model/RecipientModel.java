package com.example.application.model;

import com.example.application.converter.EmailAddressConverter;
import com.example.application.data.EmailAddress;
import com.example.application.data.Recipient;
import com.example.application.util.ConvertedSignal;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.signals.Signal;

import java.util.function.Function;

import static com.example.application.util.CustomSignalUtil.nullSafe;
import static java.util.Objects.requireNonNull;

public class RecipientModel {

    private final LetterModel letterModel;
    private final Signal<Recipient> recipient;
    private final SerializableConsumer<Recipient> writeCallback;

    private final Signal<String> name;
    private final ConvertedSignal<EmailAddress, String> emailAddress;
    private final Signal<Boolean> requireResponse;

    public RecipientModel(LetterModel letterModel, Signal<Recipient> recipient, SerializableConsumer<Recipient> writeCallback) {
        this.letterModel = letterModel;
        this.recipient = recipient;
        this.writeCallback = writeCallback;

        this.name = recipient.map(nullSafe(Recipient::name, ""));
        this.emailAddress = new ConvertedSignal<>(recipient.map(nullSafe(Recipient::emailAddress, null)), this::setEmailAddress, new EmailAddressConverter());
        this.requireResponse = recipient.map(nullSafe(Recipient::requireResponse, false));
    }

    private void update(Function<Recipient, Recipient> updater) {
        if (!requireNonNull(readOnly().peek())) {
            writeCallback.accept(updater.apply(requireNonNull(recipient.peek())));
        }
    }

    public Signal<String> name() {
        return name;
    }

    public void setName(String name) {
        update(r -> r.withName(name));
    }

    public ConvertedSignal<EmailAddress, String> emailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        update(r -> r.withEmailAddress(emailAddress));
    }

    public Signal<Boolean> requireResponse() {
        return requireResponse;
    }

    public void setRequireResponse(boolean requireResponse) {
        update(r -> r.withRequireResponse(requireResponse));
    }

    public Signal<Boolean> readOnly() {
        return letterModel.readOnly();
    }

    public void remove() {
        letterModel.removeRecipient(requireNonNull(recipient.peek()));
    }
}

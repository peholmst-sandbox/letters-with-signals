package com.example.application.model;

import com.example.application.data.Letter;
import com.example.application.data.LetterState;
import com.example.application.data.Recipient;
import com.example.application.service.LetterService;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.signals.Signal;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

import static com.example.application.util.CustomSignalUtil.mapList;
import static com.example.application.util.CustomSignalUtil.nullSafe;
import static com.example.application.util.ListUtil.*;
import static java.util.Objects.requireNonNull;

public class LetterModel {

    private final LetterService letterService;
    private final Signal<Letter> letter;
    private final SerializableConsumer<Letter> writeCallback;

    private final Signal<String> subject;
    private final Signal<String> body;
    private final Signal<Instant> lastUpdated;
    private final Signal<LetterState> state;
    private final Signal<List<Signal<Recipient>>> recipients;
    private final Signal<Boolean> readOnly;

    // TODO Attachments?
    // TODO Recipients?
    // TODO Comments?

    LetterModel(LetterService letterService, Signal<Letter> letter, SerializableConsumer<Letter> writeCallback) {
        this.letterService = letterService;
        this.letter = letter;
        this.writeCallback = writeCallback;

        subject = letter.map(nullSafe(Letter::subject, ""));
        body = letter.map(nullSafe(Letter::body, ""));
        lastUpdated = letter.map(nullSafe(Letter::lastUpdated, null));
        state = letter.map(nullSafe(Letter::state, null));
        recipients = mapList(letter, Letter::recipients, Recipient::id);
        readOnly = letter.map(nullSafe(l -> l.state() != LetterState.DRAFT, true));
    }

    private void update(Function<Letter, Letter> updater) {
        if (canEdit()) {
            writeCallback.accept(letterService.saveLetter(updater.apply(requireNonNull(letter.peek()))));
        }
    }

    public Signal<String> subject() {
        return subject;
    }

    public void setSubject(String subject) {
        update(l -> l.withSubject(subject));
    }

    public Signal<String> body() {
        return body;
    }

    public void setBody(String body) {
        update(l -> l.withBody(body));
    }

    public Signal<Instant> lastUpdated() {
        return lastUpdated;
    }

    public Signal<LetterState> state() {
        return state;
    }

    public Signal<List<Signal<Recipient>>> recipients() {
        return recipients;
    }

    public void addRecipient() {
        update(l -> l.withRecipients(add(l.recipients(), Recipient.empty())));
    }

    public void removeRecipient(Recipient recipient) {
        update(l -> l.withRecipients(remove(l.recipients(), recipient)));
    }

    public void updateRecipient(Recipient newRecipient) {
        update(l -> l.withRecipients(replace(l.recipients(), newRecipient, Recipient::id)));
    }

    public Signal<Boolean> readOnly() {
        return readOnly;
    }

    private boolean canEdit() {
        return !requireNonNull(readOnly.peek());
    }

    private boolean canSend() {
        return true; // TODO Change this!
    }

    public void ready() {

    }

    public void send() {

    }
    // TODO Can send (there is a subject and at least one valid recipient)
}

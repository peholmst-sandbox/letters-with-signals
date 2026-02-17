package com.example.application.model;

import com.example.application.data.Letter;
import com.example.application.data.LetterState;
import com.example.application.service.LetterService;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.WritableSignal;
import com.vaadin.flow.signals.function.ValueMerger;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

import static com.example.application.util.CustomSignalUtil.nullSafe;
import static java.util.Objects.requireNonNull;

public class LetterModel {

    private final LetterService letterService;

    private final WritableSignal<String> subject;
    private final WritableSignal<String> body;
    private final Signal<Instant> lastUpdated;
    private final Signal<LetterState> state;
    private final Signal<Boolean> readOnly;
    // TODO Attachments?
    // TODO Recipients?
    // TODO Comments?

    LetterModel(LetterService letterService, WritableSignal<Letter> letter) {
        this.letterService = letterService;
        subject = letter.map(
                nullSafe(Letter::subject, ""),
                saveAfterMerge(nullSafe(Letter::withSubject)));
        body = letter.map(
                nullSafe(Letter::body, ""),
                saveAfterMerge(nullSafe(Letter::withBody))
        );
        lastUpdated = letter.map(nullSafe(Letter::lastUpdated, null));
        state = letter.map(nullSafe(Letter::state, null));
        readOnly = letter.map(nullSafe(l -> l.state() != LetterState.DRAFT, true));
    }

    private <I> ValueMerger<Letter, I> saveAfterMerge(ValueMerger<Letter, I> valueMerger) {
        return new ValueMerger<>() {

            @Override
            public @Nullable Letter merge(@Nullable Letter outerValue, @Nullable I newInnerValue) {
                var result = valueMerger.merge(outerValue, newInnerValue);
                if (result != null) {
                    return letterService.saveLetter(result);
                } else {
                    return null;
                }
            }
        };
    }

    public Signal<String> subject() {
        return subject;
    }

    public void setSubject(String subject) {
        if (canEdit()) {
            this.subject.set(subject);
        }
    }

    public Signal<String> body() {
        return body;
    }

    public void setBody(String body) {
        if (canEdit()) {
            this.body.set(body);
        }
    }

    public Signal<Instant> lastUpdated() {
        return lastUpdated;
    }

    public Signal<LetterState> state() {
        return state;
    }

    public Signal<Boolean> readOnly() {
        return readOnly;
    }

    private boolean canEdit() {
        return !requireNonNull(readOnly.get());
    }

    // TODO Can send (there is a subject and at least one recipient)
}

package com.example.application.data;

import org.jspecify.annotations.Nullable;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Letter(
        UUID id,
        long version,
        String subject,
        String body,
        Instant lastUpdated,
        LetterState state,
        List<Attachment> attachments,
        List<Recipient> recipients,
        @Nullable String reviewComment
) {

    public Letter(UUID id, long version, String subject, String body, Instant lastUpdated, LetterState state,
                  List<Attachment> attachments, List<Recipient> recipients, @Nullable String reviewComment) {
        this.id = id;
        this.version = version;
        this.subject = subject;
        this.body = body;
        this.lastUpdated = lastUpdated;
        this.state = state;
        this.attachments = List.copyOf(attachments);
        this.recipients = List.copyOf(recipients);
        this.reviewComment = reviewComment;
    }

    public Letter incrementVersion(Clock clock) {
        return new Letter(id, version + 1, subject, body, clock.instant(), state, attachments, recipients, reviewComment);
    }

    public Letter withSubject(String subject) {
        return new Letter(id, version, subject, body, lastUpdated, state, attachments, recipients, reviewComment);
    }

    public Letter withBody(String body) {
        return new Letter(id, version, subject, body, lastUpdated, state, attachments, recipients, reviewComment);
    }

    public Letter withRecipients(List<Recipient> recipients) {
        return new Letter(id, version, subject, body, lastUpdated, state, attachments, recipients, reviewComment);
    }

    public Letter readyForConfirmation() {
        if (state != LetterState.DRAFT) {
            throw new IllegalStateException("state is not DRAFT");
        }
        return new Letter(id, version, subject, body, lastUpdated, LetterState.READY_FOR_CONFIRMATION, attachments, recipients, reviewComment);
    }

    public Letter confirm() {
        if (state != LetterState.READY_FOR_CONFIRMATION) {
            throw new IllegalStateException("state is not READY_FOR_CONFIRMATION");
        }
        return new Letter(id, version, subject, body, lastUpdated, LetterState.CONFIRMED, attachments, recipients, reviewComment);
    }

    public Letter requestChanges(String reviewComment) {
        if (state != LetterState.READY_FOR_CONFIRMATION) {
            throw new IllegalStateException("state is not READY_FOR_CONFIRMATION");
        }
        return new Letter(id, version, subject, body, lastUpdated, LetterState.DRAFT, attachments, recipients, reviewComment);
    }

    public Letter sent() {
        if (state != LetterState.CONFIRMED) {
            throw new IllegalStateException("state is not CONFIRMED");
        }
        return new Letter(id, version, subject, body, lastUpdated, LetterState.SENT, attachments, recipients, reviewComment);
    }

    public LetterListItem toLetterListItem() {
        return new LetterListItem(id, subject, lastUpdated, state);
    }
}

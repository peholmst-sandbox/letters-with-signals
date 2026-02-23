package com.example.application.data;

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
        List<Recipient> recipients
) {

    public Letter(UUID id, long version, String subject, String body, Instant lastUpdated, LetterState state,
                  List<Attachment> attachments, List<Recipient> recipients) {
        this.id = id;
        this.version = version;
        this.subject = subject;
        this.body = body;
        this.lastUpdated = lastUpdated;
        this.state = state;
        this.attachments = List.copyOf(attachments);
        this.recipients = List.copyOf(recipients);
    }

    public Letter incrementVersion(Clock clock) {
        return new Letter(id, version + 1, subject, body, clock.instant(), state, attachments, recipients);
    }

    public Letter withSubject(String subject) {
        return new Letter(id, version, subject, body, lastUpdated, state, attachments, recipients);
    }

    public Letter withBody(String body) {
        return new Letter(id, version, subject, body, lastUpdated, state, attachments, recipients);
    }

    public Letter withRecipients(List<Recipient> recipients) {
        return new Letter(id, version, subject, body, lastUpdated, state, attachments, recipients);
    }

    public LetterListItem toLetterListItem() {
        return new LetterListItem(id, subject, lastUpdated, state);
    }
}

package com.example.application.view;

import com.example.application.data.LetterState;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import org.jspecify.annotations.Nullable;

final class LetterStateBadgeFactory {

    private LetterStateBadgeFactory() {
    }

    public static Component createBadge(LetterState letterState) {
        return switch (letterState) {
            case DRAFT -> createBadge("Draft", null);
            case READY -> createBadge("Ready", "blue");
            case SENDING -> createBadge("Send", "yellow");
            case SENT -> createBadge("Sent", "green");
        };
    }

    private static Component createBadge(String text, @Nullable String themeName) {
        var badge = new Span(text);
        badge.getElement().getThemeList().add("badge");
        if (themeName != null && !themeName.isEmpty()) {
            badge.getElement().getThemeList().add(themeName);
        }
        return badge;
    }
}

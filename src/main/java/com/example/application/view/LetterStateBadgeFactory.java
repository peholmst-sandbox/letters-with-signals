package com.example.application.view;

import com.example.application.data.LetterState;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.signals.Signal;
import org.jspecify.annotations.Nullable;

final class LetterStateBadgeFactory {

    private LetterStateBadgeFactory() {
    }

    public static Component createBadge(Signal<LetterState> letterState) {
        var badge = new Span(() -> getBadgeText(letterState.get()));
        var badgeThemeList = badge.getElement().getThemeList();
        badgeThemeList.add("badge");
        badgeThemeList.bind("blue", () -> letterState.get() == LetterState.READY);
        badgeThemeList.bind("yellow", () -> letterState.get() == LetterState.SENDING);
        badgeThemeList.bind("green", () -> letterState.get() == LetterState.SENT);
        return badge;
    }

    private static String getBadgeText(@Nullable LetterState letterState) {
        if (letterState == null) {
            return "";
        }
        return switch (letterState) {
            case DRAFT -> "Draft";
            case READY -> "Ready";
            case SENDING -> "Sending";
            case SENT -> "Sent";
        };
    }

    @Deprecated
    public static Component createBadge(LetterState letterState) {
        return switch (letterState) {
            case DRAFT -> createBadge("Draft", null);
            case READY -> createBadge("Ready", "blue");
            case SENDING -> createBadge("Send", "yellow");
            case SENT -> createBadge("Sent", "green");
        };
    }

    @Deprecated
    private static Component createBadge(String text, @Nullable String themeName) {
        var badge = new Span(text);
        badge.getElement().getThemeList().add("badge");
        if (themeName != null && !themeName.isEmpty()) {
            badge.getElement().getThemeList().add(themeName);
        }
        return badge;
    }
}

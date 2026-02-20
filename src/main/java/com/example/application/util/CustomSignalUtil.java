package com.example.application.util;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.function.SignalMapper;
import com.vaadin.flow.signals.function.ValueMerger;
import com.vaadin.flow.signals.local.ValueSignal;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class CustomSignalUtil {

    private CustomSignalUtil() {
    }

    public static <T, R> SignalMapper<T, R> nullSafe(NullSafeSignalMapper<T, R> mapper, @Nullable R defaultValue) {
        return new SignalMapper<>() {
            @Override
            public @Nullable R map(@Nullable T value) {
                if (value == null) {
                    return defaultValue;
                } else {
                    return mapper.map(value);
                }
            }
        };
    }

    public static <T> Registration bindItems(Grid<T> grid, Signal<List<T>> signal) {
        return Signal.effect(grid, () -> {
            grid.setItems(signal.get());
        });
    }

    public static <T> Registration bindSelection(Grid<T> grid, ValueSignal<T> signal) {
        var gridListener = grid.addSelectionListener(event -> {
            T selectedItem = event.getFirstSelectedItem().orElse(null);
            // Avoid infinite loop
            if (!Objects.equals(selectedItem, signal.peek())) {
                signal.set(selectedItem);
            }
        });
        var effect = Signal.effect(grid, () -> {
            T selection = signal.get();
            if (selection == null) {
                grid.deselectAll();
            } else {
                grid.select(selection);
            }
        });
        return Registration.combine(gridListener, effect);
    }
}

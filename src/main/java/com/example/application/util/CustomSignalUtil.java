package com.example.application.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.function.SignalMapper;
import com.vaadin.flow.signals.function.ValueMerger;
import com.vaadin.flow.signals.impl.Effect;
import com.vaadin.flow.signals.local.ValueSignal;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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

    public static <O, I> ValueMerger<O, I> nullSafe(NullSafeValueMerger<O, I> merger) {
        return new ValueMerger<>() {
            @Override
            public @Nullable O merge(@Nullable O outerValue, @Nullable I newInnerValue) {
                if (outerValue == null) {
                    return null;
                } else {
                    return merger.merge(outerValue, newInnerValue);
                }
            }
        };
    }

    public static <T> Registration bindItems(Grid<T> grid, Signal<List<T>> signal) {
        return Effect.effect(grid, () -> {
            grid.setItems(signal.get());
        });
    }

    public static <T> Registration bindSelection(Grid<T> grid, ValueSignal<T> signal) {
        var gridListener = grid.addSelectionListener(event -> {
            T selectedItem = event.getFirstSelectedItem().orElse(null);
            // Avoid infinite loop
            if (!Objects.equals(selectedItem, signal.get())) {
                // TODO This is till triggering an infinite loop!
                signal.set(selectedItem);
            }
        });
        var effect = Effect.effect(grid, () -> {
            T selection = signal.get();
            if (selection == null) {
                grid.deselectAll();
            } else {
                grid.select(selection);
            }
        });
        return Registration.combine(gridListener, effect);
    }

    public static <C extends Component & HasComponents, T> Registration bindChild(C component, Signal<T> signal, Function<T, Component> childFactory) {
        return Effect.effect(component, () -> {
            component.removeAll();
            T value = signal.get();
            if (value != null) {
                component.add(childFactory.apply(value));
            }
        });
    }
}

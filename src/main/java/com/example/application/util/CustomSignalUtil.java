package com.example.application.util;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.function.SignalMapper;
import com.vaadin.flow.signals.local.ValueSignal;
import org.jspecify.annotations.Nullable;

import java.util.*;

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
            signal.set(event.getFirstSelectedItem().orElse(null));
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

    public static <T, K, O> Signal<List<Signal<T>>> mapList(Signal<O> outer, NullSafeSignalMapper<O, List<T>> signalMapper, SerializableFunction<T, K> keyMapper) {
        final Map<K, ValueSignal<T>> signals = new HashMap<>();
        return outer.map(l -> {
            var originalList = Optional.ofNullable(l).map(signalMapper::map).orElse(List.of());
            var keys = new HashSet<K>();
            var signalList = originalList.stream().map(i -> {
                var key = keyMapper.apply(i);
                keys.add(key);
                var signal = signals.computeIfAbsent(key, k -> new ValueSignal<>(i));
                signal.set(i);
                return (Signal<T>) signal;
            }).toList();
            signals.keySet().removeIf(key -> !keys.contains(key));
            return signalList;
        });
    }
}

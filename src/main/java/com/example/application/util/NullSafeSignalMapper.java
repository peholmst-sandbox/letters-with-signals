package com.example.application.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;

/**
 * Version of {@link com.vaadin.flow.signals.function.SignalMapper} that requires the value to be non-null.
 *
 * @param <T>
 * @param <R>
 * @see CustomSignalUtil#nullSafe(NullSafeSignalMapper, Object)
 */
@FunctionalInterface
public interface NullSafeSignalMapper<T, R> extends Serializable {

    @Nullable
    R map(T value);
}

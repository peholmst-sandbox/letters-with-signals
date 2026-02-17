package com.example.application.util;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;

/**
 * Version of {@link com.vaadin.flow.signals.function.ValueMerger} that requires the outer value to always be non-null.
 */
public interface NullSafeValueMerger<O, I> extends Serializable {

    O merge(O outerValue, @Nullable I newInnerValue);
}

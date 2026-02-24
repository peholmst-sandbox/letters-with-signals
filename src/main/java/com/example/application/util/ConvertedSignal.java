package com.example.application.util;

import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.local.ValueSignal;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public class ConvertedSignal<M, P> {

    private final Signal<M> modelSignal;
    private final SerializableConsumer<M> writeCallback;
    private final Converter<P, M> converter;
    private final Signal<P> presentationSignal;
    private final ValueSignal<Boolean> invalid = new ValueSignal<>(false);
    private final ValueSignal<String> errorMessage = new ValueSignal<>(null);
    private @Nullable P typedPresentationValue;

    public ConvertedSignal(Signal<M> modelSignal, SerializableConsumer<M> writeCallback, Converter<@Nullable P, @Nullable M> converter) {
        this.modelSignal = modelSignal;
        this.writeCallback = writeCallback;
        this.converter = converter;
        this.presentationSignal = () -> {
            if (requireNonNull(invalid.get())) {
                return typedPresentationValue;
            } else {
                return converter.convertToPresentation(modelSignal.get(), null);
            }
        };
    }

    public Signal<Boolean> invalid() {
        return invalid;
    }

    public Signal<String> errorMessage() {
        return errorMessage;
    }

    public Signal<M> model() {
        return modelSignal;
    }

    public Signal<P> presentation() {
        return presentationSignal;
    }

    public void setPresentation(P presentation) {
        this.typedPresentationValue = presentation;
        var result = converter.convertToModel(presentation, null);
        invalid.set(result.isError());
        errorMessage.set(result.getMessage().orElse(null));
        result.ifOk(writeCallback);
    }
}

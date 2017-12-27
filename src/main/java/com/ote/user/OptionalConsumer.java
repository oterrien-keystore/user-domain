package com.ote.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionalConsumer<T> {

    private final Optional<T> optional;

    public static <T> OptionalConsumer<T> of(Optional<T> optional) {
        return new OptionalConsumer<>(optional);
    }

    public OptionalConsumer<T> ifPresent(Consumer<T> consumer) {
        optional.ifPresent(consumer);
        return this;
    }

    public OptionalConsumer<T> ifNotPresent(Runnable runnable) {
        if (!optional.isPresent()) {
            runnable.run();
        }
        return this;
    }
}
package com.globant.code.camp.fp.utils;

import java.util.stream.Stream;

@FunctionalInterface
public interface RecCall<T> {

  RecCall<T> next();

  default boolean isDone() {
    return false;
  }

  default T get() {
    throw new IllegalStateException("IntermediateCall can't return a value");
  }

  default T apply() {
    return Stream.iterate(this, RecCall::next)
            .filter(RecCall::isDone)
            .findAny()
            .map(RecCall::get)
            .orElseThrow(() -> new RuntimeException("Recursive Final Call never found"));
  }

  record FinalCall<T>(T value) implements RecCall<T> {

    @Override
    public RecCall<T> next() {
      throw new IllegalStateException("FinalCall can't have next");
    }

    @Override
    public boolean isDone() {
      return true;
    }

    @Override
    public T get() {
      return value;
    }

  }

  static <T> RecCall<T> cont(RecCall<T> nextCall) {
    return nextCall;
  }

  static <T> RecCall<T> stop(T value) {
    return new FinalCall<>(value);
  }
}

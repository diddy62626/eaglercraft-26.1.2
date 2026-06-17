package com.lmax.disruptor;

/**
 * EaglerCraft stub for com.lmax.disruptor.EventTranslatorTwoArg.
 * Used by log4j2's AsyncLoggerConfigDisruptor.
 */
public interface EventTranslatorTwoArg<T, A, B> {
    void translateTo(T event, long sequence, A arg0, B arg1);
}

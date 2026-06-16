package com.lmax.disruptor;

/**
 * EaglerCraft stub for com.lmax.disruptor.EventFactory.
 * Used by log4j2's AsyncLoggerConfigDisruptor.
 */
public interface EventFactory<T> {
    T newInstance();
}

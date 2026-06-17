package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.MapCodec;

/**
 * EaglerCraft stub for SimpleMapCodec.
 *
 * Extends BOTH MapCodec and Codec. Since MapCodec is now standalone
 * (not extending Codec), there are name clashes for methods like
 * optionalFieldOf(String) which both define with different return types.
 *
 * Fix: SimpleMapCodec extends ONLY Codec (not MapCodec). It provides
 * a codec() method that returns itself.
 */
public interface SimpleMapCodec<K, V> extends Keyable, Codec<java.util.Map<K, V>> {
}

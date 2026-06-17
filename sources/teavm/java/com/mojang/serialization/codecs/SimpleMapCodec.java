package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.MapCodec;

/**
 * EaglerCraft stub for SimpleMapCodec.
 *
 * In real MC, SimpleMapCodec extends MapCodec AND Codec. But since we made
 * MapCodec standalone (not extending Codec), SimpleMapCodec can extend both
 * without name clashes.
 */
public interface SimpleMapCodec<K, V> extends MapCodec<java.util.Map<K, V>>, Keyable, Codec<java.util.Map<K, V>> {
}

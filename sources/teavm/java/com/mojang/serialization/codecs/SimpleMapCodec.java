package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.MapCodec;

public interface SimpleMapCodec<K, V> extends MapCodec<java.util.Map<K, V>>, Keyable, Codec<java.util.Map<K, V>> {
}

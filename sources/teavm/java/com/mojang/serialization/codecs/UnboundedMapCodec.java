package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import java.util.Map;

public interface UnboundedMapCodec<K, V> extends Codec<Map<K, V>> {
}

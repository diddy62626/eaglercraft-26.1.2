package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.function.Function;

public class RecordCodecBuilder {
    public static <O> Codec<O> create(Function<O, MapCodec<O>> builder) { return null; }
    public static <O> MapCodec<O> mapCodec(Function<O, MapCodec<O>> builder) { return null; }
    public static <O, F> MapCodec<F> forGetter(Function<O, F> getter) { return null; }
    public static <O, F> MapCodec<F> of(MapCodec<F> codec, Function<O, F> getter) { return null; }
}

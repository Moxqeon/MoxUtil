package org.moxqeon.module.math;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 加权随机中使用的不可变权重条目，将对象与权重绑定为 Map.Entry。
 *
 * @param <O> 对象的类型
 */
public final class WeightEntry<O> implements Map.Entry<O, Integer> {
    private final O obj;
    private final int weight;

    public WeightEntry(@NotNull O obj, int weight) {
        this.obj = obj;
        this.weight = weight;
    }

    @Override
    public O getKey() {
        return obj;
    }

    @Override
    public Integer getValue() {
        return weight;
    }

    @Override
    public Integer setValue(Integer value) {
        throw new UnsupportedOperationException("Immutable weight");
    }
}
package org.moxqeon.api.compound;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 可解析的复合字符串，子类负责将列表解析为具体类型。
 *
 * @param <T> 解析结果的类型
 */
public abstract class ParsableCompound<T> extends Compound {
    public ParsableCompound(@NotNull String... strings) {
        super(new ArrayList<>(Arrays.asList(strings)));
    }

    public ParsableCompound(@NotNull String compound, @NotNull String split) {
        this();
        list.addAll(Arrays.asList(compound.split(split)));
    }

    public ParsableCompound(@NotNull String compound) {
        this(compound, DEFAULT_SPLIT);
    }

    public abstract T parse();
}
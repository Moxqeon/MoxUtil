package org.moxqeon.api.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 固定双精度浮点占位符，始终返回预设的浮点数。
 */
public final class FixedDouble implements Placeholder<Double> {
    private final double value;

    public FixedDouble(double value) {
        this.value = value;
    }

    @Override
    @NotNull
    public Double get(@Nullable Object... objects) {
        return value;
    }
}
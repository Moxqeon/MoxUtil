package org.moxqeon.api.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 固定整数占位符，始终返回预设的整数。
 */
public final class FixedInteger implements Placeholder<Integer> {
    private final int value;

    public FixedInteger(int value) {
        this.value = value;
    }

    @Override
    @NotNull
    public Integer get(@Nullable Object... objects) {
        return value;
    }
}
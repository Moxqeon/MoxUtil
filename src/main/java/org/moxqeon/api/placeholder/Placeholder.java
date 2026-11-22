package org.moxqeon.api.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moxqeon.util.StringUtil;

/**
 * 占位符接口，根据可选的输入参数产出指定类型的结果。
 *
 * @param <T> 占位符产出的结果类型
 */
public interface Placeholder<T> {
    @NotNull
    static Placeholder<?> fromString(@NotNull String value) {
        if (value.startsWith("double"))
            return new PlaceholderDouble(value);
        if (value.startsWith("int"))
            return new PlaceholderInteger(value);
        if (StringUtil.isDouble(value))
            return new FixedDouble(Double.parseDouble(value));
        if (StringUtil.isLong(value))
            return new FixedInteger(Integer.parseInt(value));
        return new PlaceholderString(value);
    }

    @NotNull
    T get(@Nullable Object... objects);
}
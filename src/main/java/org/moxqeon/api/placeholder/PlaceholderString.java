package org.moxqeon.api.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 字符串占位符，按格式串填充参数。
 */
public final class PlaceholderString implements Placeholder<String> {
    private final String format;

    PlaceholderString(@NotNull String format) {
        this.format = format.replace("\\{}", "%s");
    }

    @Override
    @NotNull
    public String get(@Nullable Object... objects) {
        return String.format(format, objects);
    }
}
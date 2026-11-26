package org.moxqeon.util;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.jetbrains.annotations.NotNull;

/**
 * StringBuilder 工具类，提供字符串首尾包裹等便捷方法。
 */

@Extension
public final class StringBuilderUtil {
    private StringBuilderUtil() {
        throw new IllegalStateException();
    }

    @NotNull
    public static StringBuilder clamp(@This @NotNull StringBuilder stringBuilder, @NotNull String clamp) {
        return stringBuilder.insert(0, clamp).append(clamp);
    }
}
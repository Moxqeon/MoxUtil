package org.moxqeon.util;

import manifold.ext.rt.api.Extension;
import org.jetbrains.annotations.NotNull;

/**
 * 类名工具对象，提供类名格式转换能力。
 */
@Extension
public final class ClassUtil {
    private ClassUtil() {
        throw new IllegalStateException();
    }

    @NotNull
    public static String dot2Slash(@NotNull String className) {
        return className.replace(".", "/");
    }
}
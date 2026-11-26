package org.moxqeon.util;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Map 操作工具类，提供类型安全的取值、汇总等便捷方法。
 */
public final class MapUtil {
    private MapUtil() {
        throw new IllegalStateException();
    }

    @Nullable
    public static <T> T get(@This @NotNull Map<?, ?> map, @Nullable Object key, @NotNull Class<T> tClass) {
        Object value = map.get(key);
        return value != null ? tClass.cast(value) : null;
    }

    @NotNull
    public static <T> T getOrDefault(@This @NotNull Map<?, ?> map, @Nullable Object key, @NotNull Class<T> tClass, @NotNull T t) {
        Object value = map.get(key);
        return value != null ? tClass.cast(value) : t;
    }

    public static <T> List<T> getFullList(@This @NotNull Map<?, List<T>> map) {
        List<T> result = new ArrayList<>();
        for (List<T> list : map.values())
            result.addAll(list);
        return result;
    }

    public static int sum(@This @NotNull Map<?, Integer> map) {
        int result = 0;
        for (int i : map.values())
            result += i;
        return result;
    }
}
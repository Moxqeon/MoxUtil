package org.moxqeon.util;

import com.google.common.base.Converter;
import com.google.common.base.Function;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表（List）操作工具类，提供随机占位替换、统计、转换等便捷方法。
 */
@Extension
public final class ListUtil {
    private ListUtil() {
        throw new IllegalStateException();
    }

    @NotNull
    public static List<String> replaceRandomInt(@NotNull List<String> $this) {
        $this.replaceAll(StringUtil::replaceRandomInt);
        return $this;
    }

    public static boolean containsRandomInt(@NotNull List<String> $this) {
        for (String element : $this) {
            if (StringUtil.containsRandomInt(element))
                return true;
        }
        return false;
    }

    public static int count(@NotNull List<String> $this, @NotNull String regex) {
        int count = 0;
        for (String element : $this)
            count += StringUtil.count(element, regex);
        return count;
    }

    public static int maxLength(@NotNull List<String> $this) {
        if ($this.isEmpty())
            return -1;
        int max = 0;
        for (String str : $this)
            max = Math.max(max, str.length());
        return max;
    }

    public static <O, T> List<T> convert(@NotNull List<O> $this, @NotNull Function<O, T> converter) {
        List<T> result = new ArrayList<>();
        $this.forEach(o -> result.add(converter.apply(o)));
        return result;
    }

    public static <O, T> List<T> convert(@NotNull List<O> $this, @NotNull Converter<O, T> converter) {
        List<T> result = new ArrayList<>();
        $this.forEach(o -> result.add(converter.convert(o)));
        return result;
    }

    public static <T> T get(@NotNull List<?> $this, int index, @NotNull Class<T> tClass) {
        Object value = $this.get(index);
        return value != null ? tClass.cast(value) : null;
    }
}
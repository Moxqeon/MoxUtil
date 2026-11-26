package org.moxqeon.util;

import com.google.common.base.Converter;
import com.google.common.base.Function;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.Jailbreak;
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
    public static List<String> replaceRandomInt(@NotNull List<String> list) {
        list.replaceAll(StringUtil::replaceRandomInt);
        return list;
    }

    public static boolean containsRandomInt(@NotNull List<String> list) {
        for (String element : list) {
            if (StringUtil.containsRandomInt(element))
                return true;
        }
        return false;
    }

    public static int count(@NotNull List<String> list, @NotNull String regex) {
        int count = 0;
        for (String element : list)
            count += StringUtil.count(element, regex);
        return count;
    }

    public static int maxLength(@NotNull List<String> list) {
        if (list.isEmpty())
            return -1;
        int max = 0;
        for (String str : list)
            max = Math.max(max, str.length());
        return max;
    }

    public static <O, T> List<T> convert(@NotNull List<O> list, @NotNull Function<O, T> converter) {
        List<T> result = new ArrayList<>();
        list.forEach(o -> result.add(converter.apply(o)));
        return result;
    }

    public static <O, T> List<T> convert(@NotNull List<O> list, @NotNull Converter<O, T> converter) {
        List<T> result = new ArrayList<>();
        list.forEach(o7 -> result.add(converter.convert(o)));
        return result;
    }

    public static <T> T get(@NotNull List<?> list, int index, @NotNull Class<T> tClass) {
        Object value = list.get(index);
        return value != null ? tClass.cast(value) : null;
    }
}
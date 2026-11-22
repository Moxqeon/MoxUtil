package org.moxqeon.api.context;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 上下文接口，提供按字段名取值的能力，通常由 {@code ContextGenerator} 动态生成实现。
 */
public interface Context {
    default <T> T get(@NotNull String fieldName, @NotNull Class<T> tClass) {
        return tClass.cast(get(fieldName));
    }

    Object get(@NotNull String fieldName);

    Context EMPTY = fieldName -> {
        throw new RuntimeException();
    };


    static <T extends Context> T newContainer(@NotNull Class<T> tClass, @NotNull LinkedHashMap<String, Class<?>> fieldMap, Object... args) {
        Class<?>[] paramClasses = new Class[fieldMap.size()];
        int i = 0;
        for (Map.Entry<String, Class<?>> entry : fieldMap.entrySet()) {
            paramClasses[i] = entry.getValue();
            i++;
        }
        try {
            Constructor<T> constructor = tClass.getConstructor(paramClasses);
            return constructor.newInstance(args);
        } catch (Exception e) {
            return null;
        }
    }
}
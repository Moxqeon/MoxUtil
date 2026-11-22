package org.moxqeon.api;

import org.moxqeon.module.reflect.Reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 字符串序列化接口，提供序列化与反序列化能力。
 */
public interface StringSerializable {
    Map<Class<?>, Method> METHOD_CACHE = new HashMap<>();

    String serialize();

    static <T> T deserialize(String s, Class<T> tClass) {
        try {
            Method deserialize;
            if (METHOD_CACHE.containsKey(tClass)) deserialize = METHOD_CACHE.get(tClass);
            else deserialize = tClass.getDeclaredMethod("deserialize", String.class);
            return Reflect.cast(deserialize.invoke(null, s), tClass);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static void add(Class<?> clazz) {
        try {
            METHOD_CACHE.put(clazz, Reflect.getMethodExact(clazz, "deserialize", String.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
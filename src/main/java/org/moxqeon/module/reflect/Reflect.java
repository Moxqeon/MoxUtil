package org.moxqeon.module.reflect;

import com.google.common.base.Preconditions;
import manifold.util.ReflectUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moxqeon.MoxUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 反射工具类，封装字段与方法的访问、实例构造以及类型转换等操作。
 * @see manifold.ext.rt.api.Jailbreak
 */
@Deprecated
public final class Reflect {
    private Reflect() {
        throw new IllegalStateException();
    }

    public static <T> T construct(@NotNull Class<T> tClass, @NotNull Class<?>[] paramClasses, @NotNull Object... params) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Preconditions.checkArgument((paramClasses.length == params.length), "Params must be in line with the given classes");
        return tClass.getConstructor(paramClasses).newInstance(params);
    }

    public static Map<String, Field> getNonStaticFields(@NotNull Class<?> clazz) {
        Map<String, Field> result = new LinkedHashMap<>();
        for (Field field : clazz.getFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            result.put(field.getName(), field);
        }
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            if (result.containsKey(fieldName) || Modifier.isStatic(field.getModifiers())) continue;
            result.put(fieldName, field);
        }
        if (clazz.getSuperclass() != null) result.putAll(getNonStaticFields(clazz.getSuperclass()));
        return result;
    }

    @NotNull
    public static Field getFieldExact(@NotNull Class<?> clazz, @NotNull String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getField(fieldName);
        } catch (ReflectiveOperationException ignored) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (ReflectiveOperationException ignored1) {
                if (clazz.getSuperclass() == null) throw new NoSuchFieldException(fieldName);
                return getFieldExact(clazz.getSuperclass(), fieldName);
            }
        }
    }

    @NotNull
    public static Method getMethodExact(@NotNull Class<?> clazz, @NotNull String methodName, Class<?>... paramTypes) throws NoSuchMethodException {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (ReflectiveOperationException ignored) {
            try {
                return clazz.getDeclaredMethod(methodName, paramTypes);
            } catch (ReflectiveOperationException ignored1) {
                throw new NoSuchMethodException(methodName);
            }
        }
    }

    @Nullable
    public static <T> T getFieldValue(@NotNull Object obj, @NotNull String fieldName) throws NoSuchFieldException {
        Field field = getFieldExact(obj.getClass(), fieldName);
        return getFieldValue(obj, field);
    }

    @Nullable
    public static <T> T getFieldValue(@NotNull Object obj, @NotNull Field field) {
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            T result = cast(field.get(obj));
            field.setAccessible(accessible);
            return result;
        } catch (IllegalAccessException ignored) {
            MoxUtil.warn(String.format("Failed to access field \"%s\" of %s", field.getName(), obj.getClass()));
            field.setAccessible(accessible);
            return null;
        }
    }

    public static void setFieldValue(@NotNull Object obj, @NotNull String fieldName, @Nullable Object value) throws NoSuchFieldException {
        Field field = getFieldExact(obj.getClass(), fieldName);
        setFieldValue(obj, field, value);
    }

    public static void setFieldValue(@NotNull Object obj, @NotNull Field field, @Nullable Object value) {
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException ignored) {
            MoxUtil.warn(String.format("Failed to access field \"%s\" of %s", field.getName(), obj.getClass()));
        }
        field.setAccessible(accessible);
    }

    public static <T> T invokeMethod(@NotNull Object obj, @NotNull Method method, @NotNull Object... params) {
        boolean accessible = method.canAccess(obj);
        method.setAccessible(true);
        try {
            T result = cast(method.invoke(obj, params));
            method.setAccessible(accessible);
            return result;
        } catch (IllegalAccessException e) {
            MoxUtil.warn(String.format("Failed to access method \"%s\" of %s", method, obj.getClass()));
            method.setAccessible(accessible);
            return null;
        } catch (InvocationTargetException e) {
            MoxUtil.warn(String.format("Failed to invoke method \"%s\" of %s", method, obj.getClass()));
            method.setAccessible(accessible);
            return null;
        }
    }

    public static <T> T invokeMethod(@NotNull Object obj, @NotNull String methodName, @NotNull Class<?>[] paramTypes, @NotNull Object... params) throws NoSuchMethodException {
        Preconditions.checkArgument((paramTypes.length == params.length), "Mismatched amount of params");
        Method method = getMethodExact(obj.getClass(), methodName, paramTypes);
        return invokeMethod(obj, method, params);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T cast(@Nullable Object obj) {
        return obj != null ? (T) obj : null;
    }

    public static <T> T cast(@Nullable Object obj, @NotNull Class<T> tClass) {
        return obj != null ? tClass.cast(obj) : null;
    }
}
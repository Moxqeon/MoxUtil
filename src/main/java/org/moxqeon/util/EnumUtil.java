package org.moxqeon.util;

import manifold.ext.rt.api.Extension;
import org.moxqeon.module.reflect.Reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 枚举工具类，提供枚举全部名称的反射获取能力。
 */
@Extension
public final class EnumUtil {
    private EnumUtil() {
        throw new IllegalStateException();
    }

    public static String[] allNames(Class<?> enumClass) {
        try {
            Method valuesMethod = Reflect.getMethodExact(enumClass, "values");
            Object[] enums = Reflect.cast(valuesMethod.invoke(null));
            int length = enums.length;
            String[] array = new String[length];
            int i = 0;
            for (Object anEnum : enums) {
                array[i] = Reflect.invokeMethod(anEnum, enumClass.isAssignableFrom(Enum.class) ? "name" : "displayName", new Class[0]);
                i++;
            }
            return array;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
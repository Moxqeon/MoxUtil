package org.moxqeon.module.script;

import jdk.dynalink.beans.StaticClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JavaScript 引擎辅助工具类，提供列表/数组构造、类查找与静态类包装等便捷方法。
 */
public final class JavaScriptTool {
    private JavaScriptTool() {
        throw new IllegalStateException();
    }

    @NotNull
    public static List<?> listOf(@NotNull Object... objects) {
        return new ArrayList<>(Arrays.asList(objects));
    }

    @NotNull
    public static String[] stringArray(@NotNull String... strings) {
        return strings;
    }

    public static int[] intArray(int... integers) {
        return integers;
    }

    public static long[] longArray(long... longs) {
        return longs;
    }

    public static double[] doubleArray(double... doubles) {
        return doubles;
    }

    public static float[] floatArray(float... floats) {
        return floats;
    }

    public static char[] charArray(char... chars) {
        return chars;
    }

    public static Class<?>[] classArray(Class<?>... classes) {
        return classes;
    }

    @NotNull
    public static Class<?> findClass(@NotNull String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    public static StaticClass staticClass(@NotNull Class<?> clazz) {
        return StaticClass.forClass(clazz);
    }

    public static int toInt(Number value) {
        return value.intValue();
    }




}
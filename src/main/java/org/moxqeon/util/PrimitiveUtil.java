package org.moxqeon.util;

import manifold.ext.rt.api.Extension;
import org.jetbrains.annotations.NotNull;

/**
 * 基本类型数组与对应包装类型数组之间相互转换的工具类。
 */
@Extension
public final class PrimitiveUtil {
    private PrimitiveUtil() {
        throw new IllegalStateException();
    }

    public static Byte[] copy(byte[] bytes) {
        Byte[] copy = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            copy[i] = bytes[i];
        return copy;
    }

    public static byte[] copy(@NotNull Byte[] bytes) {
        byte[] copy = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            copy[i] = bytes[i];
        return copy;
    }

    public static Integer[] copy(int[] integers) {
        Integer[] copy = new Integer[integers.length];
        for (int i = 0; i < integers.length; i++)
            copy[i] = integers[i];
        return copy;
    }

    public static int[] copy(@NotNull Integer[] integers) {
        int[] copy = new int[integers.length];
        for (int i = 0; i < integers.length; i++)
            copy[i] = integers[i];
        return copy;
    }

    public static Long[] copy(long[] longs) {
        Long[] copy = new Long[longs.length];
        for (int i = 0; i < longs.length; i++)
            copy[i] = longs[i];
        return copy;
    }

    public static long[] copy(@NotNull Long[] longs) {
        long[] copy = new long[longs.length];
        for (int i = 0; i < longs.length; i++)
            copy[i] = longs[i];
        return copy;
    }

    public static Double[] copy(double[] doubles) {
        Double[] copy = new Double[doubles.length];
        for (int i = 0; i < doubles.length; i++)
            copy[i] = doubles[i];
        return copy;
    }

    public static double[] copy(@NotNull Double[] doubles) {
        double[] copy = new double[doubles.length];
        for (int i = 0; i < doubles.length; i++)
            copy[i] = doubles[i];
        return copy;
    }

    public static Float[] copy(float[] floats) {
        Float[] copy = new Float[floats.length];
        for (int i = 0; i < floats.length; i++)
            copy[i] = floats[i];
        return copy;
    }

    public static float[] copy(@NotNull Float[] floats) {
        float[] copy = new float[floats.length];
        for (int i = 0; i < floats.length; i++)
            copy[i] = floats[i];
        return copy;
    }

    public static Character[] copy(char[] chars) {
        Character[] copy = new Character[chars.length];
        for (int i = 0; i < chars.length; i++)
            copy[i] = chars[i];
        return copy;
    }

    public static char[] copy(@NotNull Character[] chars) {
        char[] copy = new char[chars.length];
        for (int i = 0; i < chars.length; i++)
            copy[i] = chars[i];
        return copy;
    }
}
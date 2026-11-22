package org.moxqeon.util;

import manifold.ext.rt.api.Extension;

/**
 * 数据工具对象，提供字节数组反转等基础数据处理能力。
 */
@Extension
public final class DataUtil {
    private DataUtil() {
        throw new IllegalStateException();
    }

    public static byte[] reverseBytes(byte[] bytes) {
        byte[] reversed = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            reversed[i] = bytes[bytes.length - 1 - i];
        return reversed;
    }
}
package org.moxqeon.util;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 输入流工具对象，提供按缓冲区读取输入流的方法。
 */
@Extension
public final class StreamUtil {
    private StreamUtil() {
        throw new IllegalStateException();
    }

    public static byte[] readAllBytes(@This @NotNull InputStream $this, int bufferSize) {
        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        int len;
        try {
            while ((len = $this.read(buffer)) != -1)
                byteArrayOutput.write(buffer, 0, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutput.toByteArray();
    }
}
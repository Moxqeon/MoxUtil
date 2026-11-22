package org.moxqeon.api.asm;

import org.jetbrains.annotations.NotNull;

/**
 * 可根据字节码动态定义类的类加载器。
 */
public final class ASMClassLoader extends ClassLoader {

    public Class<?> define(@NotNull String className, byte[] bytes) {
        return defineClass(className, bytes, 0, bytes.length);
    }
}
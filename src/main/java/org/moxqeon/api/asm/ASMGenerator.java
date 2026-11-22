package org.moxqeon.api.asm;

import org.jetbrains.annotations.NotNull;

/**
 * ASM 字节码类生成器的抽象基类。
 *
 * @param <T> 生成类所实现的父类型
 */
public abstract class ASMGenerator<T> {
    @NotNull
    public abstract Class<? extends T> generate();
}
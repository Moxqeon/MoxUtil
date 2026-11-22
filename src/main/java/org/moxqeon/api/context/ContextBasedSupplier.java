package org.moxqeon.api.context;

import org.jetbrains.annotations.NotNull;

/**
 * 基于上下文的供应器接口，根据给定的上下文产出结果。
 *
 * @param <T> 产出结果的类型
 */
public interface ContextBasedSupplier<T> {
    @NotNull
    T get(@NotNull Context context);
}
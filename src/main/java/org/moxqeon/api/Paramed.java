package org.moxqeon.api;

import org.jetbrains.annotations.NotNull;

/**
 * 持有可变参数数组的抽象基类，提供按索引设置参数的能力。
 */
public abstract class Paramed {
    @NotNull
    protected Object[] params;

    @NotNull
    public Object set(int index, @NotNull Object obj) {
        if (index >= params.length) {
            Object[] newParams = new Object[index + 1];
            System.arraycopy(params, 0, newParams, 0, params.length);
            params = newParams;
        }
        return params[index] = obj;
    }

}
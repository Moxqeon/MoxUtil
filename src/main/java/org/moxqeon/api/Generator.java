package org.moxqeon.api;

/**
 * 通用生成器接口，用于以统一的方式产出指定类型的结果。
 *
 * @param <T> 生成结果的类型
 */
public interface Generator<T> {
    T generate();
}
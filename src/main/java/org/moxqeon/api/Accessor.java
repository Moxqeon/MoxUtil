package org.moxqeon.api;

/**
 * 访问器接口，暴露单一可访问属性。
 *
 * @param <T> 被访问属性的类型
 */
public interface Accessor<T> {
    T getAccess();
}
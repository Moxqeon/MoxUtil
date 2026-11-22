package org.moxqeon.api;

import com.google.common.base.Converter;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.moxqeon.util.JsonUtil;

/**
 * 通过 JSON 序列化在两种类型之间进行双向转换的转换器。
 *
 * @param <O> 原始类型
 * @param <T> 目标类型
 */
public final class JsonConverter<O, T> extends Converter<O, T> {
    private final Class<O> objectClass;
    private final Class<T> targetClass;

    public JsonConverter(@NotNull Class<O> objectClass, @NotNull Class<T> targetClass) {
        this.objectClass = objectClass;
        this.targetClass = targetClass;
    }

    @Override
    @NotNull
    public T doForward(@NotNull O o) {
        Gson gson = JsonUtil.GSON;
        return gson.fromJson(gson.toJson(o), targetClass);
    }

    @Override
    @NotNull
    public O doBackward(@NotNull T t) {
        Gson gson = JsonUtil.GSON;
        return gson.fromJson(gson.toJson(t), objectClass);
    }
}
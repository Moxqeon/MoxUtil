package org.moxqeon.api.condition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * 条件接口，用于异步判定某个条件是否成立。
 */
public interface Condition {
    CompletableFuture<Boolean> check(@Nullable ExecutorService executor, @NotNull Map<String, Double> variables);
}
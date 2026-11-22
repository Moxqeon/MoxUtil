package org.moxqeon.api.condition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moxqeon.util.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * 组合条件，按 AND 或 OR 逻辑组合多个子条件。
 */
public class Criteria implements Condition {
    private final List<Condition> criteria = new ArrayList<>();
    private final Logic logic;

    public Criteria(@NotNull Logic logic, @NotNull List<?> criteria, @NotNull String... variables) {
        this.logic = logic;
        for (Object obj : criteria) {
            if (obj instanceof Map) this.criteria.add(new Criteria((Map<?, ?>) obj, variables));
            else if (obj instanceof String) this.criteria.add(new Criterion((String) obj, variables));
            else if (obj instanceof Condition) this.criteria.add((Condition) obj);
        }
    }

    public Criteria(@NotNull Map<?, ?> map, @NotNull String... variables) {
        this.logic = Logic.valueOf(MapUtil.get(map, "Logic", String.class));
        List<?> criteria = MapUtil.get(map, "Criteria", List.class);
        for (Object obj : criteria) {
            if (obj instanceof Map) this.criteria.add(new Criteria((Map<?, ?>) obj, variables));
            else if (obj instanceof String) this.criteria.add(new Criterion((String) obj, variables));
        }
    }

    @Override
    public CompletableFuture<Boolean> check(@Nullable ExecutorService executor, @NotNull Map<String, Double> variables) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        ArrayList<CompletableFuture<Boolean>> futures = new ArrayList<>();
        for (Condition condition : criteria)
            futures.add(condition.check(executor, variables));
        for (CompletableFuture<Boolean> future : futures) {
            try {
                if (logic == Logic.AND && !future.get()) {
                    result.complete(false);
                    return result;
                } else if (logic == Logic.OR && future.get()) {
                    result.complete(true);
                    return result;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        result.complete(logic == Logic.AND);
        return result;
    }
}
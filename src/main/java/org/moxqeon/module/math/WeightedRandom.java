package org.moxqeon.module.math;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 带权重的随机选择器，根据各条目的累计权重随机返回一个对象。
 *
 * @param <O> 随机选择的对象类型
 */
public class WeightedRandom<O> extends SimpleSeeded implements Supplier<O> {
    protected List<WeightEntry<O>> weightList = new ArrayList<>();

    public WeightedRandom() {
        super();
    }

    public WeightedRandom(long seed) {
        super(seed);
    }

    public void addWeight(@NotNull Integer weight, @NotNull O obj) {
        Preconditions.checkArgument(weight > 0, "Weight must be positive");
        if (weightList.isEmpty()) {
            weightList.add(new WeightEntry<>(obj, weight));
        } else {
            weightList.add(new WeightEntry<>(obj, weight + totalWeight()));
        }
    }

    @Override
    @NotNull
    public O get() {
        int value = new Random(getSeed()).nextInt(totalWeight()) + 1;
        return getExact(value);
    }

    public O getExact(int weight) {
        for (WeightEntry<O> entry : weightList)
            if (entry.getValue() >= weight)
                return entry.getKey();
        throw new IllegalStateException("Not Expected");
    }

    public int totalWeight() {
        return weightList.get(weightList.size() - 1).getValue();
    }
}
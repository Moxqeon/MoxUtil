package org.moxqeon.api.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moxqeon.util.MathUtil;

import java.util.Random;

/**
 * 双精度浮点占位符，按区间随机生成浮点数。
 */
public final class PlaceholderDouble implements Placeholder<Double> {
    private final double min;
    private final double max;
    private final Double exp;

    PlaceholderDouble(@NotNull String placeholder) {
        String[] tmp = placeholder.split(";");
        min = Double.parseDouble(tmp[1]);
        max = Double.parseDouble(tmp[2]);
        exp = tmp.length > 3 ? Double.parseDouble(tmp[3]) : null;
    }

    @Override
    @NotNull
    public Double get(@Nullable Object... objects) {
        Random random = new Random(System.currentTimeMillis());
        return exp != null ? MathUtil.nextDouble(random, min, max, exp) : MathUtil.nextDouble(random, min, max);
    }
}
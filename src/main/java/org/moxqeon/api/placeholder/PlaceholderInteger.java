package org.moxqeon.api.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moxqeon.util.MathUtil;

import java.util.Random;

/**
 * 整数占位符，按区间随机生成整数。
 */
public final class PlaceholderInteger implements Placeholder<Integer> {
    private final int min;
    private final int max;
    private final Integer exp;

    PlaceholderInteger(@NotNull String placeholder) {
        String[] tmp = placeholder.split(";");
        min = Integer.parseInt(tmp[1]);
        max = Integer.parseInt(tmp[2]);
        exp = tmp.length > 3 ? Integer.parseInt(tmp[3]) : null;
    }

    @Override
    @NotNull
    public Integer get(@Nullable Object... objects) {
        Random random = new Random(System.currentTimeMillis());
        return exp != null ? MathUtil.nextInt(random, min, max, exp) : MathUtil.nextInt(random, min, max);
    }
}
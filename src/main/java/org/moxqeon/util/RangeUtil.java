package org.moxqeon.util;

import com.google.common.collect.Range;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * 区间（Range）随机取值工具类。
 */
public final class RangeUtil {
    private RangeUtil() {
        throw new IllegalStateException();
    }

    public static int randomInt(@NotNull Range<Integer> $this) {
        final int min = $this.lowerEndpoint();
        final int max = $this.upperEndpoint();
        return MathUtil.nextInt(new Random(), min, max);
    }

    public static int randomInt(@NotNull Range<Integer> $this, int expect) {
        final int min = $this.lowerEndpoint();
        final int max = $this.upperEndpoint();
        return MathUtil.nextInt(new Random(), min, max, expect);
    }

    public static float randomFloat(@NotNull Range<Float> $this) {
        final float min = $this.lowerEndpoint();
        final float max = $this.upperEndpoint();
        return MathUtil.nextFloat(new Random(), min, max);
    }

    public static float randomFloat(@NotNull Range<Float> $this, float expect) {
        final float min = $this.lowerEndpoint();
        final float max = $this.upperEndpoint();
        return MathUtil.nextFloat(new Random(), min, max, expect);
    }

    public static double randomDouble(@NotNull Range<Double> $this) {
        final double min = $this.lowerEndpoint();
        final double max = $this.upperEndpoint();
        return MathUtil.nextDouble(new Random(), min, max);
    }

    public static double randomDouble(@NotNull Range<Double> $this, double expect) {
        final double min = $this.lowerEndpoint();
        final double max = $this.upperEndpoint();
        return MathUtil.nextDouble(new Random(), min, max, expect);
    }
}
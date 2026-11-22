package org.moxqeon.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * 数学计算工具类，提供范围判断、随机数、取整、概率等计算方法。
 */


@Extension
public final class MathUtil {
    private MathUtil() {
        throw new IllegalStateException();
    }

    public static <N extends Number> boolean isInRange(@This @NotNull N $this, @NotNull N min, @NotNull N max) {
        return Range.closed(min.doubleValue(), max.doubleValue()).contains($this.doubleValue());
    }

    public static int nextInt(@NotNull Random $this, int min, int max) {
        return $this.nextInt(max - min + 1) + min;
    }

    public static int nextInt(@NotNull Random $this, int min, int max, int expect) {
        Preconditions.checkArgument(isInRange(expect, min, max), "Invalid expectation in range");
        if (chance((float) (max - expect) / (max - min)))
            return $this.nextInt(expect - min + 1) + min;
        else
            return $this.nextInt(max - expect + 1) + expect;
    }

    public static float nextFloat(@NotNull Random $this, float min, float max) {
        return $this.nextFloat() * (max - min) + min;
    }

    public static float nextFloat(@NotNull Random $this, float min, float max, float expect) {
        Preconditions.checkArgument(isInRange(expect, min, max), "Invalid expectation in range");
        if (chance((max - expect) / (max - min)))
            return $this.nextFloat() * (expect - min) + min;
        else
            return $this.nextFloat() * (max - expect) + expect;
    }

    public static double nextDouble(@NotNull Random $this, double min, double max) {
        return $this.nextDouble() * (max - min) + min;
    }

    public static double nextDouble(@NotNull Random $this, double min, double max, double expect) {
        Preconditions.checkArgument(isInRange(expect, min, max), "Invalid expectation in range");
        if (chance((max - expect) / (max - min)))
            return $this.nextDouble() * (expect - min) + min;
        else
            return $this.nextDouble() * (max - expect) + expect;
    }

    public static double sinWithDegree(double sin) {
        return Math.sin(sin / 180.0D * Math.PI);
    }

    public static double cosWithDegree(double cos) {
        return Math.cos(cos / 180.0D * Math.PI);
    }

    public static double tanWithDegree(double tan) {
        return Math.tan(tan / 180.0D * Math.PI);
    }

    public static boolean chance(@This @NotNull Float $this) {
        return (Math.random() < $this);
    }

    public static boolean chance(@NotNull Double $this) {
        return (Math.random() < $this);
    }

    public static boolean chance(int chance, int total) {
        return new Random().nextInt(total) < chance;
    }

    public static int floor(@This @NotNull Float $this) {
        float f = $this;
        int i = (int) f;
        return (f < i) ? (i - 1) : i;
    }

    public static int ceil(@This @NotNull Float $this) {
        float f = $this;
        int i = (int) f;
        return (i < f) ? (i + 1) : i;
    }

    public static int floor(@NotNull Double $this) {
        double d = $this;
        int i = (int) d;
        return (d < i) ? (i - 1) : i;
    }

    public static int ceil(@NotNull Double $this) {
        double d = $this;
        int i = (int) d;
        return (i < d) ? (i + 1) : i;
    }

    public static int clamp(int i, int i1, int i2) {
        Preconditions.checkArgument(i1 < i2);
        if (i < i1)
            return i1;
        else
            return Math.min(i, i2);
    }
}
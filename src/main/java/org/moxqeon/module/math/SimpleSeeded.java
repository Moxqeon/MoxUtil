package org.moxqeon.module.math;

import java.util.UUID;

/**
 * 带种子的随机源抽象基类，默认以随机值作为种子。
 */
public abstract class SimpleSeeded implements Seeded {
    protected long seed;

    protected SimpleSeeded() {
        this(UUID.randomUUID().getMostSignificantBits());
    }

    protected SimpleSeeded(long seed) {
        this.seed = seed;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }
}
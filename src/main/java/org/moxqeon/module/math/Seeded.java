package org.moxqeon.module.math;

/**
 * 种子接口，提供可读写种子的能力。
 */
public interface Seeded {
    long getSeed();

    void setSeed(long seed);
}
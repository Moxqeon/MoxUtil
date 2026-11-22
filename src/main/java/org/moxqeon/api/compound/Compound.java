package org.moxqeon.api.compound;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * 可拆分的字符串列表，既能作为列表使用，也能按分隔符序列化。
 */
public class Compound implements Iterable<String> {
    public static final String DEFAULT_SPLIT = "!";
    private String split = DEFAULT_SPLIT;

    protected final List<String> list;

    public Compound(@NotNull List<String> list) {
        this.list = list;
    }

    public Compound(@NotNull String... list) {
        this(new ArrayList<>(Arrays.asList(list)));
    }

    public Compound(@NotNull String compound, @NotNull String split) {
        this(compound.split(split));
    }

    public Compound(@NotNull String compound) {
        this(compound, DEFAULT_SPLIT);
    }

    @Override public String toString() {
        StringJoiner joiner = new StringJoiner(split);
        for (String string : list) joiner.add(string);
        return joiner.toString();
    }

    @Override public int hashCode() {
        return list.hashCode();
    }

    @Override public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Compound compound = (Compound) obj;
        if (!list.equals(compound.list)) return false;
        return split.equals(compound.split);
    }

    @Override public @NotNull Iterator<String> iterator() {
        return list.iterator();
    }

    public @NotNull String get(int index) {
        return list.get(index);
    }

    public void set(int index, @NotNull String value) {
        if (value.contains(split)) throw new IllegalArgumentException("Value must not contain the split character");
        list.set(index, value);
    }
}
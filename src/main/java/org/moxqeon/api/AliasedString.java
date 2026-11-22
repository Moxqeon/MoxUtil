package org.moxqeon.api;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.StringJoiner;

/**
 * 携带多个别名、支持按别名匹配的可序列化字符串。
 */
public final class AliasedString implements StringSerializable {
    public static AliasedString deserialize(@NotNull String serial) {
        return new AliasedString(Set.of(serial.split("\n")));
    }

    private final Set<String> aliases = Sets.newConcurrentHashSet();

    /**
     * 使用一组别名构造实例。
     *
     * @param aliases 别名集合
     */
    public AliasedString(@NotNull Collection<String> aliases) {
        this.aliases.addAll(aliases);
    }

    public boolean matches(@NotNull String str) {
        return aliases.contains(str);
    }

    @Override
    public int hashCode() {
        return aliases.hashCode();
    }

    @Override
    @NotNull
    public String serialize() {
        StringJoiner joiner = new StringJoiner("\n");
        for (String alias : aliases)
            joiner.add(alias);
        return joiner.toString();
    }
}
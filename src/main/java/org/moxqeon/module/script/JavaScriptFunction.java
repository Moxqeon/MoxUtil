package org.moxqeon.module.script;

import com.google.common.base.Preconditions;
import org.graalvm.polyglot.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * JavaScript 函数封装，便于调用脚本中的函数。
 */
public final class JavaScriptFunction {
    private final Value function;

    public JavaScriptFunction(@NotNull Value function) {
        Preconditions.checkArgument(function.canExecute(), "The object must be a function");
        this.function = function;
    }

    @Nullable
    public <T> T invoke(@Nullable Object... objects) {
        Value result = this.function.execute(objects);
        return result.asHostObject();
    }

    @Override
    public int hashCode() {
        return this.function.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        JavaScriptFunction that = (JavaScriptFunction) other;
        return function.equals(that.function);
    }
}
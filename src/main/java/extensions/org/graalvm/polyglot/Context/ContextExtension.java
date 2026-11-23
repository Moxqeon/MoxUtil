package extensions.org.graalvm.polyglot.Context;

import jdk.dynalink.beans.StaticClass;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.graalvm.polyglot.Context;
import org.jetbrains.annotations.NotNull;

@Extension
public final class ContextExtension {
    private ContextExtension() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static void importObject(@This @NotNull Context context, @NotNull String varName, @NotNull Object value) {
        context.getBindings("js").putMember(varName, value);
    }

    public static void importClass(@This @NotNull Context context, @NotNull Class<?> clazz) {
        importClass(context, clazz.getSimpleName(), clazz);
    }

    public static void importClass(@This @NotNull Context context, @NotNull String className, @NotNull Class<?> clazz) {
        importObject(context, className, StaticClass.forClass(clazz));
    }

    public static void importClass(@This @NotNull Context context, @NotNull String varName, @NotNull String className) throws ClassNotFoundException {
        importClass(context, varName, Class.forName(className));
    }
}

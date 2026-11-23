package extensions.java.lang.Object;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moxqeon.module.reflect.Reflect;

import java.lang.reflect.Field;

@Extension
public final class ObjectExtension {
    private ObjectExtension() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static <T> T getFieldValue(@This @NotNull Object $this, @NotNull String fieldName) throws NoSuchFieldException {
        return Reflect.getFieldValue($this, fieldName);
    }

    public static void setFieldValue(@This @NotNull Object $this, @NotNull String fieldName, @Nullable Object value) throws NoSuchFieldException {
        Reflect.setFieldValue($this, fieldName, value);
    }

    public static <T> T cast(@This @NotNull Object $this){
        return Reflect.cast($this);
    }

    public static <T> T cast(@This @NotNull Object $this, Class<T> tClass) {
        return Reflect.cast($this, tClass);
    }
}

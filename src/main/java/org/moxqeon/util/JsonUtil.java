package org.moxqeon.util;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * JSON 工具类，封装 Gson 实例、JSON 解析、合并与美化输出等功能。
 */
public final class JsonUtil {
    private JsonUtil() {
        throw new IllegalStateException();
    }

    private static final Gson PRETTY_PRINTER = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().setLenient().create();
    public static final Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().setLenient().create();
    @SuppressWarnings("deprecation")
    public static final JsonParser PARSER = new JsonParser();
    private static Boolean newVersionGson = null;

    @NotNull
    public static String toPrettyString(@NotNull JsonObject $this) {
        return PRETTY_PRINTER.toJson($this);
    }

    @NotNull
    public static JsonObject merge(@NotNull JsonObject $this, @NotNull JsonObject prior) {
        for (Map.Entry<String, JsonElement> entry : prior.entrySet()) {
            if (entry.getValue().isJsonObject() && $this.get(entry.getKey()).isJsonObject())
                $this.add(entry.getKey(), merge($this.getAsJsonObject(entry.getKey()), entry.getValue().getAsJsonObject()));
            else if (entry.getValue().isJsonArray() && $this.get(entry.getKey()).isJsonArray())
                $this.getAsJsonArray(entry.getKey()).addAll(entry.getValue().getAsJsonArray());
            else
                $this.add(entry.getKey(), entry.getValue());
        }
        return $this;
    }

    @NotNull
    public static JsonObject mergeWithOperation(@NotNull JsonObject $this, @NotNull JsonObject other, @NotNull BiConsumer<JsonObject, JsonObject> consumer) {
        consumer.accept($this, other);
        return $this;
    }

    @NotNull
    public static Set<String> keys(@NotNull JsonObject $this) {
        if (newVersionGson == null)
            newVersionGson = newVersionGson();
        if (newVersionGson)
            return $this.keySet();
        else {
            Set<Map.Entry<String, JsonElement>> entrySet = $this.entrySet();
            String[] keys = new String[entrySet.size()];
            int i = 0;
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                keys[i] = entry.getKey();
                i++;
            }
            return Set.of(keys);
        }
    }

    @SuppressWarnings("deprecation")
    public static JsonElement parse(@NotNull String json) {
        if (newVersionGson == null)
            newVersionGson = newVersionGson();
        if (newVersionGson)
            return JsonParser.parseString(json);
        else
            return PARSER.parse(json);
    }

    private static boolean newVersionGson() {
        try {
            Class.forName("com.google.gson.internal.GsonBuildConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
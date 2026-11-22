package org.moxqeon.util;

import manifold.ext.rt.api.Extension;
import org.jetbrains.annotations.NotNull;
import org.moxqeon.api.SQLSynthetic;
import org.moxqeon.api.StringSerializable;
import org.moxqeon.module.reflect.Reflect;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * SQL 语句生成工具类，根据类结构生成建表、插入、查询等 SQL 语句。
 */
@Extension
public final class SQLUtil {
    private SQLUtil() {
        throw new IllegalStateException();
    }

    @NotNull
    public static String sqliteCreateTable(@NotNull Class<?> clazz, @NotNull String primaryKey) {
        StringBuilder fields = new StringBuilder();
        for (Map.Entry<String, Field> fieldEntry : Reflect.getNonStaticFields(clazz).entrySet()) {
            if (fieldEntry.getValue().isAnnotationPresent(SQLSynthetic.class)) continue;
            String fieldName = fieldEntry.getKey();
            String fieldType = fieldEntry.getValue().getType().getSimpleName();
            String type;
            switch (fieldType) {
                case "String":
                    type = "VARCHAR(256)";
                    break;
                case "Integer":
                case "int":
                case "Long":
                case "long":
                    type = "INTEGER";
                    break;
                case "Double":
                case "double":
                case "Float":
                case "float":
                    type = "REAL";
                    break;
                case "Boolean":
                case "boolean":
                    type = "BOOLEAN";
                    break;
                default:
                    throw new RuntimeException("Invalid field type");
            }
            if (primaryKey.equals(fieldName)) {
                fields.append(StringUtil.camelToSnake(fieldName)).append(' ').append(type).append(" PRIMARY KEY, ");
            } else {
                fields.append(StringUtil.camelToSnake(fieldName)).append(' ').append(type).append(", ");
            }
        }
        return "CREATE TABLE IF NOT EXISTS " + StringUtil.camelToSnake(clazz.getSimpleName()) +
                "(" + trimLast(fields, 2) + ")";
    }

    @NotNull
    public static String sqliteInsert(@NotNull Object any) {
        Class<?> tClass = any.getClass();
        StringBuilder fields = new StringBuilder();
        for (Field field : Reflect.getNonStaticFields(tClass).values()) {
            if (field.isAnnotationPresent(SQLSynthetic.class)) continue;
            if ("String".equals(field.getType().getSimpleName())) {
                fields.append('"').append(Reflect.<Object>getFieldValue(any, field)).append("\", ");
            }
        }
        return "INSERT OR REPLACE INTO " + StringUtil.camelToSnake(tClass.getSimpleName()) +
                " VALUES(" + trimLast(fields, 2) + ")";
    }

    @NotNull
    public static String sqliteQuery(@NotNull Class<?> clazz) {
        return "SELECT * FROM " + StringUtil.camelToSnake(clazz.getSimpleName());
    }

    @NotNull
    public static <K extends StringSerializable, V extends StringSerializable> String sqliteCreateMap(
            @NotNull String name,
            @NotNull Map.Entry<Class<K>, Class<V>> pair,
            @NotNull Map.Entry<Boolean, Boolean> notNull) {
        String[] notNullSign = new String[]{
                notNull.getKey() ? "NOT NULL" : "",
                notNull.getValue() ? "NOT NULL" : ""
        };
        return "CREATE TABLE IF NOT EXISTS " + name + "(" +
                StringUtil.camelToSnake(pair.getKey().getSimpleName()) + " TEXT " + notNullSign[0] + ", " +
                StringUtil.camelToSnake(pair.getValue().getSimpleName()) + " TEXT " + notNullSign[1] + ")";
    }

    private static String trimLast(@NotNull StringBuilder builder, int count) {
        return builder.substring(0, Math.max(0, builder.length() - count));
    }
}
package org.moxqeon.api.condition;

import org.jetbrains.annotations.NotNull;

/**
 * 比较判断枚举，表示大于、等于、小于三种关系。
 */
public enum Judgement {
    MORE, EQUAL, LESS;

    public static Judgement fromOperator(char operator) {
        switch (operator) {
            case '<':
                return LESS;
            case '=':
                return EQUAL;
            case '>':
                return MORE;
            default:
                throw new IllegalArgumentException("Unrecognizable operator: " + operator);
        }
    }
}
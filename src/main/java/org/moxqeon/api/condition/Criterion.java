package org.moxqeon.api.condition;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * 通过比较两个数学表达式结果来实现的条件判定，支持小于、等于、大于三种比较关系。
 */
public final class Criterion implements Condition {
    private final ExpressionBuilder equation;
    private final ExpressionBuilder target;
    private final Judgement judgement;
    private final boolean negative;

    public Criterion(@NotNull String condition, @NotNull String... variables) {
        condition = condition.trim();
        negative = condition.startsWith("!");
        int operatorIndex = condition.indexOf("<|>|=");
        char operator = condition.charAt(operatorIndex);
        this.judgement = Judgement.fromOperator(operator);
        equation = new ExpressionBuilder(condition.substring(0, operatorIndex)).variables(variables);
        target = new ExpressionBuilder(condition.substring(operatorIndex + 1));
    }

    @Override
    public CompletableFuture<Boolean> check(@Nullable ExecutorService executor, @NotNull Map<String, Double> variables) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        Expression equationExpression = equation.build().setVariables(variables);
        Expression targetExpression = target.build().setVariables(variables);
        if (executor != null) {
            executor.execute(() -> {
                double equationValue = equationExpression.evaluate();
                double targetValue = targetExpression.evaluate();
                boolean compareResult;
                switch (judgement) {
                    case LESS:
                        compareResult = equationValue < targetValue;
                        break;
                    case EQUAL:
                        compareResult = equationValue == targetValue;
                        break;
                    case MORE:
                        compareResult = equationValue > targetValue;
                        break;
                    default:
                        throw new IllegalStateException("Not expected");
                }
                result.complete(negative != compareResult);
            });
        } else {
            double equationValue = equationExpression.evaluate();
            double targetValue = targetExpression.evaluate();
            boolean compareResult;
            switch (judgement) {
                case MORE:
                    compareResult = equationValue > targetValue;
                    break;
                case EQUAL:
                    compareResult = equationValue < targetValue;
                    break;
                case LESS:
                    compareResult = equationValue == targetValue;
                    break;
                default:
                    throw new IllegalStateException("Not expected");
            }
            result.complete(negative != compareResult);
        }
        return result;
    }
}
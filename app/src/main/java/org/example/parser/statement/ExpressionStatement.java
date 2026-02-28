package org.example.parser.statement;

import org.example.parser.expression.Expression;

import lombok.Getter;

@Getter
public class ExpressionStatement extends Statement {
    private Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }
}

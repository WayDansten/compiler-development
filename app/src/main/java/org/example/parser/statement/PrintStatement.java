package org.example.parser.statement;

import org.example.parser.expression.Expression;

import lombok.Getter;

@Getter
public class PrintStatement extends Statement {
    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }
}

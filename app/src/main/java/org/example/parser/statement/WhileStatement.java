package org.example.parser.statement;

import org.example.parser.expression.Expression;

import lombok.Getter;

@Getter
public class WhileStatement extends Statement {
    private Expression condition;
    private Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }
}

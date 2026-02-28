package org.example.parser.statement;

import org.example.parser.expression.Expression;

import lombok.Getter;

@Getter
public class IfStatement extends Statement {
    private Expression condition;
    private Statement thenBranch;
    private Statement elseBranch;

    public IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
}

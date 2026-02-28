package org.example.parser.statement;

import org.example.parser.expression.Expression;

import lombok.Getter;

@Getter
public class VarStatement extends Statement {
    private String name;
    private Expression initializer;

    public VarStatement(String name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }
}

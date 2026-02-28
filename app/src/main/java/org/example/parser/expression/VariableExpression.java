package org.example.parser.expression;

import lombok.Getter;

@Getter
public class VariableExpression extends Expression {
    private String name;

    public VariableExpression(String name) {
        this.name = name;
    }
}

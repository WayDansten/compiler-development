package org.example.parser.expression;

import lombok.Getter;

@Getter
public class AssignExpression extends Expression {
    private String name;
    private Expression value;

    public AssignExpression(String name, Expression value) {
        this.name = name;
        this.value = value;
    }
}

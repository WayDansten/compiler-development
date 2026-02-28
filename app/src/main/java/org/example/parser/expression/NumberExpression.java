package org.example.parser.expression;

import lombok.Getter;

@Getter
public class NumberExpression extends Expression {
    private double value;

    public NumberExpression(double value) {
        this.value = value;
    }
}

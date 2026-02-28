package org.example.parser.expression;

import lombok.Getter;

@Getter
public class StringExpression extends Expression {
    private String value;

    public StringExpression(String value) {
        this.value = value;
    }
}

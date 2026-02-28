package org.example.parser.expression;

import org.example.lexer.TokenType;

import lombok.Getter;

@Getter
public class UnaryExpression extends Expression {
    private TokenType operator;
    private Expression right;

    public UnaryExpression(TokenType operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }
}

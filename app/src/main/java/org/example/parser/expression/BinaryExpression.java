package org.example.parser.expression;

import org.example.lexer.TokenType;

import lombok.Getter;

@Getter
public class BinaryExpression extends Expression {
    private Expression left;
    private TokenType operator;
    private Expression right;

    public BinaryExpression(Expression left, TokenType operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}

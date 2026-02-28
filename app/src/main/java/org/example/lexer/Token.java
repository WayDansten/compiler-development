package org.example.lexer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
    private TokenType type;
    private String value;
    private int position;

    public Token(TokenType type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Token(type: " + type + ", value: " + value + ") at " + position;
    }
}

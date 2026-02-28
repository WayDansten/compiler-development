package org.example.lexer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
    private TokenType type;
    private String value;
    private int position;
    private int line;
    private int column;

    public Token(TokenType type, String value, int position, int line, int column) {
        this.type = type;
        this.value = value;
        this.position = position;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return "Token(type: " + type + ", value: " + value + ") at " + position;
    }
}

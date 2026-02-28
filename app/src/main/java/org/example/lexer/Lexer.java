package org.example.lexer;

import java.util.ArrayList;
import java.util.List;

import org.example.exception.ParseException;

public class Lexer {
    private String input;
    private int length;

    private int position;

    public Lexer(String input) {
        this.input = input;
        this.length = input.length();
        this.position = 0;
    }

    public List<Token> tokenize() throws ParseException {
        List<Token> result = new ArrayList<>();
        while (position < length) {
            var current = peek(input);

            if (Character.isWhitespace(current)) {
                next();
            } else if (Character.isDigit(current)) {
                tokenizeNumber(result);
            } else if (Character.isLetter(current)) {
                tokenizeWord(result);
            } else {
                tokenizeOperator(result);
            }
        }

        return result;
    }

    private void tokenizeNumber(List<Token> result) {
        var start = position;

        while (Character.isDigit(peek(input))) {
            next();
        }

        var numberString = input.substring(start, position - start);
        result.add(new Token(TokenType.NUMBER, numberString, start));
    }

    private void tokenizeWord(List<Token> result) {
        var start = position;

        while (Character.isLetterOrDigit(peek(input))) {
            next();
        }

        var word = input.substring(start, position - start);

        switch (word) {
            case "var":
                addToken(result, TokenType.VAR, word, start);
                break;
            case "print":
                addToken(result, TokenType.PRINT, word, start);
                break;
            case "if":
                addToken(result, TokenType.IF, word, start);
                break;
            case "else":
                addToken(result, TokenType.ELSE, word, start);
                break;
            case "while":
                addToken(result, TokenType.WHILE, word, start);
                break;
            default:
                addToken(result, TokenType.ID, word, start);
                break;
        }
    }

    private void tokenizeOperator(List<Token> result) throws ParseException {
        var current = peek(input);
        var start = position;

        switch(current) {
            case '+':
                next();
                addToken(result, TokenType.PLUS, "+", start);
                break;
            case '-':
                next();
                addToken(result, TokenType.MINUS, "-", start);
                break;
            case '*':
                next();
                addToken(result, TokenType.STAR, "*", start);
                break;
            case '/':
                next();
                addToken(result, TokenType.SLASH, "/", start);
                break;
            case '=':
                next();
                addToken(result, TokenType.EQ, "=", start);
                break;
            case ';':
                next();
                addToken(result, TokenType.SEMICOLON, ";", start);
                break;
            default:
                throw new ParseException("Unexpected character '" + current + "' at position" + position);
        }
    }

    private char peek(String input) {
        if (position >= length) {
            return '\0';
        }
        return input.charAt(position);
    }

    private char next() {
        if (position >= length) {
            return '\0';
        }

        return input.charAt(position++);
    }

    private void addToken(List<Token> result, TokenType type, String value, int position) {
        result.add(new Token(type, value, position));
    }
}

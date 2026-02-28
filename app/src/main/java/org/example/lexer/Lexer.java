package org.example.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.example.exception.ParseException;

public class Lexer {
    private String input;

    private int position;
    private int line;
    private int column;

    private static final HashMap<String, TokenType> keywords = new HashMap<>();
    static {
        keywords.put("var", TokenType.VAR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
    }

    private static final HashMap<String, TokenType> operators = new HashMap<>();
    static {
        operators.put("+", TokenType.PLUS);
        operators.put("-", TokenType.MINUS);
        operators.put("*", TokenType.STAR);
        operators.put("/", TokenType.SLASH);
        operators.put("=", TokenType.EQ);

        operators.put(">", TokenType.GT);
        operators.put("<", TokenType.LT);
        operators.put(">=", TokenType.GTEQ);
        operators.put("<=", TokenType.LTEQ);
        operators.put("==", TokenType.EQEQ);
        operators.put("!=", TokenType.NEQ);

        operators.put("!", TokenType.EXCL);
        operators.put("&&", TokenType.AND);
        operators.put("||", TokenType.OR);

        operators.put("(", TokenType.LPAREN);
        operators.put(")", TokenType.RPAREN);
        operators.put("{", TokenType.LBRACE);
        operators.put("}", TokenType.RBRACE);
        operators.put(";", TokenType.SEMICOLON);

    }

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 1;
        this.column = 1;
    }

    public List<Token> tokenize() throws ParseException {
        List<Token> result = new ArrayList<>();
        while (position < input.length()) {
            var current = peek(input);

            if (Character.isWhitespace(current)) {
                next();
            } else if (Character.isDigit(current)) {
                tokenizeNumber(result);
            } else if (Character.isLetter(current)) {
                tokenizeWord(result);
            } else {
                tokenizeOperatorOrPunctuation(result);
            }
        }

        result.add(new Token(TokenType.EOF, "", position, line, column));

        return result;
    }

    private void tokenizeNumber(List<Token> result) {
        int startPos = position;
        int startLine = line;
        int startColumn = column;

        while (Character.isDigit(peek(input))) {
            next();
        }

        var numberString = input.substring(startPos, position);
        result.add(new Token(TokenType.NUMBER, numberString, startPos, startLine, startColumn));
    }

    private void tokenizeWord(List<Token> result) {
        int startPos = position;
        int startLine = line;
        int startColumn = column;

        while (Character.isLetterOrDigit(peek(input))) {
            next();
        }

        var word = input.substring(startPos, position);
        var type = keywords.getOrDefault(word, TokenType.ID);

        result.add(new Token(type, word, startPos, startLine, startColumn));
    }

    private void tokenizeOperatorOrPunctuation(List<Token> result) throws ParseException {
        int startPos = position;
        int startLine = line;
        int startColumn = column;

        if (position + 1 < input.length()) {
            var twoChars = input.substring(position, position + 2);
            if (operators.containsKey(twoChars)) {
                next();
                next();
                result.add(new Token(operators.get(twoChars), twoChars, startPos, startLine, startColumn));
                return;
            }
        }

        var oneChar = input.substring(position, position + 1);
        if (operators.containsKey(oneChar)) {
            next();
            result.add(new Token(operators.get(oneChar), oneChar, startPos, startLine, startColumn));
        } else {
            var badChar = peek(input);
            throw new ParseException(String.format("[Lexer Error] Unexpected character '%s' at Line %d, Column %d",
                    badChar, startLine, startColumn));
        }
    }

    private char peek(String input) {
        if (position >= input.length()) {
            return '\0';
        }
        return input.charAt(position);
    }

    private char next() {
        if (position >= input.length()) {
            return '\0';
        }

        char current = input.charAt(position++);

        if (current == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        return current;
    }
}

package org.example.parser;

import java.util.ArrayList;
import java.util.List;

import org.example.exception.ParseException;
import org.example.lexer.Token;
import org.example.lexer.TokenType;
import org.example.parser.expression.AssignExpression;
import org.example.parser.expression.BinaryExpression;
import org.example.parser.expression.Expression;
import org.example.parser.expression.NumberExpression;
import org.example.parser.expression.UnaryExpression;
import org.example.parser.expression.VariableExpression;
import org.example.parser.statement.BlockStatement;
import org.example.parser.statement.ExpressionStatement;
import org.example.parser.statement.IfStatement;
import org.example.parser.statement.PrintStatement;
import org.example.parser.statement.Statement;
import org.example.parser.statement.VarStatement;
import org.example.parser.statement.WhileStatement;

public class Parser {
    private final List<Token> tokens;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(parseDeclaration());
        }
        return statements;
    }

    private Statement parseDeclaration() {
        if (match(List.of(TokenType.VAR))) {
            return parseVarDeclaration();
        }

        return parseStatement();
    }

    private Statement parseStatement() {
        if (match(List.of(TokenType.IF))) {
            return parseIfStatement();
        }

        if (match(List.of(TokenType.WHILE))) {
            return parseWhileStatement();
        }

        if (match(List.of(TokenType.PRINT))) {
            return parsePrintStatement();
        }

        if (match(List.of(TokenType.LBRACE))) {
            return new BlockStatement(parseBlock());
        }

        return parseExpressionStatement();
    }

    private Statement parseVarDeclaration() {
        Token name = consume(TokenType.ID, "Ожидается имя переменной.");
        Expression initializer = null;

        if (match(List.of(TokenType.EQ))) {
            initializer = parseExpression();
        }

        consume(TokenType.SEMICOLON, "Ожидается ';' после объявления переменной.");
        return new VarStatement(name.getValue(), initializer);
    }

    private Statement parseIfStatement() {
        consume(TokenType.LPAREN, "Ожидается '(' после 'if'.");
        Expression condition = parseExpression();
        consume(TokenType.RPAREN, "Ожидается ')' после условия 'if'.");

        Statement thenBranch = parseStatement();
        Statement elseBranch = null;

        if (match(List.of(TokenType.ELSE))) {
            elseBranch = parseStatement();
        }

        return new IfStatement(condition, thenBranch, elseBranch);
    }

    private Statement parseWhileStatement() {
        consume(TokenType.LPAREN, "Ожидается '(' после 'while'.");
        Expression condition = parseExpression();
        consume(TokenType.RPAREN, "Ожидается ')' после условия 'while'.");

        Statement body = parseStatement();
        return new WhileStatement(condition, body);
    }

    private Statement parsePrintStatement() {
        Expression value = parseExpression();
        consume(TokenType.SEMICOLON, "Ожидается ';' после значения.");
        return new PrintStatement(value);
    }

    private Statement parseExpressionStatement() {
        Expression expr = parseExpression();
        consume(TokenType.SEMICOLON, "Ожидается ';' после выражения.");
        return new ExpressionStatement(expr);
    }

    private List<Statement> parseBlock() {
        List<Statement> statements = new ArrayList<>();

        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            statements.add(parseDeclaration());
        }

        consume(TokenType.RBRACE, "Ожидается '}' после блока.");
        return statements;
    }

    private Expression parseExpression() {
        return parseAssignment();
    }

    private Expression parseAssignment() {
        Expression expression = parseLogicalOr();

        if (match(List.of(TokenType.EQ))) {
            Token equals = previous();
            Expression value = parseAssignment();

            if (expression instanceof VariableExpression variableExpression) {
                return new AssignExpression(variableExpression.getName(), value);
            }

            throw new ParseException(String.format("[Parser Error] Line %d, Col %d: Invalid assignment target.",
                    equals.getLine(), equals.getColumn()));
        }

        return expression;
    }

    private Expression parseLogicalOr() {
        Expression expression = parseLogicalAnd();

        while (match(List.of(TokenType.OR))) {
            TokenType operator = previous().getType();
            Expression right = parseLogicalAnd();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseLogicalAnd() {
        Expression expression = parseEquality();

        while (match(List.of(TokenType.AND))) {
            TokenType operator = previous().getType();
            Expression right = parseEquality();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseEquality() {
        Expression expression = parseComparison();

        while (match(List.of(TokenType.EQEQ, TokenType.NEQ))) {
            TokenType operator = previous().getType();
            Expression right = parseComparison();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseComparison() {
        Expression expression = parseTerm();

        while (match(List.of(TokenType.LT, TokenType.LTEQ, TokenType.GT, TokenType.GTEQ))) {
            TokenType operator = previous().getType();
            Expression right = parseTerm();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseTerm() {
        Expression expression = parseFactor();

        while (match(List.of(TokenType.PLUS, TokenType.MINUS))) {
            TokenType operator = previous().getType();
            Expression right = parseFactor();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseFactor() {
        Expression expression = parseUnary();

        while (match(List.of(TokenType.STAR, TokenType.SLASH))) {
            TokenType operator = previous().getType();
            Expression right = parseUnary();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseUnary() {
        if (match(List.of(TokenType.MINUS))) {
            TokenType operator = previous().getType();
            Expression right = parseUnary();
            return new UnaryExpression(operator, right);
        }

        return parsePrimary();
    }

    private Expression parsePrimary() {
        if (match(List.of(TokenType.NUMBER))) {
            return new NumberExpression(Double.parseDouble(previous().getValue()));
        }

        if (match(List.of(TokenType.ID))) {
            return new VariableExpression(previous().getValue());
        }

        if (match(List.of(TokenType.LPAREN))) {
            Expression expression = parseExpression();
            consume(TokenType.RPAREN, "Ожидается ')' после выражения.");
            return expression;
        }

        throw new ParseException("Ожидается выражение.");
    }

    private boolean match(List<TokenType> types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }

        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            position++;
        }

        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(position);
    }

    private Token previous() {
        return tokens.get(position - 1);
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }

        Token token = peek();
        throw new ParseException(
                String.format("[Parser Error] Line %d, Col %d: %s", token.getLine(), token.getColumn(), message));
    }
}

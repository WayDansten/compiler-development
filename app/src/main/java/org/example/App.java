package org.example;

import java.util.List;

import org.example.lexer.Lexer;
import org.example.lexer.Token;

public class App {
    public static void main(String[] args) {
        String codeExample = "var x = 123; print x + 5;";

        Lexer lexer = new Lexer(codeExample);
        try {
            List<Token> tokens = lexer.tokenize();
            tokens.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

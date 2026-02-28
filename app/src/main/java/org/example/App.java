package org.example;

import java.util.List;

import org.example.core.ASTPrinter;
import org.example.core.RandomProgramGenerator;
import org.example.lexer.Lexer;
import org.example.lexer.Token;
import org.example.parser.Parser;
import org.example.parser.statement.Statement;

public class App {
    public static void main(String[] args) {
        RandomProgramGenerator generator = new RandomProgramGenerator();
        String program = generator.generate(20);
        System.out.println("Generated Program:\n" + program);

        Lexer lexer = new Lexer(program);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        List<Statement> ast = parser.parse();
        System.out.println(String.format("Successfully parsed %d statements", ast.size()));

        ASTPrinter printer = new ASTPrinter();
        printer.print(ast);
    }
}

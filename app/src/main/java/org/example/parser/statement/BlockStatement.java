package org.example.parser.statement;

import java.util.List;

import lombok.Getter;

@Getter
public class BlockStatement extends Statement {
    private List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }
}

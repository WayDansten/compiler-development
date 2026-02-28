package org.example.core;

import org.example.parser.expression.*;
import org.example.parser.statement.*;

import java.util.List;

public class ASTPrinter {
    
    // Main method for printing the entire program
    public void print(List<Statement> statements) {
        System.out.println("Root (Program)");
        for (int i = 0; i < statements.size(); i++) {
            printNode(statements.get(i), "", i == statements.size() - 1);
        }
    }
    
    // Recursive rendering method
    private void printNode(Object node, String indent, boolean isLast) {
        if (node == null) return;
        
        // Draw the branch
        String marker = isLast ? "└── " : "├── ";
        System.out.print(indent + marker);
        
        // Prepare the indentation for child elements
        String childIndent = indent + (isLast ? "    " : "│   ");
        
        if (node instanceof VarStatement) {
            VarStatement v = (VarStatement) node;
            System.out.println("VarStatement: " + v.getName());
            
            if (v.getInitializer() != null) {
                printNode(v.getInitializer(), childIndent, true);
            }
        }
        else if (node instanceof PrintStatement) {
            PrintStatement p = (PrintStatement) node;
            System.out.println("PrintStatement");
            printNode(p.getExpression(), childIndent, true);
        }
        else if (node instanceof IfStatement) {
            IfStatement i = (IfStatement) node;
            System.out.println("IfStatement");
            printNode(i.getCondition(), childIndent, false);
            printNode(i.getThenBranch(), childIndent, i.getElseBranch() == null);
            
            if (i.getElseBranch() != null) {
                printNode(i.getElseBranch(), childIndent, true);
            }
        }
        else if (node instanceof WhileStatement) {
            WhileStatement w = (WhileStatement) node;
            System.out.println("WhileStatement");
            printNode(w.getCondition(), childIndent, false);
            printNode(w.getBody(), childIndent, true);
        }
        else if (node instanceof BlockStatement) {
            BlockStatement b = (BlockStatement) node;
            System.out.println("BlockStatement");
            for (int j = 0; j < b.getStatements().size(); j++) {
                printNode(b.getStatements().get(j), childIndent, j == b.getStatements().size() - 1);
            }
        }
        else if (node instanceof ExpressionStatement) {
            ExpressionStatement e = (ExpressionStatement) node;
            System.out.println("ExpressionStatement");
            printNode(e.getExpression(), childIndent, true);
        }
        else if (node instanceof BinaryExpression) {
            BinaryExpression bin = (BinaryExpression) node;
            System.out.println("BinaryExpression: " + bin.getOperator());
            printNode(bin.getLeft(), childIndent, false);
            printNode(bin.getRight(), childIndent, true);
        }
        else if (node instanceof UnaryExpression) {
            UnaryExpression un = (UnaryExpression) node;
            System.out.println("UnaryExpression: " + un.getOperator());
            printNode(un.getRight(), childIndent, true);
        }
        else if (node instanceof AssignExpression) {
            AssignExpression assign = (AssignExpression) node;
            System.out.println("AssignExpression: " + assign.getName() + " =");
            printNode(assign.getValue(), childIndent, true);
        }
        else if (node instanceof NumberExpression) {
            NumberExpression num = (NumberExpression) node;
            System.out.println("Number: " + num.getValue());
        }
        else if (node instanceof VariableExpression) {
            VariableExpression varExpr = (VariableExpression) node;
            System.out.println("Variable: " + varExpr.getName());
        }
        else {
            System.out.println("Unknown Node: " + node.getClass().getName());
        }
    }
}

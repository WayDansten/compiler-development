package org.example.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomProgramGenerator {
    private final Random random = new Random();

    // Pool of variable names
    private final String[] varNames = {"x", "y", "z", "alpha", "beta", "count", "total", "index", "sum"};

    // List of already declared variables (to avoid using them before declaration)
    private final List<String> declaredVars = new ArrayList<>();

    private final String[] mathOps = {"+", "-", "*", "/"};
    private final String[] compareOps = {"==", "!=", "<", ">", "<=", ">="};
    private final String[] logicOps = {"&&", "||"};

    /**
     * Generates a random program
     * @param statementCount Number of statements at the top level
     * @return Generated program as a string
     */
    public String generate(int statementCount) {
        declaredVars.clear();
        StringBuilder builder = new StringBuilder();

        // Declare at least a couple of variables at the beginning
        // so there's something to work with
        for (int i = 0; i < 3; i++) {
            builder.append(generateVarDeclaration(0)).append("\n");
        }

        generateBlock(builder, statementCount, 0);

        return builder.toString();
    }

    /**
     * Generates a random program with default statement count of 10
     * @return Generated program as a string
     */
    public String generate() {
        return generate(10);
    }

    private void generateBlock(StringBuilder builder, int count, int indentLevel) {
        String indent = " ".repeat(indentLevel * 4);

        for (int i = 0; i < count; i++) {
            // Choose the type of the next statement
            // 0: Variable declaration (var x = ...)
            // 1: Assignment (x = ...)
            // 2: Print (print ...)
            // 3: If-Else
            // 4: While

            int statementType = random.nextInt(5);

            // Limit nesting of if/while so code doesn't get too deep
            if (indentLevel > 2 && statementType > 2) {
                statementType = random.nextInt(3);
            }

            switch (statementType) {
                case 0:
                    builder.append(generateVarDeclaration(indentLevel)).append("\n");
                    break;
                case 1:
                    if (!declaredVars.isEmpty()) {
                        builder.append(indent).append(getRandomVar()).append(" = ")
                               .append(generateExpression()).append(";\n");
                    } else {
                        builder.append(generateVarDeclaration(indentLevel)).append("\n"); // Fallback
                    }
                    break;
                case 2:
                    builder.append(indent).append("print ").append(generateExpression()).append(";\n");
                    break;
                case 3:
                    builder.append(indent).append("if (").append(generateCondition()).append(") {\n");
                    generateBlock(builder, random.nextInt(3) + 1, indentLevel + 1);

                    if (random.nextDouble() > 0.5) { // 50% chance for else
                        builder.append(indent).append("} else {\n");
                        generateBlock(builder, random.nextInt(2) + 1, indentLevel + 1);
                    }
                    builder.append(indent).append("}\n");
                    break;
                case 4:
                    builder.append(indent).append("while (").append(generateCondition()).append(") {\n");
                    generateBlock(builder, random.nextInt(3) + 1, indentLevel + 1);
                    builder.append(indent).append("}\n");
                    break;
                default:
                    break;
            }
        }
    }

    private String generateVarDeclaration(int indentLevel) {
        String indent = " ".repeat(indentLevel * 4);

        // Pick a random name. If it already exists, we'll just redefine it 
        // (for lexer testing purposes this is ok)
        String varName = varNames[random.nextInt(varNames.length)];
        if (!declaredVars.contains(varName)) {
            declaredVars.add(varName);
        }

        return indent + "var " + varName + " = " + generateExpression() + ";";
    }

    private String generateExpression() {
        // Simple numbers or variables
        if (random.nextDouble() > 0.6 || declaredVars.isEmpty()) {
            return String.valueOf(random.nextInt(99) + 1);
        }

        if (random.nextDouble() > 0.5) {
            return getRandomVar();
        }

        // Compound mathematical expression (e.g., x + 42)
        String left = random.nextDouble() > 0.5 ? getRandomVar() : String.valueOf(random.nextInt(99) + 1);
        String right = random.nextDouble() > 0.5 ? getRandomVar() : String.valueOf(random.nextInt(99) + 1);
        String op = mathOps[random.nextInt(mathOps.length)];

        return left + " " + op + " " + right;
    }

    private String generateCondition() {
        // E.g., x <= 10
        String left = getRandomVarOrNumber();
        String right = getRandomVarOrNumber();
        String compOp = compareOps[random.nextInt(compareOps.length)];

        String condition = left + " " + compOp + " " + right;

        // 30% chance to complicate the condition with a logical operator
        // (e.g., x <= 10 && y == 5)
        if (random.nextDouble() > 0.7) {
            String logicOp = logicOps[random.nextInt(logicOps.length)];
            String extraLeft = getRandomVarOrNumber();
            String extraRight = getRandomVarOrNumber();
            String extraComp = compareOps[random.nextInt(compareOps.length)];

            condition = "(" + condition + ") " + logicOp + " (" + extraLeft + " " + extraComp + " " + extraRight + ")";
        }

        return condition;
    }

    private String getRandomVar() {
        if (declaredVars.isEmpty()) {
            return "1"; // Fallback
        }
        return declaredVars.get(random.nextInt(declaredVars.size()));
    }

    private String getRandomVarOrNumber() {
        if (!declaredVars.isEmpty() && random.nextDouble() > 0.5) {
            return getRandomVar();
        }
        return String.valueOf(random.nextInt(99) + 1);
    }
}

package org.dani.lytrix.LyTrixR.backend.interpreter.evaluation;

import java.util.*;
import java.io.*;
//

//import org.dani.lytrix.core.frontend.parser.*;
import org.dani.lytrix.core.frontend.scanner.tokens.*;

//import javax.management.RuntimeErrorException;
import org.dani.lytrix.core.frontend.ast.ASTNode;
import org.dani.lytrix.core.frontend.ast.baseNodes.*;
import org.dani.lytrix.core.frontend.ast.nodes.*;
import org.dani.lytrix.core.frontend.ast.visitors.NodeVisitor;

//...
//..
//
public abstract class RunTimeEvaluator {

    // Main class attributes:
    protected Map<String, Object> variables;
    protected Map<String, TokenType> types;

    // simple constructor initializing variable and type tables
    protected RunTimeEvaluator() {
        this.variables = new HashMap<String, Object>();
        this.types = new HashMap<String, TokenType>();
    }

    // Special backend processing functions are declared in this layer...
    // Checks if token is literal
    protected boolean validateLiteral(TokenType curr) {
        switch (curr) {
            case INT_LIT:
                return true;
            case CHAR_LIT:
                return true;
            case STRING_LIT:
                return true;
            case FLOAT_LIT:
                return true;
            case DOUBLE_LIT:
                return true;
            default:
                return false;
        }
    }

    // type conversion : implicit converter function
    protected Object convertLiteral(TokenType type, Token literal) {
        switch (type) {
            case INT:
                if (literal.getType() == TokenType.INT_LIT) {
                    return Integer.parseInt(literal.getLex());
                } else if (literal.getType() == TokenType.FLOAT_LIT) {
                    return Integer.parseInt(literal.getLex());
                } else if (literal.getType() == TokenType.DOUBLE_LIT) {
                    return Integer.parseInt(literal.getLex());
                }
                break;
            case CHAR:
                if (literal.getType() == TokenType.CHAR_LIT) {
                    return literal.getLex().charAt(0);
                }
                break;
            case STRING:
                if (literal.getType() == TokenType.STRING_LIT) {
                    return literal.getLex();
                } else if (literal.getType() == TokenType.CHAR_LIT) {
                    return literal.getLex();
                }
                break;
            case FLOAT:
                if (literal.getType() == TokenType.FLOAT_LIT) {
                    return Float.parseFloat(literal.getLex());
                } else if (literal.getType() == TokenType.DOUBLE_LIT) {
                    return Float.parseFloat(literal.getLex());
                } else if (literal.getType() == TokenType.INT_LIT) {
                    return Float.parseFloat(literal.getLex());
                }
                break;
            case DOUBLE:
                if (literal.getType() == TokenType.FLOAT_LIT) {
                    return Double.parseDouble(literal.getLex());
                }
                else if (literal.getType() == TokenType.DOUBLE_LIT) {
                    return Double.parseDouble(literal.getLex());
                } else if (literal.getType() == TokenType.INT_LIT) {
                    return Double.parseDouble(literal.getLex());
                }
                break;
        }
        throw new RuntimeException("Variable Type mismatch!\nCannot assign " + literal.getLex() + " to " + type);

    }

    // converts the string lexeme to corresponding datatype object
    protected Object StringToLiteral(Token literal) {
        switch (literal.getType()) {
            case INT_LIT:
                return Integer.parseInt(literal.getLex());
            case FLOAT_LIT:
                return Float.parseFloat(literal.getLex());
            case DOUBLE_LIT:
                return Double.parseDouble(literal.getLex());
            case CHAR_LIT:
                return literal.getLex().charAt(0);
            case STRING_LIT:
                return literal.getLex();
            default:
                throw new RuntimeException("Invalid literal for output");
        }
    }

    //
    // read specific type of data from user
    protected Object readUserInput(Token identifier, Token type) {
        Scanner input = new Scanner(System.in);
        Object inputObject;
        switch (type.getType()) {
            case INT:
                inputObject = Integer.parseInt(input.nextLine());
                break;
            case CHAR:
                inputObject = input.nextLine().charAt(0);
                break;
            case STRING:
                inputObject = input.nextLine();
                break;
            case FLOAT:
                inputObject = Float.parseFloat(input.nextLine());
                break;
            case DOUBLE:
                inputObject = Double.parseDouble(input.nextLine());
                break;
            default:
                throw new RuntimeException("Unsupported input type: " + type.getLex());

        }
        variables.put(identifier.getLex(), inputObject);
        return inputObject;
    }

    // Process rhs to resolve + convert it for assignment, declaration, reassignment
    // of variables;
    protected Object processVarRhs(TokenType lhsType, Token rhsToken) {
        Object varValue;

        if (rhsToken.getType() == TokenType.IDENT) {
            String rhsVarName = rhsToken.getLex();
            if (types.containsKey(rhsVarName))
                varValue = variables.get(rhsToken.getLex());
            else
                throw new RuntimeException("Variable not found with name : " + rhsVarName);

        } else if (validateLiteral(rhsToken.getType())) {
            varValue = convertLiteral(lhsType, rhsToken);
        } else
            throw new RuntimeException("Invalid Literal for variable");

        return varValue;
    }

}
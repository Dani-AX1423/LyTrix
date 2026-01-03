package org.dani.lytrix.LyTrixR.backend.interpreter;

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
import org.dani.lytrix.LyTrixR.backend.interpreter.evaluation.*;

//...
//..
//
//Main interpreter class that processes the generated ast code by parser.
//Job : traverses through AST nodes, calls respective visitor functions , produces result
// THIS IS WHERE ABSTRACTION turns into MEANINGFUL OUTPUT . THIS IS WHERE MAGIC HAPPENS
public class Interpreter extends RunTimeEvaluator implements NodeVisitor<Object> {
    private ASTNode tree;
    private String retMain;

    // ProgramNode tree;
    public Interpreter(ASTNode tree) {
        super();
        this.tree = tree;

    }

    //
    // exported the helper functions to RunTimeEvaluator file

    //
    //
    // Visitor functions to visit each node at a time.
    // visits the entire program (initiater)
    public Object visitProgram(ProgramNode node) {
        return node.getfn().accept(this);
    }

    // visits functions declarations (modules of program/thread)
    public Object visitFunctionDeclr(FunctionDeclrNode node) {
        return node.getfnBlock().accept(this);
    }

    // visits Each and respective function block of code (Block executor)
    public Object visitFunctionBlock(FunctionBlockNode node) {
        for (AbstractStatement stmt : node.getStmts()) {
            stmt.accept(this);
        }
        return node.getfnRet().accept(this);
    }

    //
    // 3 types of Variable statement processor functions
    // Variable declaration:
    public Object visitVarDeclr(VarDeclrNode node) {

        TokenType varType = node.getDType().getType();
        String varName = node.getIdentifier().getLex();
        if (types.containsKey(varName)) {
            throw new RuntimeException("Variable is already declared with name : " + varName);
        }

        types.put(varName, varType);
        variables.put(varName, null);

        return null;
    }

    // variable initialisation
    public Object visitVarInit(VarInitNode node) {

        String varName = node.getIdentifier().getLex();
        if (types.containsKey(varName)) {
            throw new RuntimeException("Variable is already declared with name : " + varName);
        }
        TokenType varType = node.getDType().getType();
        Token curr_lit = node.getLiteral();

        Object varValue = processVarRhs(varType, curr_lit);// convertLiteral(varType, curr_lit);

        types.put(varName, varType);
        variables.put(varName, varValue);

        return null;
    }

    // variable assignment or reassignment
    public Object visitVarAssign(VarAssignNode node) {

        String varName = node.getIdentifier().getLex();
        Token curr_lit = node.getLiteral();

        if (types.containsKey(varName)) {
            TokenType varType = types.get(varName);
            Object varValue;
            // varValue = convertLiteral(varType, curr_lit);
            // types.replace(varName, varType);
            varValue = processVarRhs(varType, curr_lit);
            variables.replace(varName, varValue);
        } else {
            throw new RuntimeException("Variable is not declared with the name : " + varName);
        }

        return null;
    }

    //
    // Input statement node processing function
    public Object visitInput(InputNode node) {
        Token declrType = node.getDeclrType();
        Token identifier = node.getIdentifier();
        Token argType = node.getArgType();
        if (declrType == null) {

            if (variables.containsKey(identifier.getLex())) {
                TokenType reqType = types.get(identifier.getLex());
                if (reqType == argType.getType()) {
                    readUserInput(identifier, argType);
                    return null;
                } else {
                    throw new RuntimeException("Type mismatch! cannot read object of type <" + argType.getLex()
                            + "> and store it into Object of type <" + reqType.name() + ">");
                }
            } else {
                throw new RuntimeException("Variable is not declared with the name : " + identifier.getLex());
            }
        } else {
            if (variables.containsKey(identifier.getLex())) {
                throw new RuntimeException("cannot perform redeclaration on the same variable!");
            } else {
                TokenType reqType = declrType.getType();
                if (reqType == argType.getType()) {
                    types.put(identifier.getLex(), reqType);
                    readUserInput(identifier, argType);
                    return null;
                } else {
                    throw new RuntimeException("Type mismatch! cannot read object of type <" + argType.getLex()
                            + "> and store it into Object of type <" + declrType.getLex() + ">");
                }
            }

        }

    }

    //
    // Output statement node processing function
    public Object visitOutput(OutputNode node) {
        Token arg = node.getArg();

        if (arg == null) {
            System.out.println();
        } else {
            TokenType argType = arg.getType();
            Object argValue;
            if (argType == TokenType.STRING_LIT || argType == TokenType.CHAR_LIT) {
                argValue = StringToLiteral(arg);
                System.out.println(argValue);
            } else if (argType == TokenType.INT_LIT || argType == TokenType.FLOAT_LIT
                    || argType == TokenType.DOUBLE_LIT) {
                argValue = StringToLiteral(arg);
                System.out.println(argValue);
            }

            // Variable printing or calling
            else if (argType == TokenType.IDENT) {
                String identifier = arg.getLex();
                if (variables.containsKey(identifier)) {
                    Object literal = variables.get(identifier);
                    System.out.println(literal);
                } else {
                    throw new RuntimeException("Variable with name : " + identifier + " not found.");
                }
            }
        }
        return null;
    }

    // visits the terminator of functions (return validator)
    public Object visitReturn(ReturnNode node) {
        Token value = node.getRetVal();
        String retVal = value.getLex();
        if (retVal.equals("EOM")) {
            this.retMain = retVal;
            return null;
        }
        return retVal;
    }

    // special string printer to indicate the end of execution of program
    public String End() {
        return "\n\nProgram Executed!\nRecieved Special object <" + this.retMain + ".>\n\n";
    }

    // Main interpret function that executes tree and its nodes (Chain reaction)
    public void interpret() {
        tree.accept(this);
    }

}

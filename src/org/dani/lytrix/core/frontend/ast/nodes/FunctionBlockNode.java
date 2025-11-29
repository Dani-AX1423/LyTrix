package org.dani.lytrix.core.frontend.ast.nodes;
//Required : Abstract Classes,Token class, NodeVisitor interface
import org.dani.lytrix.core.frontend.ast.baseNodes.*;
import org.dani.lytrix.core.frontend.scanner.tokens.*;
import org.dani.lytrix.LyTrix_R.backend.interpreter.NodeVisitor;

//function block and its code are turned into nodes or tree here
public class FunctionBlockNode extends AbstractBlock {
private ReturnNode fnRetMain;
public FunctionBlockNode(ReturnNode fnRetMain)
{this.fnRetMain = fnRetMain;}

//special functions for displaying in string format for human readability
private String indent(String text)
{return " " + text.replace("\n","\n  ");}
public String toString()
{return "Function Block : \n" + indent(fnRetMain.toString());}

//node value retriever for return statement
public ReturnNode getfnRet()
{return fnRetMain;}

//accept function to verify logic and operation and call the next node visitor
@Override
public <R> R accept(NodeVisitor<R> tree)
{return tree.visitFunctionBlock(this);}
}

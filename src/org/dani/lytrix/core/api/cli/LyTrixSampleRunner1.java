package org.dani.lytrix.core.api.cli;

import java.util.*;
import java.io.*;

import org.dani.lytrix.core.frontend.scanner.streams.SourceReader;
import org.dani.lytrix.core.frontend.scanner.tokens.Token;
import org.dani.lytrix.core.frontend.scanner.tokens.TokenType;
import org.dani.lytrix.core.frontend.scanner.Lexing_Process.Lexer;
import org.dani.lytrix.core.frontend.scanner.Lexing_Process.LexHelperFunctions;


public class LyTrixSampleRunner1
{
public static void main(String args[])
{
Scanner sc = new Scanner(System.in);
String path;
String SrcCode;
System.out.print("Enter the correct path of file : ");
path = sc.nextLine();
SourceReader sr = new SourceReader(path);
try {
SrcCode = sr.CodeReader();
Lexer lexical = new Lexer(SrcCode);
List<Token> tokens = new ArrayList<Token>();
tokens = lexical.getTokens();
System.out.println("Tokens:\n");
for(Token token:tokens)
{System.out.println(token);}
}
catch(IOException e)
{System.out.println("File error! : " +e.getMessage());}


}
}



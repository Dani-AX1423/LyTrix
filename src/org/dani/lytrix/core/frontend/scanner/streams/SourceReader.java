package org.dani.lytrix.core.frontend.scanner.streams;

import java.util.*;
import java.io.*;
public class SourceReader
{
String path;
StringBuilder SrcCode = new StringBuilder();
public SourceReader(String path)
{this.path=path;}

public String CodeReader() throws IOException
{
String c;
try {
BufferedReader br = new BufferedReader(new FileReader(path));
while((c=br.readLine())!=null)
{
SrcCode.append(c);
SrcCode.append("\n");
}
//SrcCode.append(c);
br.close();
return SrcCode.toString();
}
catch(IOException e)
{System.out.println("File Operation Error" + e.getMessage());return "";}
}
}

package org.apache.xpath;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xpath.functions.FuncExtFunction;

public interface ExtensionsProvider {
   boolean functionAvailable(String var1, String var2) throws TransformerException;

   boolean elementAvailable(String var1, String var2) throws TransformerException;

   Object extFunction(String var1, String var2, Vector var3, Object var4) throws TransformerException;

   Object extFunction(FuncExtFunction var1, Vector var2) throws TransformerException;
}

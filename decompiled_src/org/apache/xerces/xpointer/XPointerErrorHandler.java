package org.apache.xerces.xpointer;

import java.io.PrintWriter;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLParseException;

class XPointerErrorHandler implements XMLErrorHandler {
   protected PrintWriter fOut;

   public XPointerErrorHandler() {
      this(new PrintWriter(System.err));
   }

   public XPointerErrorHandler(PrintWriter var1) {
      this.fOut = var1;
   }

   public void warning(String var1, String var2, XMLParseException var3) throws XNIException {
      this.printError("Warning", var3);
   }

   public void error(String var1, String var2, XMLParseException var3) throws XNIException {
      this.printError("Error", var3);
   }

   public void fatalError(String var1, String var2, XMLParseException var3) throws XNIException {
      this.printError("Fatal Error", var3);
      throw var3;
   }

   private void printError(String var1, XMLParseException var2) {
      this.fOut.print("[");
      this.fOut.print(var1);
      this.fOut.print("] ");
      String var3 = var2.getExpandedSystemId();
      if (var3 != null) {
         int var4 = var3.lastIndexOf(47);
         if (var4 != -1) {
            var3 = var3.substring(var4 + 1);
         }

         this.fOut.print(var3);
      }

      this.fOut.print(':');
      this.fOut.print(var2.getLineNumber());
      this.fOut.print(':');
      this.fOut.print(var2.getColumnNumber());
      this.fOut.print(": ");
      this.fOut.print(var2.getMessage());
      this.fOut.println();
      this.fOut.flush();
   }
}

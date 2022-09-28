package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.util.Encoding;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

public final class NumberingDefinitions {
   private Vector definitions = new Vector();

   public void add(Numbering var1) {
      var1.id = this.definitions.size() + 1;
      this.definitions.addElement(var1);
   }

   public byte[] getBytes(String var1) throws Exception {
      StringWriter var2 = new StringWriter();
      PrintWriter var3 = new PrintWriter(var2);
      var3.println("<?xml version=\"1.0\" encoding=\"" + Encoding.officialName(var1) + "\"?>");
      this.print(var3);
      var3.flush();
      return var2.toString().getBytes(var1);
   }

   private void print(PrintWriter var1) {
      var1.println("<w:numbering xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">");
      int var2 = 0;

      int var3;
      Numbering var4;
      for(var3 = this.definitions.size(); var2 < var3; ++var2) {
         var4 = (Numbering)this.definitions.elementAt(var2);
         var4.print(var1);
      }

      var2 = 0;

      for(var3 = this.definitions.size(); var2 < var3; ++var2) {
         var4 = (Numbering)this.definitions.elementAt(var2);
         int var5 = var4.id;
         var1.println("<w:num w:numId=\"" + var5 + "\">");
         var1.println("<w:abstractNumId w:val=\"" + var5 + "\" />");
         var1.println("</w:num>");
      }

      var1.println("</w:numbering>");
   }
}

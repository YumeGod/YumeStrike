package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.util.Encoding;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class DocumentSettings {
   public boolean singleSided;
   public boolean mirrorMargins;

   public byte[] getBytes(String var1) throws Exception {
      StringWriter var2 = new StringWriter();
      PrintWriter var3 = new PrintWriter(var2);
      var3.println("<?xml version=\"1.0\" encoding=\"" + Encoding.officialName(var1) + "\"?>");
      this.print(var3);
      var3.flush();
      return var2.toString().getBytes(var1);
   }

   private void print(PrintWriter var1) {
      var1.println("<w:settings xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">");
      var1.println("<w:view w:val=\"print\" />");
      if (!this.singleSided && this.mirrorMargins) {
         var1.println("<w:mirrorMargins />");
      }

      var1.println("<w:bordersDoNotSurroundHeader />");
      var1.println("<w:bordersDoNotSurroundFooter />");
      if (!this.singleSided) {
         var1.println("<w:evenAndOddHeaders />");
      }

      var1.println("</w:settings>");
   }
}

package com.xmlmind.fo.converter.docx;

import java.io.PrintWriter;

public class Field {
   public String instruction;
   public String result;
   public RunProperties properties;

   public Field(String var1) {
      this(var1, "");
   }

   public Field(String var1, String var2) {
      this(var1, var2, (RunProperties)null);
   }

   public Field(String var1, String var2, RunProperties var3) {
      this.instruction = var1;
      this.result = var2;
      this.properties = var3;
   }

   public void print(PrintWriter var1) {
      var1.println("<w:r><w:fldChar w:fldCharType=\"begin\" /></w:r>");
      var1.println("<w:r>");
      if (this.properties != null) {
         this.properties.print(var1);
      }

      var1.println("<w:instrText>" + Wml.escape(this.instruction) + "</w:instrText>");
      var1.println("</w:r>");
      var1.println("<w:r><w:fldChar w:fldCharType=\"separate\" /></w:r>");
      var1.println("<w:r>");
      if (this.properties != null) {
         this.properties.print(var1);
      }

      var1.println("<w:t>" + Wml.escape(this.result) + "</w:t>");
      var1.println("</w:r>");
      var1.println("<w:r><w:fldChar w:fldCharType=\"end\" /></w:r>");
   }
}

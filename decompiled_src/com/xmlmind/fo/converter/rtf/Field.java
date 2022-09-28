package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.util.Encoder;
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

   public void print(PrintWriter var1, Encoder var2) {
      var1.print("{");
      if (this.properties != null) {
         this.properties.print(var1);
      }

      var1.println();
      var1.print("{\\field");
      var1.print("{\\*\\fldinst " + Rtf.escape(this.instruction, var2) + "}");
      var1.print("{\\fldrslt " + Rtf.escape(this.result, var2) + "}");
      var1.println("}");
      var1.println("}");
   }
}

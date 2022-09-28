package com.xmlmind.fo.converter.docx.sdt;

import com.xmlmind.fo.converter.docx.RunProperties;
import java.io.PrintWriter;
import org.xml.sax.Attributes;

public final class SdtTextField extends SdtElement {
   private boolean multiLine;

   public SdtTextField(Attributes var1, RunProperties var2) {
      super(var1, var2);
      String var3 = var1.getValue("", "multi-line");
      if (var3 != null) {
         if (var3.equals("true")) {
            this.multiLine = true;
         } else if (var3.equals("false")) {
            this.multiLine = false;
         }
      }

      if (this.title == null) {
         this.title = "Text Field";
      }

   }

   public boolean preserveSpace() {
      return this.multiLine;
   }

   protected void printType(PrintWriter var1) {
      if (this.multiLine) {
         var1.println("<w:text w:multiLine=\"true\" />");
      } else {
         var1.println("<w:text />");
      }

   }
}

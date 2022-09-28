package com.xmlmind.fo.converter.docx.sdt;

import com.xmlmind.fo.converter.docx.RunProperties;
import java.io.PrintWriter;
import org.xml.sax.Attributes;

public final class SdtComboBox extends SdtElement {
   private boolean editable;
   private SdtList list;

   public SdtComboBox(Attributes var1, RunProperties var2) {
      this(var1, var2, true);
   }

   public SdtComboBox(Attributes var1, RunProperties var2, boolean var3) {
      super(var1, var2);
      this.list = new SdtList();
      if (this.title == null) {
         this.title = var3 ? "Combo Box" : "Drop-Down List";
      }

      this.editable = var3;
   }

   public void addEntry(Attributes var1) {
      String var2 = var1.getValue("", "value");
      if (var2 != null) {
         String var3 = var1.getValue("", "display-text");
         if (var3 == null) {
            var3 = var2;
         }

         this.list.add(var2, var3);
      }
   }

   protected void printType(PrintWriter var1) {
      String var2 = this.editable ? "comboBox" : "dropDownList";
      var1.println("<w:" + var2 + ">");
      this.list.print(var1);
      var1.println("</w:" + var2 + ">");
   }
}

package com.xmlmind.fo.converter.docx.sdt;

import com.xmlmind.fo.converter.docx.Wml;
import java.io.PrintWriter;
import java.util.Vector;

public final class SdtList {
   private Vector entries = new Vector();

   public void add(String var1, String var2) {
      this.entries.addElement(new Entry(var1, var2));
   }

   public void print(PrintWriter var1) {
      int var2 = 0;

      for(int var3 = this.entries.size(); var2 < var3; ++var2) {
         Entry var4 = (Entry)this.entries.elementAt(var2);
         var1.print("<w:listItem w:value=\"" + Wml.escape(var4.value));
         if (var4.displayText != null) {
            var1.print("\" w:displayText=\"" + Wml.escape(var4.displayText));
         }

         var1.println("\" />");
      }

   }

   private class Entry {
      String value;
      String displayText;

      Entry(String var2, String var3) {
         this.value = var2;
         this.displayText = var3;
      }
   }
}

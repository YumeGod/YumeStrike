package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class Footnote {
   private static final int TYPE_PARAGRAPH = 1;
   private static final int TYPE_TABLE = 2;
   private static final int TYPE_TABLE_AND_CAPTION = 3;
   private Vector elements = new Vector();

   public void add(Paragraph var1) {
      this.add(var1, 1);
   }

   private void add(Object var1, int var2) {
      this.elements.addElement(new Element(var2, var1));
   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      var1.println("<w:footnote w:suppressRef=\"on\">");
      int var3 = 0;
      int var4 = this.elements.size();

      while(var3 < var4) {
         Element var5 = (Element)this.elements.elementAt(var3);
         switch (var5.type) {
            case 1:
               Paragraph var6 = (Paragraph)var5.object;
               var6.print(var1, var2);
            default:
               ++var3;
         }
      }

      var1.println("</w:footnote>");
   }

   private class Element {
      int type;
      Object object;

      Element(int var2, Object var3) {
         this.type = var2;
         this.object = var3;
      }
   }
}

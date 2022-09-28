package com.xmlmind.fo.converter.docx;

import java.io.PrintWriter;
import java.util.Vector;

public final class Footnote {
   public int id;
   private Vector content = new Vector();

   public void add(Paragraph var1) {
      this.add(0, var1);
   }

   public void add(List var1) {
      this.add(3, var1);
   }

   private void add(int var1, Object var2) {
      this.content.addElement(new Element(var1, var2));
   }

   public void print(PrintWriter var1) {
      var1.println("<w:footnote w:id=\"" + this.id + "\">");
      int var2 = 0;

      for(int var3 = this.content.size(); var2 < var3; ++var2) {
         Element var4 = (Element)this.content.elementAt(var2);
         switch (var4.type) {
            case 0:
               Paragraph var5 = (Paragraph)var4.object;
               var5.print(var1);
               break;
            case 3:
               List var6 = (List)var4.object;
               var6.print(var1);
         }
      }

      var1.println("</w:footnote>");
   }

   private class Element {
      static final int TYPE_PARAGRAPH = 0;
      static final int TYPE_TABLE = 1;
      static final int TYPE_TABLE_AND_CAPTION = 2;
      static final int TYPE_LIST = 3;
      int type;
      Object object;

      Element(int var2, Object var3) {
         this.type = var2;
         this.object = var3;
      }
   }
}

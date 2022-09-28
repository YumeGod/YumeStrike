package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public class StaticContent {
   private static final int TYPE_PARAGRAPH = 1;
   private static final int TYPE_TABLE = 2;
   private static final int TYPE_TABLE_AND_CAPTION = 3;
   private Vector elements = new Vector();

   public void add(Paragraph var1) {
      this.add(var1, 1);
   }

   public void add(Table var1) {
      this.add(var1, 2);
   }

   public void add(TableAndCaption var1) {
      this.add(var1, 3);
   }

   private void add(Object var1, int var2) {
      this.elements.addElement(new Element(var2, var1));
   }

   public void print(PrintWriter var1, Encoder var2, int var3) throws Exception {
      int var4 = 0;

      for(int var5 = this.elements.size(); var4 < var5; ++var4) {
         Element var6 = (Element)this.elements.elementAt(var4);
         switch (var6.type) {
            case 1:
               Paragraph var7 = (Paragraph)var6.object;
               var7.print(var1, var2);
               break;
            case 2:
               Table var8 = (Table)var6.object;
               var8.print(var1, var2, var3);
               break;
            case 3:
               TableAndCaption var9 = (TableAndCaption)var6.object;
               var9.print(var1, var2, var3);
         }
      }

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

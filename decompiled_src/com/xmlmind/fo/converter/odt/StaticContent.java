package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class StaticContent {
   private Vector content = new Vector();

   private void add(int var1, Object var2) {
      this.content.addElement(new Element(var1, var2));
   }

   public void add(Paragraph var1) {
      this.add(0, var1);
   }

   public void add(Table var1) {
      this.add(1, var1);
   }

   public void add(TableAndCaption var1) {
      this.add(2, var1);
   }

   public void add(List var1) {
      this.add(3, var1);
   }

   public void print(PrintWriter var1, Encoder var2) {
      int var3 = 0;

      for(int var4 = this.content.size(); var3 < var4; ++var3) {
         Element var5 = (Element)this.content.elementAt(var3);
         switch (var5.type) {
            case 0:
               Paragraph var6 = (Paragraph)var5.object;
               var6.print(var1, var2);
               break;
            case 1:
               Table var7 = (Table)var5.object;
               var7.print(var1, var2);
               break;
            case 2:
               TableAndCaption var8 = (TableAndCaption)var5.object;
               var8.print(var1, var2);
               break;
            case 3:
               List var9 = (List)var5.object;
               var9.print(var1, var2);
         }
      }

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

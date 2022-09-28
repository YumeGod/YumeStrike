package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class Footnote {
   private static final int TYPE_PARAGRAPH = 1;
   private static final int TYPE_TABLE = 2;
   private static final int TYPE_TABLE_AND_CAPTION = 3;
   private boolean isEndNote;
   private Vector elements;

   public Footnote() {
      this(false);
   }

   public Footnote(boolean var1) {
      this.elements = new Vector();
      this.isEndNote = var1;
   }

   public void add(Paragraph var1) {
      this.add(var1, 1);
   }

   private void add(Object var1, int var2) {
      this.elements.addElement(new Element(var2, var1));
   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      var1.print("{\\footnote");
      if (this.isEndNote) {
         var1.print("\\ftnalt");
      }

      var1.println();

      int var3;
      for(var3 = this.elements.size() - 1; var3 >= 0; --var3) {
         Element var4 = (Element)this.elements.elementAt(var3);
         if (var4.type != 1) {
            break;
         }

         Paragraph var5 = (Paragraph)var4.object;
         if (!var5.isVoid()) {
            var5.isLast = true;
            break;
         }
      }

      var3 = 0;
      int var7 = this.elements.size();

      while(var3 < var7) {
         Element var8 = (Element)this.elements.elementAt(var3);
         switch (var8.type) {
            case 1:
               Paragraph var6 = (Paragraph)var8.object;
               var6.print(var1, var2);
            default:
               ++var3;
         }
      }

      var1.println("}");
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

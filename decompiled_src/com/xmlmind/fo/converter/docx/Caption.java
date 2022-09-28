package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;
import java.util.Vector;

public final class Caption {
   public static final int SIDE_TOP = 0;
   public static final int SIDE_BOTTOM = 1;
   public int side;
   private Vector content = new Vector();

   public Caption(Context var1) {
      this.initialize(var1);
   }

   private void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      switch (var2[72].keyword()) {
         case 4:
         case 27:
            this.side = 1;
            break;
         case 16:
         case 204:
         default:
            this.side = 0;
      }

   }

   public void add(Paragraph var1) {
      this.add(0, var1);
   }

   private void add(int var1, Object var2) {
      this.content.addElement(new Element(var1, var2));
   }

   public void print(PrintWriter var1) {
      int var2 = 0;
      int var3 = this.content.size();

      while(var2 < var3) {
         Element var4 = (Element)this.content.elementAt(var2);
         switch (var4.type) {
            case 0:
               Paragraph var5 = (Paragraph)var4.object;
               var5.print(var1);
            default:
               ++var2;
         }
      }

   }

   public double minWidth() {
      double var1 = 0.0;
      int var3 = 0;
      int var4 = this.content.size();

      while(var3 < var4) {
         Element var5 = (Element)this.content.elementAt(var3);
         switch (var5.type) {
            case 0:
               Paragraph var6 = (Paragraph)var5.object;
               double var7 = var6.minWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
            default:
               ++var3;
         }
      }

      return var1;
   }

   public double maxWidth() {
      double var1 = 0.0;
      int var3 = 0;
      int var4 = this.content.size();

      while(var3 < var4) {
         Element var5 = (Element)this.content.elementAt(var3);
         switch (var5.type) {
            case 0:
               Paragraph var6 = (Paragraph)var5.object;
               double var7 = var6.maxWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
            default:
               ++var3;
         }
      }

      return var1;
   }

   private class Element {
      static final int TYPE_PARAGRAPH = 0;
      int type;
      Object object;

      Element(int var2, Object var3) {
         this.type = var2;
         this.object = var3;
      }
   }
}

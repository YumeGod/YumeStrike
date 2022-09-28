package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public class Caption {
   public static final int SIDE_TOP = 1;
   public static final int SIDE_BOTTOM = 2;
   public int side;
   private Vector paragraphs;

   public Caption() {
      this.paragraphs = new Vector();
   }

   public Caption(Context var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      switch (var2[72].keyword()) {
         case 4:
         case 27:
            this.side = 2;
            break;
         case 16:
         case 204:
            this.side = 1;
            break;
         default:
            this.side = 1;
      }

   }

   public void add(Paragraph var1) {
      this.paragraphs.addElement(var1);
   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      int var3 = 0;

      for(int var4 = this.paragraphs.size(); var3 < var4; ++var3) {
         Paragraph var5 = (Paragraph)this.paragraphs.elementAt(var3);
         var5.print(var1, var2);
      }

   }

   public int maxWidth() {
      int var1 = 0;
      int var2 = 0;

      for(int var3 = this.paragraphs.size(); var2 < var3; ++var2) {
         Paragraph var4 = (Paragraph)this.paragraphs.elementAt(var2);
         int var5 = var4.maxWidth();
         if (var5 > var1) {
            var1 = var5;
         }
      }

      return var1;
   }

   public int minWidth() {
      int var1 = 0;
      int var2 = 0;

      for(int var3 = this.paragraphs.size(); var2 < var3; ++var2) {
         Paragraph var4 = (Paragraph)this.paragraphs.elementAt(var2);
         int var5 = var4.minWidth();
         if (var5 > var1) {
            var1 = var5;
         }
      }

      return var1;
   }
}

package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class TableCell {
   public int colNumber;
   public int colSpan;
   public int rowSpan;
   public double width;
   public boolean relativeWidth;
   public boolean startsRow;
   public boolean endsRow;
   public TableCellStyle style;
   public boolean isCovered;
   public boolean requiresLayout;
   private Vector content = new Vector();

   public TableCell(int var1) {
      this.colNumber = var1;
      this.colSpan = 1;
      this.rowSpan = 1;
      this.style = new TableCellStyle();
   }

   public TableCell(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.colNumber = number(var2[82]);
      this.colSpan = number(var2[187]);
      this.rowSpan = number(var2[188]);
      Value var3 = var2[308];
      switch (var3.type) {
         case 4:
            this.width = var3.length();
            break;
         case 13:
            this.width = var3.percentage();
            this.relativeWidth = true;
      }

      if (var2[279].keyword() == 209) {
         this.startsRow = true;
      }

      if (var2[98].keyword() == 209) {
         this.endsRow = true;
      }

      this.style = new TableCellStyle(var1);
   }

   private static int number(Value var0) {
      int var1 = (int)Math.round(var0.number());
      if (var1 < 1) {
         var1 = 1;
      }

      return var1;
   }

   public boolean hasBackground() {
      return this.style.background != -1;
   }

   public void setBackground(Color var1) {
      this.style.background = Odt.rgb(var1);
   }

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

   public void layout(double var1, StyleTable var3) throws Exception {
      var1 -= this.style.paddingLeft + this.style.paddingRight;
      int var4 = 0;

      for(int var5 = this.content.size(); var4 < var5; ++var4) {
         Element var6 = (Element)this.content.elementAt(var4);
         switch (var6.type) {
            case 0:
               Paragraph var7 = (Paragraph)var6.object;
               if (var7.requiresLayout()) {
                  var7.layout(var1);
                  var7.style = var3.add(var7.style);
               }
               break;
            case 1:
               Table var8 = (Table)var6.object;
               var8.layout(var1, var3);
               break;
            case 2:
               TableAndCaption var9 = (TableAndCaption)var6.object;
               var9.layout(var1, var3);
               break;
            case 3:
               List var10 = (List)var6.object;
               var10.layout(var1, var3);
         }
      }

   }

   public void print(PrintWriter var1, Encoder var2) {
      if (this.isCovered) {
         var1.print("<table:covered-table-cell");
         var1.print(" table:style-name=\"" + this.style.name + "\"");
         var1.println("/>");
      } else {
         var1.print("<table:table-cell");
         var1.print(" table:style-name=\"" + this.style.name + "\"");
         if (this.colSpan > 1) {
            var1.print(" table:number-columns-spanned=\"" + this.colSpan + "\"");
         }

         if (this.rowSpan > 1) {
            var1.print(" table:number-rows-spanned=\"" + this.rowSpan + "\"");
         }

         var1.println(">");
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

         var1.print("</table:table-cell>");
      }
   }

   public double minWidth() {
      double var1 = 0.0;
      int var3 = 0;

      for(int var4 = this.content.size(); var3 < var4; ++var3) {
         Element var5 = (Element)this.content.elementAt(var3);
         double var7;
         switch (var5.type) {
            case 0:
               Paragraph var6 = (Paragraph)var5.object;
               var7 = var6.minWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
               break;
            case 1:
               Table var9 = (Table)var5.object;
               var7 = var9.minWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
               break;
            case 2:
               TableAndCaption var10 = (TableAndCaption)var5.object;
               var7 = var10.minWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
               break;
            case 3:
               List var11 = (List)var5.object;
               var7 = var11.minWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
         }
      }

      var1 += this.style.paddingLeft + this.style.paddingRight;
      ++var1;
      if (!this.relativeWidth && this.width > var1) {
         var1 = this.width;
      }

      return var1;
   }

   public double maxWidth() {
      double var1 = 0.0;
      int var3 = 0;

      for(int var4 = this.content.size(); var3 < var4; ++var3) {
         Element var5 = (Element)this.content.elementAt(var3);
         double var7;
         switch (var5.type) {
            case 0:
               Paragraph var6 = (Paragraph)var5.object;
               var7 = var6.maxWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
               break;
            case 1:
               Table var9 = (Table)var5.object;
               var7 = var9.maxWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
               break;
            case 2:
               TableAndCaption var10 = (TableAndCaption)var5.object;
               var7 = var10.maxWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
               break;
            case 3:
               List var11 = (List)var5.object;
               var7 = var11.maxWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
         }
      }

      var1 += this.style.paddingLeft + this.style.paddingRight;
      ++var1;
      if (!this.relativeWidth && this.width > var1) {
         var1 = this.width;
      }

      return var1;
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

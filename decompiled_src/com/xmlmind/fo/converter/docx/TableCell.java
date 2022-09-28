package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;
import java.util.Vector;

public final class TableCell implements Cloneable {
   public static final int ALIGNMENT_TOP = 0;
   public static final int ALIGNMENT_CENTER = 1;
   public static final int ALIGNMENT_BOTTOM = 2;
   public static final int MERGE_NONE = 0;
   public static final int MERGE_START = 1;
   public static final int MERGE_CONTINUE = 2;
   public int colNumber;
   public int colSpan;
   public int rowSpan;
   public double width;
   public boolean relativeWidth;
   public boolean startsRow;
   public boolean endsRow;
   public Borders borders;
   public double marginTop;
   public double marginBottom;
   public double marginLeft;
   public double marginRight;
   public Color background;
   public int alignment;
   public boolean requiresLayout;
   public boolean isInvisible;
   public int hMerge = 0;
   public int vMerge = 0;
   private Vector content = new Vector();

   public TableCell(int var1) {
      this.colNumber = var1;
      this.colSpan = 1;
      this.rowSpan = 1;
      this.borders = new Borders();
      this.alignment = 0;
   }

   public TableCell(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.colNumber = number(var2[82]);
      this.colSpan = number(var2[187]);
      this.rowSpan = number(var2[188]);
      if (var2[279].keyword() == 209) {
         this.startsRow = true;
      }

      if (var2[98].keyword() == 209) {
         this.endsRow = true;
      }

      Value var3 = var2[308];
      switch (var3.type) {
         case 4:
            this.width = var3.length();
            break;
         case 13:
            this.width = var3.percentage();
            this.relativeWidth = true;
      }

      this.borders = new Borders(var2);
      this.marginTop = this.borders.top.space;
      this.borders.top.space = 0.0;
      this.marginBottom = this.borders.bottom.space;
      this.borders.bottom.space = 0.0;
      this.marginLeft = this.borders.left.space;
      this.borders.left.space = 0.0;
      this.marginRight = this.borders.right.space;
      this.borders.right.space = 0.0;
      Value var4 = var2[8];
      if (var4.type == 24) {
         this.background = var4.color();
      }

      switch (var2[93].keyword()) {
         case 4:
            this.alignment = 2;
            break;
         case 16:
         default:
            this.alignment = 0;
            break;
         case 31:
            this.alignment = 1;
      }

   }

   private static int number(Value var0) {
      int var1 = (int)Math.round(var0.number());
      if (var1 < 1) {
         var1 = 1;
      }

      return var1;
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

   private void add(int var1, Object var2) {
      this.content.addElement(new Element(var1, var2));
   }

   public void keepWithNext() {
      if (this.isEmpty()) {
         Paragraph var3 = Paragraph.empty();
         var3.properties.keepWithNext = true;
         this.add(var3);
      } else {
         Element var1 = (Element)this.content.elementAt(0);
         switch (var1.type) {
            case 0:
               Paragraph var2 = (Paragraph)var1.object;
               if (var2.properties == null) {
                  var2.properties = new ParagraphProperties();
               }

               var2.properties.keepWithNext = true;
            default:
         }
      }
   }

   public void breakBefore(int var1) {
      if (this.isEmpty()) {
         Paragraph var4 = Paragraph.empty();
         var4.properties.breakBefore = var1;
         this.add(var4);
      } else {
         Element var2 = (Element)this.content.elementAt(0);
         switch (var2.type) {
            case 0:
               Paragraph var3 = (Paragraph)var2.object;
               if (var3.properties == null) {
                  var3.properties = new ParagraphProperties();
               }

               var3.properties.breakBefore = var1;
            default:
         }
      }
   }

   public boolean isEmpty() {
      return this.content.size() == 0;
   }

   public boolean hasMargin() {
      return this.marginTop > 0.0 || this.marginBottom > 0.0 || this.marginLeft > 0.0 || this.marginRight > 0.0;
   }

   public void layout(double var1, NumberingDefinitions var3) throws Exception {
      var1 -= this.marginLeft + this.marginRight;
      int var4 = 0;

      for(int var5 = this.content.size(); var4 < var5; ++var4) {
         Element var6 = (Element)this.content.elementAt(var4);
         switch (var6.type) {
            case 0:
               Paragraph var7 = (Paragraph)var6.object;
               if (var7.requiresLayout()) {
                  var7.layout(var1);
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

   public void print(PrintWriter var1) {
      var1.println("<w:tc>");
      var1.print("<w:tcPr>");
      if (this.hMerge != 0) {
         var1.print("<w:hMerge w:val=\"" + merge(this.hMerge) + "\" />");
      }

      if (this.vMerge != 0) {
         var1.print("<w:vMerge w:val=\"" + merge(this.vMerge) + "\" />");
      }

      if (this.borders != null && this.borders.materialized()) {
         var1.print("<w:tcBorders>");
         this.borders.print(var1);
         var1.print("</w:tcBorders>");
      }

      if (this.background != null) {
         var1.print("<w:shd");
         var1.print(" w:val=\"clear\"");
         var1.print(" w:fill=\"" + Wml.hexColorType(this.background) + "\"");
         var1.print(" />");
      }

      if (this.hasMargin()) {
         var1.print("<w:tcMar>");
         if (this.marginTop > 0.0) {
            var1.print(margin("top", this.marginTop));
         }

         if (this.marginLeft > 0.0) {
            var1.print(margin("left", this.marginLeft));
         }

         if (this.marginBottom > 0.0) {
            var1.print(margin("bottom", this.marginBottom));
         }

         if (this.marginRight > 0.0) {
            var1.print(margin("right", this.marginRight));
         }

         var1.print("</w:tcMar>");
      }

      var1.print("<w:vAlign w:val=\"" + this.alignment() + "\" />");
      var1.println("</w:tcPr>");
      if (this.isEmpty()) {
         Paragraph.EMPTY.print(var1);
      } else {
         int var2 = 0;

         for(int var3 = this.content.size(); var2 < var3; ++var2) {
            Element var4 = (Element)this.content.elementAt(var2);
            switch (var4.type) {
               case 0:
                  Paragraph var5 = (Paragraph)var4.object;
                  var5.print(var1);
                  break;
               case 1:
                  Table var6 = (Table)var4.object;
                  var6.print(var1);
                  break;
               case 2:
                  TableAndCaption var7 = (TableAndCaption)var4.object;
                  var7.print(var1);
                  break;
               case 3:
                  List var8 = (List)var4.object;
                  var8.print(var1);
            }
         }

         Element var9 = (Element)this.content.lastElement();
         switch (var9.type) {
            case 1:
               Paragraph.EMPTY.print(var1);
               break;
            case 2:
               TableAndCaption var10 = (TableAndCaption)var9.object;
               if (var10.caption == null || var10.caption.side == 0) {
                  Paragraph.EMPTY.print(var1);
               }
         }
      }

      var1.println("</w:tc>");
   }

   private static String merge(int var0) {
      String var1 = null;
      switch (var0) {
         case 1:
            var1 = "restart";
            break;
         case 2:
            var1 = "continue";
      }

      return var1;
   }

   private static String margin(String var0, double var1) {
      return "<w:" + var0 + " w:w=\"" + Math.round(20.0 * var1) + "\" w:type=\"dxa\" />";
   }

   private String alignment() {
      String var1;
      switch (this.alignment) {
         case 0:
         default:
            var1 = "top";
            break;
         case 1:
            var1 = "center";
            break;
         case 2:
            var1 = "bottom";
      }

      return var1;
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

      var1 += this.marginLeft + this.marginRight;
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

      var1 += this.marginLeft + this.marginRight;
      ++var1;
      if (!this.relativeWidth && this.width > var1) {
         var1 = this.width;
      }

      return var1;
   }

   public TableCell copy() {
      TableCell var1 = null;

      try {
         var1 = (TableCell)this.clone();
         if (this.borders != null) {
            var1.borders = this.borders.copy();
         }

         var1.content = new Vector();
      } catch (CloneNotSupportedException var3) {
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

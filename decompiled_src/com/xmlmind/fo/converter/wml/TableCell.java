package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class TableCell implements Cloneable {
   public static final String ALIGNMENT_TOP = "top";
   public static final String ALIGNMENT_CENTER = "center";
   public static final String ALIGNMENT_BOTTOM = "bottom";
   public static final String MERGE_START = "restart";
   public static final String MERGE_CONTINUE = "continue";
   public int columnNumber;
   public int columnSpan;
   public int rowSpan;
   public boolean startsRow;
   public boolean endsRow;
   public int width;
   public double percentage;
   public String alignment;
   public Borders borders;
   public Color background;
   public int marginTop;
   public int marginBottom;
   public int marginLeft;
   public int marginRight;
   public int gridSpan;
   public String hMerge;
   public String vMerge;
   private Vector elements;
   private boolean requiresLayout;

   public TableCell() {
      this.elements = new Vector();
      this.columnSpan = 1;
      this.rowSpan = 1;
      this.alignment = "top";
      this.gridSpan = 1;
   }

   public TableCell(Context var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.columnNumber = number(var2[82]);
      this.columnSpan = number(var2[187]);
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
            this.width = Wml.toTwips(var3.length());
            break;
         case 13:
            this.percentage = var3.percentage();
      }

      switch (var2[93].keyword()) {
         case 4:
            this.alignment = "bottom";
            break;
         case 16:
         default:
            this.alignment = "top";
            break;
         case 31:
            this.alignment = "center";
      }

      this.borders = new Borders(var2);
      this.marginTop = this.borders.top.space;
      this.borders.top.space = 0;
      this.marginBottom = this.borders.bottom.space;
      this.borders.bottom.space = 0;
      this.marginLeft = this.borders.left.space;
      this.borders.left.space = 0;
      this.marginRight = this.borders.right.space;
      this.borders.right.space = 0;
      Value var4 = var2[8];
      if (var4.type == 24) {
         this.background = var4.color();
      }

   }

   public void setReference(int var1) {
      if (this.percentage > 0.0) {
         this.width = (int)((double)var1 * this.percentage / 100.0);
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
      this.elements.addElement(new Element(1, var1));
      if (var1.hasPicture) {
         this.requiresLayout = true;
      }

   }

   public void add(Table var1) {
      this.elements.addElement(new Element(2, var1));
      this.requiresLayout = true;
   }

   public void add(TableAndCaption var1) {
      this.elements.addElement(new Element(3, var1));
      this.requiresLayout = true;
   }

   public void keepWithNext() {
      if (this.isEmpty()) {
         Paragraph var3 = Paragraph.empty();
         var3.properties.keepWithNext = true;
         this.add(var3);
      } else {
         Element var1 = (Element)this.elements.elementAt(0);
         switch (var1.type) {
            case 1:
               Paragraph var2 = (Paragraph)var1.object;
               if (var2.properties == null) {
                  var2.properties = new ParProperties();
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
         Element var2 = (Element)this.elements.elementAt(0);
         switch (var2.type) {
            case 1:
               Paragraph var3 = (Paragraph)var2.object;
               if (var3.properties == null) {
                  var3.properties = new ParProperties();
               }

               var3.properties.breakBefore = var1;
            default:
         }
      }
   }

   public boolean requiresLayout() {
      return this.requiresLayout;
   }

   public void layout(int var1) throws Exception {
      if (this.requiresLayout) {
         int var2 = 0;

         for(int var3 = this.elements.size(); var2 < var3; ++var2) {
            Element var4 = (Element)this.elements.elementAt(var2);
            switch (var4.type) {
               case 1:
                  Paragraph var5 = (Paragraph)var4.object;
                  if (var5.hasPicture) {
                     var5.layout(var1);
                  }
                  break;
               case 2:
                  Table var6 = (Table)var4.object;
                  var6.layout(var1);
                  break;
               case 3:
                  TableAndCaption var7 = (TableAndCaption)var4.object;
                  var7.layout(var1);
            }
         }

      }
   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      var1.println("<w:tc>");
      var1.print("<w:tcPr>");
      if (this.width > 0) {
         var1.print("<w:tcW w:w=\"" + this.width + "\" w:type=\"dxa\" />");
      } else if (this.gridSpan > 1) {
         var1.print("<w:gridSpan w:val=\"" + this.gridSpan + "\" />");
      }

      if (this.hMerge != null) {
         var1.print("<w:hmerge w:val=\"" + this.hMerge + "\" />");
      }

      if (this.vMerge != null) {
         var1.print("<w:vmerge w:val=\"" + this.vMerge + "\" />");
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
         if (this.marginTop > 0) {
            var1.print(this.margin("top", this.marginTop));
         }

         if (this.marginLeft > 0) {
            var1.print(this.margin("left", this.marginLeft));
         }

         if (this.marginBottom > 0) {
            var1.print(this.margin("bottom", this.marginBottom));
         }

         if (this.marginRight > 0) {
            var1.print(this.margin("right", this.marginRight));
         }

         var1.print("</w:tcMar>");
      }

      var1.print("<w:vAlign w:val=\"" + this.alignment + "\" />");
      var1.println("</w:tcPr>");
      if (this.isEmpty()) {
         Paragraph.EMPTY.print(var1);
      } else {
         int var3 = 0;

         for(int var4 = this.elements.size(); var3 < var4; ++var3) {
            Element var5 = (Element)this.elements.elementAt(var3);
            switch (var5.type) {
               case 1:
                  Paragraph var6 = (Paragraph)var5.object;
                  var6.print(var1, var2);
                  break;
               case 2:
                  Table var7 = (Table)var5.object;
                  var7.print(var1, var2);
                  break;
               case 3:
                  TableAndCaption var8 = (TableAndCaption)var5.object;
                  var8.print(var1, var2);
            }
         }

         Element var9 = (Element)this.elements.lastElement();
         switch (var9.type) {
            case 2:
               Paragraph.EMPTY.print(var1);
               break;
            case 3:
               TableAndCaption var10 = (TableAndCaption)var9.object;
               if (var10.caption == null || var10.caption.side == 1) {
                  Paragraph.EMPTY.print(var1);
               }
         }
      }

      var1.println("</w:tc>");
   }

   public boolean hasMargin() {
      return this.marginTop > 0 || this.marginBottom > 0 || this.marginLeft > 0 || this.marginRight > 0;
   }

   private String margin(String var1, int var2) {
      return "<w:" + var1 + " w:w=\"" + var2 + "\" w:type=\"dxa\" />";
   }

   public boolean isEmpty() {
      return this.elements.size() == 0;
   }

   public int maxWidth() {
      int var1 = 0;
      int var2 = 0;

      for(int var3 = this.elements.size(); var2 < var3; ++var2) {
         Element var4 = (Element)this.elements.elementAt(var2);
         int var6;
         switch (var4.type) {
            case 1:
               Paragraph var5 = (Paragraph)var4.object;
               var6 = var5.maxWidth();
               if (var6 > var1) {
                  var1 = var6;
               }
               break;
            case 2:
               Table var7 = (Table)var4.object;
               var6 = var7.maxWidth();
               if (var6 > var1) {
                  var1 = var6;
               }
               break;
            case 3:
               TableAndCaption var8 = (TableAndCaption)var4.object;
               var6 = var8.maxWidth();
               if (var6 > var1) {
                  var1 = var6;
               }
         }
      }

      var1 += this.marginLeft;
      var1 += this.marginRight;
      var1 += 28;
      if (this.width > var1) {
         var1 = this.width;
      }

      return var1;
   }

   public int minWidth() {
      int var1 = 0;
      int var2 = 0;

      for(int var3 = this.elements.size(); var2 < var3; ++var2) {
         Element var4 = (Element)this.elements.elementAt(var2);
         int var6;
         switch (var4.type) {
            case 1:
               Paragraph var5 = (Paragraph)var4.object;
               var6 = var5.minWidth();
               if (var6 > var1) {
                  var1 = var6;
               }
               break;
            case 2:
               Table var7 = (Table)var4.object;
               var6 = var7.minWidth();
               if (var6 > var1) {
                  var1 = var6;
               }
               break;
            case 3:
               TableAndCaption var8 = (TableAndCaption)var4.object;
               var6 = var8.minWidth();
               if (var6 > var1) {
                  var1 = var6;
               }
         }
      }

      var1 += this.marginLeft;
      var1 += this.marginRight;
      var1 += 28;
      if (this.width > var1) {
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

         var1.elements = new Vector();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }

   private class Element {
      static final int TYPE_PARAGRAPH = 1;
      static final int TYPE_TABLE = 2;
      static final int TYPE_TABLE_AND_CAPTION = 3;
      int type;
      Object object;

      Element(int var2, Object var3) {
         this.type = var2;
         this.object = var3;
      }
   }
}

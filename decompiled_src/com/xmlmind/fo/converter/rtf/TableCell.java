package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class TableCell implements Cloneable {
   public static final int ALIGNMENT_TOP = 0;
   public static final int ALIGNMENT_CENTER = 1;
   public static final int ALIGNMENT_BOTTOM = 2;
   public static final int MERGE_NONE = 0;
   public static final int MERGE_START = 1;
   public static final int MERGE_CONTINUE = 2;
   public int columnNumber;
   public int columnSpan;
   public int rowSpan;
   public boolean startsRow;
   public boolean endsRow;
   public int width;
   public double percentage;
   public int alignment;
   public Borders borders;
   public int background;
   public int fontSize;
   public int paddingTop;
   public int paddingBottom;
   public int paddingLeft;
   public int paddingRight;
   public int hMerge;
   public int vMerge;
   public int boundary;
   public boolean isNested;
   public int nestingLevel;
   private Vector elements;
   private boolean requiresLayout;

   public TableCell() {
      this.elements = new Vector();
      this.columnSpan = 1;
      this.rowSpan = 1;
      this.alignment = 0;
      this.hMerge = 0;
      this.vMerge = 0;
   }

   public TableCell(Context var1, ColorTable var2) {
      this();
      this.initialize(var1, var2);
   }

   public void initialize(Context var1, ColorTable var2) {
      Value[] var3 = var1.properties.values;
      this.columnNumber = number(var3[82]);
      this.columnSpan = number(var3[187]);
      this.rowSpan = number(var3[188]);
      if (var3[279].keyword() == 209) {
         this.startsRow = true;
      }

      if (var3[98].keyword() == 209) {
         this.endsRow = true;
      }

      Value var4 = var3[308];
      switch (var4.type) {
         case 4:
            this.width = Rtf.toTwips(var4.length());
            break;
         case 13:
            this.percentage = var4.percentage();
      }

      switch (var3[93].keyword()) {
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

      this.borders = new Borders(2);
      this.borders.initialize(var3, var2);
      this.paddingTop = this.borders.top.space;
      this.borders.top.space = 0;
      this.paddingBottom = this.borders.bottom.space;
      this.borders.bottom.space = 0;
      this.paddingLeft = this.borders.left.space;
      this.borders.left.space = 0;
      this.paddingRight = this.borders.right.space;
      this.borders.right.space = 0;
      Value var5 = var3[8];
      if (var5.type == 24) {
         this.background = Rtf.colorIndex(var5.color(), var2);
      }

      double var6 = var3[106].length();
      this.fontSize = (int)Math.round(2.0 * var6);
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
      if (var1.properties == null) {
         var1.properties = new ParProperties();
      }

      var1.properties.isInTable = true;
      var1.properties.nestingLevel = this.nestingLevel;
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

   public boolean isEmpty() {
      return this.elements.size() == 0;
   }

   public void close() {
      Paragraph var1;
      if (this.isEmpty()) {
         var1 = Paragraph.empty();
         var1.isLast = true;
         this.add(var1);
      } else {
         Element var2 = (Element)this.elements.lastElement();
         switch (var2.type) {
            case 1:
               var1 = (Paragraph)var2.object;
               var1.isLast = true;
               break;
            case 2:
               var1 = Paragraph.empty();
               var1.isLast = true;
               this.add(var1);
               break;
            case 3:
               TableAndCaption var3 = (TableAndCaption)var2.object;
               Caption var4 = var3.caption;
               if (var4 != null && var4.side == 2) {
                  Paragraph var5 = var4.lastParagraph();
                  if (var5 != null) {
                     var5.isLast = true;
                     return;
                  }
               }

               var1 = Paragraph.empty();
               var1.isLast = true;
               this.add(var1);
         }
      }

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
               var3.properties.breakBefore = var1;
            default:
         }
      }
   }

   public Paragraph lastParagraph() {
      Paragraph var1 = null;

      for(int var2 = this.elements.size() - 1; var2 >= 0; --var2) {
         Element var3 = (Element)this.elements.elementAt(var2);
         if (var3.type != 1) {
            if (var3.type == 3) {
               TableAndCaption var6 = (TableAndCaption)var3.object;
               Caption var5 = var6.caption;
               if (var5 != null && var5.side == 2) {
                  var1 = var5.lastParagraph();
               }
            }
            break;
         }

         Paragraph var4 = (Paragraph)var3.object;
         if (!var4.isVoid()) {
            var1 = var4;
            break;
         }
      }

      return var1;
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

   void printDefinition(PrintWriter var1, int var2) {
      int var3 = this.paddingTop;
      int var4 = this.paddingLeft;
      switch (this.hMerge) {
         case 1:
            var1.println("\\clmgf");
            break;
         case 2:
            var1.println("\\clmrg");
      }

      switch (this.vMerge) {
         case 1:
            var1.println("\\clvmgf");
            break;
         case 2:
            var1.println("\\clvmrg");
      }

      if (var2 == 1) {
         var3 = this.paddingLeft;
         var4 = this.paddingTop;
      }

      if (var3 > 0) {
         var1.println("\\clpadft3\\clpadt" + var3);
      }

      if (this.paddingBottom > 0) {
         var1.println("\\clpadfb3\\clpadb" + this.paddingBottom);
      }

      if (var4 > 0) {
         var1.println("\\clpadfl3\\clpadl" + var4);
      }

      if (this.paddingRight > 0) {
         var1.println("\\clpadfr3\\clpadr" + this.paddingRight);
      }

      switch (this.alignment) {
         case 0:
            var1.println("\\clvertalt");
            break;
         case 1:
            var1.println("\\clvertalc");
            break;
         case 2:
            var1.println("\\clvertalb");
      }

      if (this.background > 0) {
         var1.println("\\clcbpat" + this.background);
      }

      if (this.borders != null && this.borders.materialized()) {
         this.borders.print(var1);
      }

      var1.println("\\cellx" + this.boundary);
   }

   void printContent(PrintWriter var1, Encoder var2, int var3) throws Exception {
      if (this.isEmpty()) {
         this.close();
      }

      int var4 = this.elements.size();
      Element var5;
      if (var4 == 1 && (var5 = (Element)this.elements.elementAt(0)).type == 1) {
         Paragraph var6 = (Paragraph)var5.object;
         if (var6.isVoid()) {
            RunProperties var7 = new RunProperties();
            var7.fontSize = this.fontSize;
            var6.properties.markProperties = var7;
         }
      }

      for(int var10 = 0; var10 < var4; ++var10) {
         var5 = (Element)this.elements.elementAt(var10);
         switch (var5.type) {
            case 1:
               Paragraph var11 = (Paragraph)var5.object;
               var11.print(var1, var2);
               break;
            case 2:
               Table var8 = (Table)var5.object;
               var8.print(var1, var2, var3);
               break;
            case 3:
               TableAndCaption var9 = (TableAndCaption)var5.object;
               var9.print(var1, var2, var3);
         }
      }

      if (this.isNested) {
         var1.println("\\nestcell");
      } else {
         var1.println("\\cell");
      }

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

      var1 += this.paddingLeft;
      var1 += this.paddingRight;
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

      var1 += this.paddingLeft;
      var1 += this.paddingRight;
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

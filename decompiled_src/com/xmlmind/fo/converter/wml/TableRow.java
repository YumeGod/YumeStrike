package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class TableRow {
   public int breakBefore;
   public int breakAfter;
   public boolean keepTogether;
   public boolean keepWithNext;
   public boolean keepWithPrevious;
   public int height;
   public Borders borders;
   public Color background;
   public Bookmark[] bookmarks;
   public boolean isHeaderRow;
   public Vector cells;

   public TableRow() {
      this.cells = new Vector();
      this.breakBefore = 0;
      this.breakAfter = 0;
   }

   public TableRow(Context var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var3 = var1.properties.values;
      this.breakBefore = var1.properties.breakBefore();
      this.breakAfter = var1.properties.breakAfter();
      this.keepTogether = keep(var3[137]);
      this.keepWithNext = keep(var3[141]);
      this.keepWithPrevious = keep(var3[145]);
      Value var2 = var3[118];
      if (var2.type == 4) {
         this.height = Wml.toTwips(var2.length());
      }

      this.borders = new Borders(var3);
      var2 = var3[8];
      if (var2.type == 24) {
         this.background = var2.color();
      }

   }

   private static boolean keep(Value var0) {
      boolean var1 = false;
      switch (var0.type) {
         case 1:
            if (var0.keyword() == 8) {
               var1 = true;
            }
            break;
         case 2:
            if (var0.integer() > 0) {
               var1 = true;
            }
      }

      return var1;
   }

   public void add(TableCell var1) {
      this.cells.addElement(var1);
   }

   public int size() {
      return this.cells.size();
   }

   public TableCell get(int var1) {
      return (TableCell)this.cells.elementAt(var1);
   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      int var3;
      if (this.bookmarks != null) {
         for(var3 = 0; var3 < this.bookmarks.length; ++var3) {
            this.bookmarks[var3].start(var1, var2);
         }
      }

      var1.println("<w:tr>");
      var1.print("<w:tblPrEx>");
      if (this.borders != null && this.borders.materialized()) {
         var1.print("<w:tblBorders>");
         this.borders.print(var1);
         var1.print("</w:tblBorders>");
      }

      if (this.background != null) {
         var1.print("<w:shd");
         var1.print(" w:val=\"clear\"");
         var1.print(" w:fill=\"" + Wml.hexColorType(this.background) + "\"");
         var1.print(" />");
      }

      var1.println("</w:tblPrEx>");
      var1.print("<w:trPr>");
      if (this.keepTogether) {
         var1.print("<w:cantSplit />");
      }

      if (this.height > 0) {
         var1.print("<w:trHeight");
         var1.print(" w:val=\"" + this.height + "\"");
         var1.print(" w:h-rule=\"at-least\"");
         var1.print(" />");
      }

      if (this.isHeaderRow) {
         var1.print("<w:tblHeader />");
      }

      var1.println("</w:trPr>");
      var3 = 0;

      for(int var4 = this.cells.size(); var3 < var4; ++var3) {
         TableCell var5 = (TableCell)this.cells.elementAt(var3);
         var5.print(var1, var2);
      }

      var1.println("</w:tr>");
      if (this.bookmarks != null) {
         for(var3 = this.bookmarks.length - 1; var3 >= 0; --var3) {
            this.bookmarks[var3].end(var1);
         }
      }

   }
}

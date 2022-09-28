package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;
import java.util.Vector;

public final class TableRow {
   public int breakBefore;
   public int breakAfter;
   public boolean keepTogether;
   public boolean keepWithNext;
   public boolean keepWithPrevious;
   public double height;
   public Borders borders;
   public Color background;
   public boolean requiresLayout;
   public boolean isHeaderRow;
   public Bookmark[] bookmarks;
   public TableCell[] cells;
   private Vector list = new Vector();

   public TableRow() {
      this.breakBefore = 0;
      this.breakAfter = 0;
      this.borders = new Borders();
   }

   public TableRow(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.breakBefore = var1.properties.breakBefore();
      this.breakAfter = var1.properties.breakAfter();
      this.keepTogether = keep(var2[137]);
      this.keepWithNext = keep(var2[141]);
      this.keepWithPrevious = keep(var2[145]);
      Value var3 = var2[118];
      if (var3.type == 4) {
         this.height = var3.length();
      }

      this.borders = new Borders(var2);
      Value var4 = var2[8];
      if (var4.type == 24) {
         this.background = var4.color();
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
      this.list.addElement(var1);
   }

   public int size() {
      return this.list.size();
   }

   public TableCell get(int var1) {
      return (TableCell)this.list.elementAt(var1);
   }

   public int span() {
      int var1 = 1;
      int var2 = 0;

      for(int var3 = this.size(); var2 < var3; ++var2) {
         TableCell var4 = this.get(var2);
         if (var4.rowSpan > var1) {
            var1 = var4.rowSpan;
         }
      }

      return var1;
   }

   public void normalize(int var1) {
      this.cells = new TableCell[var1];
      int var2 = 0;

      for(int var4 = this.size(); var2 < var4; ++var2) {
         TableCell var5 = this.get(var2);
         int var6 = var5.colNumber - 1;
         this.cells[var6] = var5;
         if (var5.colSpan > 1) {
            int var7 = var6 + var5.colSpan - 1;

            for(int var3 = var6 + 1; var3 <= var7; ++var3) {
               this.cells[var3] = var5.copy();
               this.cells[var3].colNumber = var3 + 1;
               this.cells[var3].colSpan = 1;
               this.cells[var3].hMerge = 2;
               this.cells[var3].borders.left.style = 75;
               this.cells[var3].borders.left.width = 0.0;
               this.cells[var3].marginLeft = 0.0;
               if (var3 < var7) {
                  this.cells[var3].borders.right.style = 75;
                  this.cells[var3].borders.right.width = 0.0;
                  this.cells[var3].marginRight = 0.0;
               }
            }

            var5.hMerge = 1;
            var5.borders.right.style = 75;
            var5.borders.right.width = 0.0;
            var5.marginRight = 0.0;
         }
      }

      for(var2 = 0; var2 < var1; ++var2) {
         if (this.cells[var2] == null) {
            this.cells[var2] = new TableCell(var2 + 1);
            this.cells[var2].isInvisible = true;
         }
      }

   }

   public void print(PrintWriter var1) {
      int var2;
      if (this.bookmarks != null) {
         for(var2 = 0; var2 < this.bookmarks.length; ++var2) {
            this.bookmarks[var2].start(var1);
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

      if (this.height > 0.0) {
         var1.print("<w:trHeight");
         var1.print(" w:val=\"" + Math.round(20.0 * this.height) + "\"");
         var1.print(" w:hRule=\"atLeast\"");
         var1.print(" />");
      }

      if (this.isHeaderRow) {
         var1.print("<w:tblHeader />");
      }

      var1.println("</w:trPr>");

      for(var2 = 0; var2 < this.cells.length; ++var2) {
         this.cells[var2].print(var1);
      }

      var1.println("</w:tr>");
      if (this.bookmarks != null) {
         for(var2 = this.bookmarks.length - 1; var2 >= 0; --var2) {
            this.bookmarks[var2].end(var1);
         }
      }

   }
}

package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.Context;
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
   public int background;
   public Bookmark[] bookmarks;
   public int indent;
   public int rowSpacing;
   public int columnSpacing;
   public boolean isHeaderRow;
   public boolean isNested;
   public int nestingLevel;
   public Vector cells;

   public TableRow() {
      this.cells = new Vector();
      this.breakBefore = 0;
      this.breakAfter = 0;
   }

   public TableRow(Context var1, ColorTable var2) {
      this();
      this.initialize(var1, var2);
   }

   public void initialize(Context var1, ColorTable var2) {
      Value[] var4 = var1.properties.values;
      this.breakBefore = var1.properties.breakBefore();
      this.breakAfter = var1.properties.breakAfter();
      this.keepTogether = keep(var4[137]);
      this.keepWithNext = keep(var4[141]);
      this.keepWithPrevious = keep(var4[145]);
      Value var3 = var4[118];
      if (var3.type == 4) {
         this.height = Rtf.toTwips(var3.length());
      }

      this.borders = new Borders(1);
      this.borders.initialize(var4, var2);
      var3 = var4[8];
      if (var3.type == 24) {
         this.background = Rtf.colorIndex(var3.color(), var2);
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
      if (this.isNested) {
         var1.isNested = true;
         var1.nestingLevel = this.nestingLevel;
      }

      this.cells.addElement(var1);
   }

   public int size() {
      return this.cells.size();
   }

   public TableCell get(int var1) {
      return (TableCell)this.cells.elementAt(var1);
   }

   void print(PrintWriter var1, Encoder var2, int var3) throws Exception {
      int var4;
      if (this.bookmarks != null) {
         for(var4 = 0; var4 < this.bookmarks.length; ++var4) {
            this.bookmarks[var4].start(var1, var2);
         }
      }

      if (!this.isNested) {
         this.printDefinition(var1, var3);
      }

      var4 = 0;

      for(int var5 = this.cells.size(); var4 < var5; ++var4) {
         TableCell var6 = (TableCell)this.cells.elementAt(var4);
         var6.printContent(var1, var2, var3);
      }

      if (this.isNested) {
         var1.println("{\\*\\nesttableprops");
         this.printDefinition(var1, var3);
         var1.println("\\nestrow}");
      } else {
         var1.println("\\row");
      }

      if (this.bookmarks != null) {
         for(var4 = this.bookmarks.length - 1; var4 >= 0; --var4) {
            this.bookmarks[var4].end(var1, var2);
         }
      }

   }

   private void printDefinition(PrintWriter var1, int var2) {
      var1.print("\\trowd");
      var1.print("\\trleft" + this.indent);
      if (this.height > 0) {
         var1.print("\\trrh" + this.height);
      }

      if (this.isHeaderRow) {
         var1.print("\\trhdr");
      }

      if (this.keepTogether) {
         var1.print("\\trkeep");
      }

      if (this.keepWithNext) {
         var1.print("\\trkeepfollow");
      }

      var1.println();
      if (this.borders != null && this.borders.materialized()) {
         this.borders.print(var1);
      }

      int var3;
      if (this.rowSpacing > 0) {
         var3 = this.rowSpacing / 2;
         var1.print("\\trspdft3\\trspdt" + var3);
         var1.println("\\trspdfb3\\trspdb" + var3);
      }

      if (this.columnSpacing > 0) {
         var3 = this.columnSpacing / 2;
         var1.print("\\trspdfl3\\trspdl" + var3);
         var1.println("\\trspdfr3\\trspdr" + var3);
      }

      var3 = 0;

      for(int var4 = this.cells.size(); var3 < var4; ++var3) {
         TableCell var5 = (TableCell)this.cells.elementAt(var3);
         var5.printDefinition(var1, var2);
      }

   }
}

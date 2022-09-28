package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;
import java.util.Vector;

public class TableGroup {
   public static final int TYPE_HEADER = 0;
   public static final int TYPE_BODY = 1;
   public static final int TYPE_FOOTER = 2;
   public int type;
   public Borders borders;
   public Color background;
   public TableRow[] rows;
   public Vector list;

   public TableGroup() {
      this(1);
   }

   public TableGroup(int var1) {
      this.list = new Vector();
      this.type = var1;
   }

   public TableGroup(Context var1) {
      this(1, var1);
   }

   public TableGroup(int var1, Context var2) {
      this(var1);
      this.initialize(var2);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.borders = new Borders(var2);
      Value var3 = var2[8];
      if (var3.type == 24) {
         this.background = var3.color();
      }

   }

   public void add(TableRow var1) {
      this.list.addElement(var1);
      if (var1.background == null) {
         var1.background = this.background;
      }

      if (this.type == 0) {
         var1.isHeaderRow = true;
      }

   }

   public int size() {
      return this.list.size();
   }

   public TableRow get(int var1) {
      return (TableRow)this.list.elementAt(var1);
   }

   public void normalize(int var1) {
      int var6 = 0;
      int var2 = 0;

      int var3;
      for(int var5 = this.size(); var2 < var5; ++var2) {
         TableRow var7 = this.get(var2);
         var3 = var2 + var7.span() - 1;
         if (var3 > var6) {
            var6 = var3;
         }
      }

      for(var2 = this.size(); var2 <= var6; ++var2) {
         this.add(new TableRow());
      }

      this.rows = new TableRow[this.size()];

      for(var2 = 0; var2 < this.rows.length; ++var2) {
         this.rows[var2] = this.get(var2);
         this.rows[var2].normalize(var1);
      }

      for(var2 = 0; var2 < this.rows.length; ++var2) {
         for(var3 = 0; var3 < var1; ++var3) {
            TableCell var9 = this.rows[var2].cells[var3];
            if (var9.rowSpan > 1) {
               var6 = var2 + var9.rowSpan - 1;

               for(int var4 = var2 + 1; var4 <= var6; ++var4) {
                  TableCell var8 = var9.copy();
                  var8.rowSpan = 1;
                  var8.vMerge = 2;
                  var8.borders.top.style = 75;
                  var8.borders.top.width = 0.0;
                  var8.marginTop = 0.0;
                  if (var4 < var6) {
                     var8.borders.bottom.style = 75;
                     var8.borders.bottom.width = 0.0;
                     var8.marginBottom = 0.0;
                  }

                  this.rows[var4].cells[var3] = var8;
               }

               var9.vMerge = 1;
               var9.borders.bottom.style = 75;
               var9.borders.bottom.width = 0.0;
               var9.marginBottom = 0.0;
            }
         }
      }

   }

   public void print(PrintWriter var1) {
      for(int var2 = 0; var2 < this.rows.length; ++var2) {
         this.rows[var2].print(var1);
      }

   }
}

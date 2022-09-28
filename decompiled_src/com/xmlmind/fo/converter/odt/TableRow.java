package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class TableRow {
   public Borders borders;
   public TableRowStyle style;
   public TableCell[] cells;
   public boolean requiresLayout;
   private Vector list = new Vector();

   public TableRow() {
      this.borders = new Borders();
      this.style = new TableRowStyle();
   }

   public TableRow(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.borders = new Borders(var2);
      this.style = new TableRowStyle(var1);
   }

   public boolean hasBackground() {
      return this.style.background != -1;
   }

   public void setBackground(Color var1) {
      this.style.background = Odt.rgb(var1);
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
               this.cells[var3] = new TableCell(var3 + 1);
               this.cells[var3].rowSpan = var5.rowSpan;
               this.cells[var3].isCovered = true;
            }
         }
      }

      for(var2 = 0; var2 < var1; ++var2) {
         if (this.cells[var2] == null) {
            this.cells[var2] = new TableCell(var2 + 1);
         }
      }

   }

   public void print(PrintWriter var1, Encoder var2) {
      var1.print("<table:table-row");
      var1.print(" table:style-name=\"" + this.style.name + "\"");
      var1.println(">");

      for(int var3 = 0; var3 < this.cells.length; ++var3) {
         this.cells[var3].print(var1, var2);
      }

      var1.print("</table:table-row>");
   }
}

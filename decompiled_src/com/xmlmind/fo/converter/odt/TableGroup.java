package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class TableGroup {
   public static final int TYPE_HEADER = 0;
   public static final int TYPE_BODY = 1;
   public static final int TYPE_FOOTER = 2;
   public int type;
   public Borders borders;
   public Color background;
   public TableRow[] rows;
   private Vector list;

   public TableGroup(Context var1) {
      this(1, var1);
   }

   public TableGroup(int var1, Context var2) {
      this.list = new Vector();
      this.type = var1;
      this.initialize(var2);
   }

   private void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.borders = new Borders(var2);
      Value var3 = var2[8];
      if (var3.type == 24) {
         this.background = var3.color();
      }

   }

   public void add(TableRow var1) {
      this.list.addElement(var1);
      if (this.background != null && !var1.hasBackground()) {
         var1.setBackground(this.background);
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
                  TableCell var8 = this.rows[var4].cells[var3];
                  var8.colSpan = var9.colSpan;
                  var8.isCovered = true;
               }
            }
         }
      }

   }

   public void print(PrintWriter var1, Encoder var2) {
      for(int var3 = 0; var3 < this.rows.length; ++var3) {
         this.rows[var3].print(var1, var2);
      }

   }
}

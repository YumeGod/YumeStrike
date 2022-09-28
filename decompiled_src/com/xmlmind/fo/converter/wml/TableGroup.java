package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public class TableGroup {
   public Borders borders;
   public Color background;
   public boolean isHeader;
   public Vector rows;

   public TableGroup() {
      this(false);
   }

   public TableGroup(boolean var1) {
      this.rows = new Vector();
      this.isHeader = var1;
   }

   public TableGroup(Context var1) {
      this(false, var1);
   }

   public TableGroup(boolean var1, Context var2) {
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
      this.rows.addElement(var1);
      if (this.isHeader) {
         var1.isHeaderRow = true;
      }

   }

   public int size() {
      return this.rows.size();
   }

   public TableRow get(int var1) {
      return (TableRow)this.rows.elementAt(var1);
   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      int var3 = 0;

      for(int var4 = this.rows.size(); var3 < var4; ++var3) {
         TableRow var5 = (TableRow)this.rows.elementAt(var3);
         var5.print(var1, var2);
      }

   }
}

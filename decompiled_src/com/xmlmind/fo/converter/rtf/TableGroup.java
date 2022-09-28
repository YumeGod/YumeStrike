package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public class TableGroup {
   public Borders borders;
   public int background;
   public boolean isHeader;
   public Vector rows;

   public TableGroup() {
      this(false);
   }

   public TableGroup(boolean var1) {
      this.rows = new Vector();
      this.isHeader = var1;
   }

   public TableGroup(Context var1, ColorTable var2) {
      this(false, var1, var2);
   }

   public TableGroup(boolean var1, Context var2, ColorTable var3) {
      this(var1);
      this.initialize(var2, var3);
   }

   public void initialize(Context var1, ColorTable var2) {
      Value[] var3 = var1.properties.values;
      this.borders = new Borders(var3, var2);
      Value var4 = var3[8];
      if (var4.type == 24) {
         this.background = Rtf.colorIndex(var4.color(), var2);
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

   public void print(PrintWriter var1, Encoder var2, int var3) throws Exception {
      int var4 = 0;

      for(int var5 = this.rows.size(); var4 < var5; ++var4) {
         TableRow var6 = (TableRow)this.rows.elementAt(var4);
         var6.print(var1, var2, var3);
      }

   }
}

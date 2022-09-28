package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;

public class TableColumn implements Cloneable {
   public static final int WIDTH_TYPE_ABSOLUTE = 0;
   public static final int WIDTH_TYPE_RELATIVE = 1;
   public static final int WIDTH_TYPE_PROPORTIONAL = 2;
   public int number;
   public int span;
   public int repeat;
   public double width;
   public int widthType;
   public Borders borders;
   public Color background;

   public TableColumn(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.number = number(var2[82]);
      this.span = number(var2[187]);
      this.repeat = number(var2[186]);
      Value var3;
      if (this.span == 1) {
         var3 = var2[83];
         if (var3 != null) {
            switch (var3.type) {
               case 4:
                  this.width = var3.length();
                  break;
               case 13:
                  this.width = var3.percentage();
                  this.widthType = 1;
                  break;
               case 29:
                  this.width = var3.proportionalColumnWidth();
                  this.widthType = 2;
            }
         }
      }

      this.borders = new Borders(var2);
      var3 = var2[8];
      if (var3.type == 24) {
         this.background = var3.color();
      }

   }

   private static int number(Value var0) {
      int var1 = (int)Math.round(var0.number());
      if (var1 < 1) {
         var1 = 1;
      }

      return var1;
   }

   public int last() {
      return this.number + this.span * this.repeat - 1;
   }

   public TableColumn copy() {
      TableColumn var1 = null;

      try {
         var1 = (TableColumn)this.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }
}

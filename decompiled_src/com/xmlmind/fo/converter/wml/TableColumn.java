package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;

public class TableColumn {
   public int number;
   public int repeat;
   public int span;
   public int width;
   public double percentage;
   public double proportion;
   public Borders borders;
   public Color background;

   public TableColumn() {
   }

   public TableColumn(Context var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.number = number(var2[82]);
      this.repeat = number(var2[186]);
      this.span = number(var2[187]);
      Value var3 = var2[83];
      if (var3 != null) {
         switch (var3.type) {
            case 4:
               this.width = Wml.toTwips(var3.length());
               break;
            case 13:
               this.percentage = var3.percentage();
               break;
            case 29:
               this.proportion = var3.proportionalColumnWidth();
         }
      }

      this.borders = new Borders(var2);
      Value var4 = var2[8];
      if (var4.type == 24) {
         this.background = var4.color();
      }

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
}

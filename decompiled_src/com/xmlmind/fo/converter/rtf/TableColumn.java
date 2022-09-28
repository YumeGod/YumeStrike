package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;

public class TableColumn {
   public int number;
   public int repeat;
   public int span;
   public int width;
   public double percentage;
   public double proportion;
   public Borders borders;
   public int background;

   public TableColumn() {
   }

   public TableColumn(Context var1, ColorTable var2) {
      this();
      this.initialize(var1, var2);
   }

   public void initialize(Context var1, ColorTable var2) {
      Value[] var3 = var1.properties.values;
      this.number = number(var3[82]);
      this.repeat = number(var3[186]);
      this.span = number(var3[187]);
      Value var4 = var3[83];
      if (var4 != null) {
         switch (var4.type) {
            case 4:
               this.width = Rtf.toTwips(var4.length());
               break;
            case 13:
               this.percentage = var4.percentage();
               break;
            case 29:
               this.proportion = var4.proportionalColumnWidth();
         }
      }

      this.borders = new Borders(var3, var2);
      Value var5 = var3[8];
      if (var5.type == 24) {
         this.background = Rtf.colorIndex(var5.color(), var2);
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

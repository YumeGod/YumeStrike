package com.xmlmind.fo.objects;

import com.xmlmind.fo.properties.Value;

public class TableCell {
   public Value[] properties;
   public int columnNumber;
   public int numberColumnsSpanned;
   public int numberRowsSpanned;
   public boolean startsRow;
   public boolean endsRow;

   public TableCell(Value[] var1) {
      this.properties = var1;
      Value var2 = var1[82];
      this.columnNumber = (int)Math.round(var2.number());
      if (this.columnNumber < 1) {
         this.columnNumber = 1;
      }

      var2 = var1[187];
      this.numberColumnsSpanned = (int)Math.round(var2.number());
      if (this.numberColumnsSpanned < 1) {
         this.numberColumnsSpanned = 1;
      }

      var2 = var1[188];
      this.numberRowsSpanned = (int)Math.round(var2.number());
      if (this.numberRowsSpanned < 1) {
         this.numberRowsSpanned = 1;
      }

      var2 = var1[279];
      if (var2.keyword() == 209) {
         this.startsRow = true;
      }

      var2 = var1[98];
      if (var2.keyword() == 209) {
         this.endsRow = true;
      }

   }
}

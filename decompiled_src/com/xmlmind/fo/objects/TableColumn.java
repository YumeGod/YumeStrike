package com.xmlmind.fo.objects;

import com.xmlmind.fo.properties.Value;

public class TableColumn {
   public Value[] properties;
   public int columnNumber;
   public int numberColumnsRepeated;
   public int numberColumnsSpanned;

   public TableColumn(Value[] var1) {
      this.properties = var1;
      Value var2 = var1[82];
      this.columnNumber = (int)Math.round(var2.number());
      if (this.columnNumber < 1) {
         this.columnNumber = 1;
      }

      var2 = var1[186];
      this.numberColumnsRepeated = (int)Math.round(var2.number());
      if (this.numberColumnsRepeated < 1) {
         this.numberColumnsRepeated = 1;
      }

      var2 = var1[187];
      this.numberColumnsSpanned = (int)Math.round(var2.number());
      if (this.numberColumnsSpanned < 1) {
         this.numberColumnsSpanned = 1;
      }

   }
}

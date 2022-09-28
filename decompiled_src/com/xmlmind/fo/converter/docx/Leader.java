package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;

public final class Leader {
   public int pattern;
   public int style;
   public double position;
   public int align;
   public RunProperties properties;

   public Leader(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.pattern = var2[153].keyword();
      this.style = var2[237].keyword();
      if (var2[319] != null) {
         this.position = var2[319].length();
      } else {
         this.position = Double.MAX_VALUE;
      }

      if (var2[320] != null) {
         this.align = var2[320].keyword();
      } else {
         this.align = 100;
      }

   }

   public TabStop tabStop() {
      byte var1 = 0;
      switch (this.pattern) {
         case 48:
            var1 = 2;
            break;
         case 171:
            switch (this.style) {
               case 42:
               case 49:
                  var1 = 2;
               case 125:
                  break;
               default:
                  var1 = 1;
            }
      }

      byte var2 = 0;
      switch (this.align) {
         case 31:
            var2 = 1;
            break;
         case 165:
            var2 = 2;
            break;
         case 245:
            var2 = 3;
      }

      return new TabStop(this.position, var2, var1);
   }
}

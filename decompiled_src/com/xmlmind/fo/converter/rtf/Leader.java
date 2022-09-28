package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;

public final class Leader {
   public int pattern;
   public int style;
   public double position;
   public int align;

   public Leader(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.pattern = var2[153].keyword();
      this.style = var2[237].keyword();
      if (var2[319] != null) {
         this.position = var2[319].length();
      }

      if (var2[320] != null) {
         this.align = var2[320].keyword();
      } else {
         this.align = 100;
      }

   }

   public ParProperties.Tab Tab() {
      int var1 = Rtf.toTwips(this.position);
      byte var2 = 0;
      switch (this.pattern) {
         case 48:
            var2 = 1;
            break;
         case 171:
            switch (this.style) {
               case 42:
               case 49:
                  var2 = 1;
               case 125:
                  break;
               default:
                  var2 = 2;
            }
      }

      byte var3 = 0;
      switch (this.align) {
         case 31:
            var3 = 2;
            break;
         case 165:
            var3 = 1;
            break;
         case 245:
            var3 = 3;
      }

      return new ParProperties.Tab(var1, var2, var3);
   }
}

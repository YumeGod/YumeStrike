package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;

public final class Leader {
   public int pattern;
   public int style;
   public double thickness;
   public double position;
   public int align;
   public TextStyle textStyle;

   public Leader(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.pattern = var2[153].keyword();
      this.style = var2[237].keyword();
      this.thickness = var2[238].length();
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
      TabStop var1 = new TabStop(this.position);
      switch (this.pattern) {
         case 48:
            var1.leaderType = 1;
            var1.leaderStyle = 2;
            break;
         case 171:
            switch (this.style) {
               case 42:
                  var1.leaderType = 1;
                  var1.leaderStyle = 3;
                  var1.leaderWidth = this.thickness;
                  break;
               case 49:
                  var1.leaderType = 1;
                  var1.leaderStyle = 2;
                  var1.leaderWidth = this.thickness;
                  break;
               case 50:
                  var1.leaderType = 2;
                  var1.leaderStyle = 1;
                  var1.leaderWidth = this.thickness;
                  break;
               case 187:
                  var1.leaderType = 1;
                  var1.leaderStyle = 1;
                  var1.leaderWidth = this.thickness;
            }
      }

      var1.alignment = 0;
      switch (this.align) {
         case 31:
            var1.alignment = 1;
            break;
         case 165:
            var1.alignment = 2;
            break;
         case 245:
            var1.alignment = 3;
      }

      return var1;
   }
}

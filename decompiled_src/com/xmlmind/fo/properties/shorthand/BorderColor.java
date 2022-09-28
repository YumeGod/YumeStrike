package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class BorderColor extends Shorthand {
   private static final int TOP = 0;
   private static final int RIGHT = 1;
   private static final int BOTTOM = 2;
   private static final int LEFT = 3;

   public BorderColor(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{65, 50, 34, 46};
   }

   protected Value list(String var1) {
      Value[] var3 = new Value[4];
      StringTokenizer var4 = new StringTokenizer(var1);

      for(int var5 = 0; var5 < 4; ++var5) {
         int var6 = this.simpleProperties[var5];
         var3[var5] = Property.list[var6].initialValue;
      }

      if (!var4.hasMoreTokens()) {
         return null;
      } else {
         var1 = var4.nextToken();
         int var2;
         if ((var2 = Keyword.index(var1)) >= 0) {
            if (var2 != 206) {
               return null;
            }

            var3[0] = new Value((byte)1, var2);
            var3[1] = var3[0];
            var3[2] = var3[0];
            var3[3] = var3[0];
         } else {
            var3[0] = this.color(var1);
            if (var3[0] == null) {
               return null;
            }

            var3[1] = var3[0];
            var3[2] = var3[0];
            var3[3] = var3[0];
            if (var4.hasMoreTokens()) {
               var3[1] = this.color(var4.nextToken());
               if (var3[1] == null) {
                  return null;
               }

               var3[3] = var3[1];
               if (var4.hasMoreTokens()) {
                  var3[2] = this.color(var4.nextToken());
                  if (var3[2] == null) {
                     return null;
                  }

                  if (var4.hasMoreTokens()) {
                     var3[3] = this.color(var4.nextToken());
                     if (var3[3] == null) {
                        return null;
                     }
                  }
               }
            }
         }

         return new Value((byte)27, var3);
      }
   }
}

package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class Padding extends Shorthand {
   private static final int TOP = 0;
   private static final int RIGHT = 1;
   private static final int BOTTOM = 2;
   private static final int LEFT = 3;

   public Padding(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{208, 204, 199, 203};
   }

   protected Value list(String var1) {
      Value[] var2 = new Value[4];
      StringTokenizer var3 = new StringTokenizer(var1);

      for(int var4 = 0; var4 < 4; ++var4) {
         int var5 = this.simpleProperties[var4];
         var2[var4] = Property.list[var5].initialValue;
      }

      if (!var3.hasMoreTokens()) {
         return null;
      } else {
         var2[0] = this.padding(var3.nextToken());
         if (var2[0] == null) {
            return null;
         } else {
            var2[2] = var2[0];
            var2[3] = var2[0];
            var2[1] = var2[0];
            if (var3.hasMoreTokens()) {
               var2[1] = this.padding(var3.nextToken());
               if (var2[1] == null) {
                  return null;
               }

               var2[3] = var2[1];
               if (var3.hasMoreTokens()) {
                  var2[2] = this.padding(var3.nextToken());
                  if (var2[2] == null) {
                     return null;
                  }

                  if (var3.hasMoreTokens()) {
                     var2[3] = this.padding(var3.nextToken());
                     if (var2[3] == null) {
                        return null;
                     }
                  }
               }
            }

            return new Value((byte)27, var2);
         }
      }
   }

   private Value padding(String var1) {
      Value var2 = this.length(var1);
      if (var2 == null) {
         var2 = this.percentage(var1);
         if (var2 == null) {
            var2 = this.expression(var1);
         }
      }

      return var2;
   }
}

package com.xmlmind.fo.properties;

import java.util.StringTokenizer;

public class TextShadow extends Property {
   public TextShadow(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value list(String var1) {
      StringTokenizer var5 = new StringTokenizer(var1, ",");
      int var2;
      if ((var2 = var5.countTokens()) == 0) {
         return null;
      } else {
         Value[] var4 = new Value[var2];

         for(int var6 = 0; var6 < var2; ++var6) {
            double var11 = 0.0;
            Color var13 = null;
            StringTokenizer var14 = new StringTokenizer(var5.nextToken());
            if (var14.countTokens() < 2) {
               return null;
            }

            var1 = var14.nextToken();
            var13 = Color.parse(var1);
            if (var13 != null) {
               var1 = var14.nextToken();
            }

            Value var3;
            if ((var3 = this.length(var1)) == null) {
               return null;
            }

            double var7 = var3.length();
            if (!var14.hasMoreTokens()) {
               return null;
            }

            if ((var3 = this.length(var14.nextToken())) == null) {
               return null;
            }

            double var9 = var3.length();
            if (var14.hasMoreTokens()) {
               var1 = var14.nextToken();
               if ((var3 = this.length(var1)) != null) {
                  var11 = var3.length();
                  if (var14.hasMoreTokens()) {
                     var1 = var14.nextToken();
                  } else {
                     var1 = null;
                  }
               }

               if (var1 != null) {
                  var13 = Color.parse(var1);
                  if (var13 == null) {
                     return null;
                  }
               }
            }

            var4[var6] = new Value((byte)25, new Shadow(var7, var9, var11, var13));
         }

         return new Value((byte)27, var4);
      }
   }
}

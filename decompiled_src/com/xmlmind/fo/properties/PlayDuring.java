package com.xmlmind.fo.properties;

import java.util.StringTokenizer;

public class PlayDuring extends Property {
   public PlayDuring(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value list(String var1) {
      StringTokenizer var4 = new StringTokenizer(var1);
      int var2;
      if ((var2 = var4.countTokens()) == 0) {
         return null;
      } else {
         Value[] var3 = new Value[var2];
         var3[0] = this.uriSpecification(var4.nextToken());
         if (var3[0] == null) {
            return null;
         } else {
            for(int var5 = 1; var5 < var3.length; ++var5) {
               var1 = var4.nextToken();
               if (var1.equals("mix")) {
                  var3[var5] = new Value((byte)1, 122);
               } else {
                  if (!var1.equals("repeat")) {
                     return null;
                  }

                  var3[var5] = new Value((byte)1, 156);
               }
            }

            return new Value((byte)27, var3);
         }
      }
   }
}

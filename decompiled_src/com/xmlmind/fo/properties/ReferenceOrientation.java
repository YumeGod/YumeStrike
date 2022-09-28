package com.xmlmind.fo.properties;

public class ReferenceOrientation extends Property {
   public ReferenceOrientation(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value integer(String var1) {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var4) {
         return null;
      }

      return var2 >= -270 && var2 <= 270 && var2 % 90 == 0 ? new Value((byte)2, var2) : null;
   }
}

package com.xmlmind.fo.properties;

public class InternalDestination extends Property {
   public InternalDestination(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value idref(String var1) {
      return var1.length() == 0 ? new Value((byte)18, var1) : super.idref(var1);
   }
}

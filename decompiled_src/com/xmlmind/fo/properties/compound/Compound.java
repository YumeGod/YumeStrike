package com.xmlmind.fo.properties.compound;

import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;

public abstract class Compound extends Property {
   public static final int[] list = new int[]{16, 24, 30, 42, 53, 60, 128, 134, 138, 142, 149, 156, 164, 193, 196, 200, 205, 246, 252, 258, 264, 309};

   public Compound(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public abstract void expand(PropertyValues var1);

   protected Value evaluate(Property var1, String var2) {
      Value var3 = var1.evaluate(var2);
      return var3 != null && var3.type != 28 ? var3 : null;
   }
}

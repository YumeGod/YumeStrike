package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public abstract class BorderShorthand extends Shorthand {
   public static final int WIDTH = 0;
   public static final int STYLE = 1;
   public static final int COLOR = 2;

   public BorderShorthand(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value list(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1);
      Property var3 = Property.list[67];
      Property var4 = Property.list[66];
      Property var5 = Property.list[65];
      Value[] var6 = new Value[]{var3.initialValue, var4.initialValue, var5.initialValue};

      while(var2.hasMoreTokens()) {
         var1 = var2.nextToken();
         Value var7 = this.evaluate(var3, var1);
         if (var7 != null) {
            var6[0] = var7;
         } else {
            var7 = this.evaluate(var4, var1);
            if (var7 != null) {
               var6[1] = var7;
            } else {
               var7 = this.evaluate(var5, var1);
               if (var7 == null) {
                  return null;
               }

               var6[2] = var7;
            }
         }
      }

      return new Value((byte)27, var6);
   }
}

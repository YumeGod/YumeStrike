package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class WhiteSpace extends Shorthand {
   public WhiteSpace(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{163, 305, 306, 315};
   }

   protected Value list(String var1) {
      Value[] var3 = new Value[4];
      StringTokenizer var4 = new StringTokenizer(var1);
      if (!var4.hasMoreTokens()) {
         return null;
      } else {
         var1 = var4.nextToken();
         int var2 = Keyword.index(var1);
         if (var2 < 0) {
            return null;
         } else {
            switch (var2) {
               case 127:
                  var3[0] = this.value(207);
                  var3[1] = this.value(209);
                  var3[2] = this.value(85);
                  var3[3] = this.value(224);
                  break;
               case 129:
                  var3[0] = this.value(207);
                  var3[1] = this.value(209);
                  var3[2] = this.value(85);
                  var3[3] = this.value(138);
                  break;
               case 151:
                  var3[0] = this.value(152);
                  var3[1] = this.value(61);
                  var3[2] = this.value(152);
                  var3[3] = this.value(138);
                  break;
               default:
                  return null;
            }

            return new Value((byte)27, var3);
         }
      }
   }

   private final Value value(int var1) {
      return new Value((byte)1, var1);
   }
}

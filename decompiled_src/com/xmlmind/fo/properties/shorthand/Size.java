package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class Size extends Shorthand {
   public Size(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{214, 212};
   }

   protected Value list(String var1) {
      Value[] var3 = new Value[2];
      StringTokenizer var4 = new StringTokenizer(var1);
      if (!var4.hasMoreTokens()) {
         return null;
      } else {
         var1 = var4.nextToken();
         int var2 = Keyword.index(var1);
         if (var2 >= 0) {
            switch (var2) {
               case 10:
                  var3[0] = Value.KEYWORD_AUTO;
                  var3[1] = var3[0];
                  break;
               case 94:
                  var3[0] = new Value((byte)1, 86);
                  var3[1] = Value.KEYWORD_AUTO;
                  break;
               case 150:
                  var3[0] = Value.KEYWORD_AUTO;
                  var3[1] = new Value((byte)1, 86);
                  break;
               default:
                  return null;
            }
         } else {
            var3[0] = this.length(var1);
            if (var3[0] == null) {
               return null;
            }

            var3[1] = var3[0];
            if (var4.hasMoreTokens()) {
               var1 = var4.nextToken();
               var3[1] = this.length(var1);
               if (var3[1] == null) {
                  return null;
               }
            }
         }

         return new Value((byte)27, var3);
      }
   }
}

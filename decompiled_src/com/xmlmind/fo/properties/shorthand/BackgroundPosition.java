package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class BackgroundPosition extends Shorthand {
   public BackgroundPosition(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{11, 12};
   }

   protected Value list(String var1) {
      StringTokenizer var3 = new StringTokenizer(var1);
      Property var4 = Property.list[11];
      Property var5 = Property.list[12];
      Value[] var6 = new Value[2];
      if (!var3.hasMoreTokens()) {
         return null;
      } else {
         var1 = var3.nextToken();
         if (Keyword.index(var1) >= 0) {
            var6[0] = new Value((byte)1, 31);
            var6[1] = var6[0];

            while(var1 != null) {
               int var2 = Keyword.index(var1);
               Value var7 = new Value((byte)1, var2);
               switch (var2) {
                  case 27:
                  case 204:
                     var6[1] = var7;
                  case 31:
                     break;
                  case 100:
                  case 165:
                     var6[0] = var7;
                     break;
                  default:
                     return null;
               }

               if (var3.hasMoreTokens()) {
                  var1 = var3.nextToken();
               } else {
                  var1 = null;
               }
            }
         } else {
            var6[0] = new Value((byte)13, 50.0);
            var6[1] = var6[0];
            var6[0] = this.evaluate(var4, var1);
            if (var6[0] == null) {
               return null;
            }

            if (var3.hasMoreTokens()) {
               var6[1] = this.evaluate(var5, var3.nextToken());
               if (var6[1] == null) {
                  return null;
               }
            }
         }

         return new Value((byte)27, var6);
      }
   }
}

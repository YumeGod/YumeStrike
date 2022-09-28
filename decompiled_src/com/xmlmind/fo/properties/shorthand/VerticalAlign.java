package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class VerticalAlign extends Shorthand {
   private static final int BASELINE = 0;
   private static final int ADJUST = 1;
   private static final int SHIFT = 2;
   private static final int DOMINANT = 3;

   public VerticalAlign(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{3, 2, 14, 94};
   }

   protected Value list(String var1) {
      Value[] var3 = new Value[4];
      StringTokenizer var4 = new StringTokenizer(var1);
      var3[0] = this.value(15);
      var3[1] = Value.KEYWORD_AUTO;
      var3[2] = this.value(15);
      var3[3] = Value.KEYWORD_AUTO;
      if (!var4.hasMoreTokens()) {
         return null;
      } else {
         var1 = var4.nextToken();
         int var2 = Keyword.index(var1);
         if (var2 >= 0) {
            switch (var2) {
               case 15:
                  break;
               case 27:
                  var3[0] = this.value(5);
                  break;
               case 121:
                  var3[0] = this.value(121);
                  break;
               case 193:
                  var3[2] = this.value(193);
                  break;
               case 194:
                  var3[2] = this.value(194);
                  break;
               case 200:
                  var3[0] = this.value(198);
                  break;
               case 201:
                  var3[0] = this.value(199);
                  break;
               case 204:
                  var3[0] = this.value(17);
                  break;
               default:
                  return null;
            }
         } else {
            Value var5 = this.percentage(var1);
            if (var5 == null) {
               var5 = this.length(var1);
            }

            if (var5 == null) {
               return null;
            }

            var3[1] = var5;
         }

         return new Value((byte)27, var3);
      }
   }

   private final Value value(int var1) {
      return new Value((byte)1, var1);
   }
}

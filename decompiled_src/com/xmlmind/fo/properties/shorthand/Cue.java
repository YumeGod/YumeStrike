package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class Cue extends Shorthand {
   public Cue(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{90, 89};
   }

   protected Value list(String var1) {
      Value[] var2 = new Value[2];
      StringTokenizer var3 = new StringTokenizer(var1);
      if (!var3.hasMoreTokens()) {
         return null;
      } else {
         var2[0] = this.cue(var3.nextToken());
         if (var2[0] == null) {
            return null;
         } else {
            var2[1] = var2[0];
            if (var3.hasMoreTokens()) {
               var2[1] = this.cue(var3.nextToken());
               if (var2[1] == null) {
                  return null;
               }
            }

            return new Value((byte)27, var2);
         }
      }
   }

   private Value cue(String var1) {
      int var2 = Keyword.index(var1);
      Value var3;
      if (var2 < 0) {
         var3 = this.uriSpecification(var1);
      } else {
         if (var2 != 125) {
            return null;
         }

         var3 = Value.KEYWORD_NONE;
      }

      return var3;
   }
}

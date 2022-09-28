package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Value;

public class Position extends Shorthand {
   public Position(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{0, 229};
   }

   protected Value list(String var1) {
      Value[] var3 = new Value[2];
      int var2 = Keyword.index(var1);
      if (var2 < 0) {
         return null;
      } else {
         switch (var2) {
            case 1:
               var3[0] = new Value((byte)1, 1);
               var3[1] = Value.KEYWORD_STATIC;
               break;
            case 69:
               var3[0] = new Value((byte)1, 69);
               var3[1] = Value.KEYWORD_STATIC;
               break;
            case 154:
               var3[0] = Value.KEYWORD_AUTO;
               var3[1] = new Value((byte)1, 154);
               break;
            case 191:
               var3[0] = Value.KEYWORD_AUTO;
               var3[1] = new Value((byte)1, 191);
               break;
            default:
               return null;
         }

         return new Value((byte)27, var3);
      }
   }
}

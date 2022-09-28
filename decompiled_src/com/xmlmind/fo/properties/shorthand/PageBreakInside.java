package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.properties.compound.Keep;
import java.util.StringTokenizer;

public class PageBreakInside extends Shorthand {
   public PageBreakInside(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{134};
   }

   public void expand(PropertyValues var1) {
      super.expand(var1);
      Keep var2 = (Keep)Property.list[this.simpleProperties[0]];
      var2.expand(var1);
   }

   protected Value list(String var1) {
      Value[] var3 = new Value[1];
      new StringTokenizer(var1);
      int var2 = Keyword.index(var1);
      if (var2 < 0) {
         return null;
      } else {
         switch (var2) {
            case 10:
               var3[0] = Value.KEYWORD_AUTO;
               break;
            case 13:
               var3[0] = Value.KEYWORD_ALWAYS;
               break;
            default:
               return null;
         }

         return new Value((byte)27, var3);
      }
   }
}

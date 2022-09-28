package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class XmlLang extends Shorthand {
   public XmlLang(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{146, 87};
   }

   protected Value list(String var1) {
      Value[] var3 = new Value[2];
      StringTokenizer var4 = new StringTokenizer(var1, "-");
      if (!var4.hasMoreTokens()) {
         return null;
      } else {
         var3[0] = this.language(var4.nextToken());
         if (var3[0] == null) {
            return null;
         } else {
            if (var4.hasMoreTokens()) {
               var3[1] = this.country(var4.nextToken());
               if (var3[1] == null) {
                  return null;
               }
            } else {
               var3[1] = Property.list[87].initialValue;
            }

            return new Value((byte)27, var3);
         }
      }
   }
}

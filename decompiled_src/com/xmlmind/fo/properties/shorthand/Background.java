package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class Background extends Shorthand {
   private static final int COLOR = 0;
   private static final int IMAGE = 1;
   private static final int REPEAT = 2;
   private static final int ATTACHMENT = 3;
   private static final int POSITION_HORIZONTAL = 4;
   private static final int POSITION_VERTICAL = 5;

   public Background(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{8, 9, 13, 7, 11, 12};
   }

   protected Value list(String var1) {
      Value[] var3 = new Value[6];
      StringTokenizer var4 = new StringTokenizer(var1);

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var3[var5] = Property.list[this.simpleProperties[var5]].initialValue;
      }

      if (!var4.hasMoreTokens()) {
         return null;
      } else {
         var1 = var4.nextToken();

         while(var1 != null) {
            int var2;
            Value var6;
            if ((var2 = Keyword.index(var1)) >= 0) {
               var6 = new Value((byte)1, var2);
               label77:
               switch (var2) {
                  case 27:
                  case 31:
                  case 100:
                  case 165:
                  case 204:
                     switch (var2) {
                        case 27:
                        case 204:
                           var3[5] = var6;
                           break;
                        case 31:
                           var3[4] = var6;
                           var3[5] = var6;
                           break;
                        case 100:
                        case 165:
                           var3[4] = var6;
                     }

                     if (var4.hasMoreTokens()) {
                        var1 = var4.nextToken();
                        if ((var2 = Keyword.index(var1)) < 0) {
                           continue;
                        }

                        switch (var2) {
                           case 27:
                           case 204:
                              var3[5] = var6;
                           case 31:
                              break label77;
                           case 100:
                           case 165:
                              var3[4] = var6;
                              break label77;
                           default:
                              continue;
                        }
                     }
                     break;
                  case 69:
                  case 174:
                     var3[3] = var6;
                     break;
                  case 125:
                     var3[1] = var6;
                     break;
                  case 136:
                  case 156:
                  case 157:
                  case 158:
                     var3[2] = var6;
                     break;
                  case 206:
                     var3[0] = var6;
                     break;
                  default:
                     return null;
               }
            } else if ((var6 = this.color(var1)) != null) {
               var3[0] = var6;
            } else if ((var6 = this.uriSpecification(var1)) != null) {
               var3[1] = var6;
            } else {
               if ((var6 = this.percentage(var1)) == null) {
                  var6 = this.length(var1);
               }

               if (var6 == null) {
                  return null;
               }

               var3[4] = var6;
               var3[5] = new Value((byte)13, 50.0);
               if (var4.hasMoreTokens()) {
                  var1 = var4.nextToken();
                  if ((var6 = this.percentage(var1)) == null) {
                     var6 = this.length(var1);
                  }

                  if (var6 == null) {
                     continue;
                  }

                  var3[5] = var6;
               }
            }

            if (var4.hasMoreTokens()) {
               var1 = var4.nextToken();
            } else {
               var1 = null;
            }
         }

         return new Value((byte)27, var3);
      }
   }
}

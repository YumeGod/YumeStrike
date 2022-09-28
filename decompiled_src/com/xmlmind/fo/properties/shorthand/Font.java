package com.xmlmind.fo.properties.shorthand;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.Value;
import java.util.StringTokenizer;

public class Font extends Shorthand {
   private static final int STYLE = 0;
   private static final int VARIANT = 1;
   private static final int WEIGHT = 2;
   private static final int SIZE = 3;
   private static final int HEIGHT = 4;
   private static final int FAMILY = 5;
   private static final int MINIMUM = 0;
   private static final int OPTIMUM = 1;
   private static final int MAXIMUM = 2;
   private static final int CONDITIONALITY = 3;
   private static final int PRECEDENCE = 4;

   public Font(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.simpleProperties = new int[]{109, 110, 111, 106, 164, 104, 108, 107};
   }

   protected Value list(String var1) {
      Property var5 = Property.list[109];
      Property var6 = Property.list[110];
      Property var7 = Property.list[111];
      Property var8 = Property.list[106];
      Property var9 = Property.list[164];
      Property var10 = Property.list[104];
      Value[] var12 = new Value[8];

      int var2;
      for(var2 = 0; var2 < var12.length; ++var2) {
         var12[var2] = Property.list[this.simpleProperties[var2]].initialValue;
      }

      var12[3] = null;
      var2 = var1.indexOf(47);
      if (var2 > 0 && var2 < var1.length() - 1) {
         var1 = var1.substring(0, var2) + " / " + var1.substring(var2 + 1, var1.length());
      }

      StringTokenizer var4 = new StringTokenizer(var1);

      Value var11;
      while(var12[3] == null) {
         if (!var4.hasMoreTokens()) {
            return null;
         }

         var1 = var4.nextToken();
         var11 = this.evaluate(var8, var1);
         if (var11 != null) {
            var12[3] = var11;
         } else {
            var11 = this.evaluate(var5, var1);
            if (var11 != null) {
               var12[0] = var11;
            } else {
               var11 = this.evaluate(var6, var1);
               if (var11 != null) {
                  var12[1] = var11;
               } else {
                  var11 = this.evaluate(var7, var1);
                  if (var11 == null) {
                     return null;
                  }

                  var12[2] = var11;
               }
            }
         }
      }

      if (!var4.hasMoreTokens()) {
         return null;
      } else {
         String var3 = var4.nextToken("");
         var4 = new StringTokenizer(var3);
         var1 = var4.nextToken();
         if (var1.equals("/")) {
            if (!var4.hasMoreTokens()) {
               return null;
            }

            var1 = var4.nextToken();
            var11 = this.evaluate(var9, var1);
            if (var11 == null) {
               return null;
            }

            var12[4] = var11;
            if (!var4.hasMoreTokens()) {
               return null;
            }

            var3 = var4.nextToken("");
            if (var12[4].type == 4) {
               var4 = new StringTokenizer(var3);
               var1 = var4.nextToken();
               if ((var11 = this.length(var1)) != null) {
                  Value[] var13 = new Value[]{var12[4], var11, null, null, null};
                  if (!var4.hasMoreTokens()) {
                     return null;
                  }

                  var13[2] = this.length(var4.nextToken());
                  if (var13[2] == null) {
                     return null;
                  }

                  if (!var4.hasMoreTokens()) {
                     return null;
                  }

                  var1 = var4.nextToken();
                  if (var1.equals("discard")) {
                     var13[3] = Value.KEYWORD_DISCARD;
                  } else {
                     if (!var1.equals("retain")) {
                        return null;
                     }

                     var13[3] = Value.KEYWORD_RETAIN;
                  }

                  if (!var4.hasMoreTokens()) {
                     return null;
                  }

                  var1 = var4.nextToken();
                  if (var1.equals("force")) {
                     var13[4] = Value.KEYWORD_FORCE;
                  } else {
                     var13[4] = this.integer(var1);
                     if (var13[4] == null) {
                        return null;
                     }
                  }

                  var12[4] = new Value((byte)9, var13);
                  if (!var4.hasMoreTokens()) {
                     return null;
                  }

                  var3 = var4.nextToken("");
               }
            }
         }

         var11 = this.evaluate(var10, var3);
         if (var11 == null) {
            return null;
         } else {
            var12[5] = var11;
            return new Value((byte)27, var12);
         }
      }
   }

   public Value compute(Value var1, Context var2) {
      if (var1.type == 1) {
         int var3 = var1.keyword();
         if (var3 == 88) {
            return super.compute(var1, var2);
         }

         String var4 = var2.translator.systemFont(var1.keyword());
         var1 = this.compute(this.evaluate(var4), var2);
      } else {
         var1 = super.compute(var1, var2);
      }

      return var1;
   }
}

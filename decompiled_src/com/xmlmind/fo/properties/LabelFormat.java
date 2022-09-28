package com.xmlmind.fo.properties;

import com.xmlmind.fo.util.StringUtil;

public final class LabelFormat {
   public static final int TYPE_NONE = 0;
   public static final int TYPE_BULLET = 1;
   public static final int TYPE_DECIMAL = 2;
   public static final int TYPE_LOWER_ALPHA = 3;
   public static final int TYPE_UPPER_ALPHA = 4;
   public static final int TYPE_LOWER_ROMAN = 5;
   public static final int TYPE_UPPER_ROMAN = 6;
   public final String prefix;
   public final int type;
   public final int start;
   public final String suffix;

   public LabelFormat(String var1, int var2, int var3, String var4) {
      if (var1 != null && (var1 = var1.trim()).length() == 0) {
         var1 = null;
      }

      this.prefix = var1;
      this.type = var2;
      if (var3 < 0 || var3 == 0 && var2 != 2) {
         var3 = 1;
      }

      this.start = var3;
      if (var4 != null && (var4 = var4.trim()).length() == 0) {
         var4 = null;
      }

      this.suffix = var4;
   }

   public static LabelFormat parse(String var0) {
      if (var0 == null) {
         return null;
      } else {
         var0 = var0.trim();
         String var1 = null;
         int var2 = 0;
         int var3 = 0;
         String var4 = null;
         int var5 = var0.length();
         if (var5 > 0) {
            int var6;
            for(var6 = var0.indexOf("%{"); var6 > 0 && var0.charAt(var6 - 1) == '%'; var6 = var0.indexOf("%{", var6 + 2)) {
            }

            if (var6 >= 0) {
               int var7 = var0.indexOf(125, var6 + 2);
               if (var7 <= var6 + 2) {
                  return null;
               }

               String var8 = var0.substring(var6 + 2, var7);
               int[] var9 = parseVarSpec(var8);
               if (var9 == null) {
                  return null;
               }

               var2 = var9[0];
               var3 = var9[1];
               if (var6 > 0) {
                  var1 = var0.substring(0, var6);
                  if (var1.indexOf(37) >= 0) {
                     var1 = StringUtil.replaceAll(var1, "%%", "%");
                  }
               }

               if (var7 + 1 < var5) {
                  var4 = var0.substring(var7 + 1);
                  if (var4.indexOf(37) >= 0) {
                     var4 = StringUtil.replaceAll(var4, "%%", "%");
                  }
               }
            }

            if (var2 == 0) {
               var2 = 1;
               var4 = var0;
               if (var0.indexOf(37) >= 0) {
                  var4 = StringUtil.replaceAll(var0, "%%", "%");
               }
            }
         }

         return new LabelFormat(var1, var2, var3, var4);
      }
   }

   private static int[] parseVarSpec(String var0) {
      int var3 = var0.indexOf(59);
      String var1;
      String var2;
      if (var3 > 0 && var3 + 1 < var0.length()) {
         var1 = var0.substring(0, var3).trim().toLowerCase();
         var2 = var0.substring(var3 + 1).trim().toLowerCase();
      } else {
         var1 = var0.trim().toLowerCase();
         var2 = null;
      }

      byte var4 = 0;
      if ("decimal".equals(var1)) {
         var4 = 2;
      } else if ("lower-alpha".equals(var1)) {
         var4 = 3;
      } else if ("upper-alpha".equals(var1)) {
         var4 = 4;
      } else if ("lower-roman".equals(var1)) {
         var4 = 5;
      } else if ("upper-roman".equals(var1)) {
         var4 = 6;
      }

      int var5 = 1;
      if (var2 != null) {
         var5 = -1;
         if (var2.startsWith("start=")) {
            try {
               var5 = Integer.parseInt(var2.substring(6));
            } catch (NumberFormatException var7) {
            }
         }
      }

      return var4 != 0 && var5 >= 0 ? new int[]{var4, var5} : null;
   }

   public String toString() {
      if (this.type == 0) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         String var2 = this.prefix;
         if (var2 != null) {
            if (var2.indexOf(37) >= 0) {
               var2 = StringUtil.replaceAll(var2, "%", "%%");
            }

            var1.append(var2);
         }

         if (this.type != 1) {
            var1.append("%{");
            switch (this.type) {
               case 2:
               default:
                  var1.append("decimal");
                  break;
               case 3:
                  var1.append("lower-alpha");
                  break;
               case 4:
                  var1.append("upper-alpha");
                  break;
               case 5:
                  var1.append("lower-roman");
                  break;
               case 6:
                  var1.append("upper-roman");
            }

            if (this.start != 1) {
               var1.append(";start=");
               var1.append(this.start);
            }

            var1.append('}');
         }

         var2 = this.suffix;
         if (var2 != null) {
            if (var2.indexOf(37) >= 0) {
               var2 = StringUtil.replaceAll(var2, "%", "%%");
            }

            var1.append(var2);
         }

         return var1.toString();
      }
   }
}

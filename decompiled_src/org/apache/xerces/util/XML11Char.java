package org.apache.xerces.util;

import java.util.Arrays;

public class XML11Char {
   private static final byte[] XML11CHARS = new byte[65536];
   public static final int MASK_XML11_VALID = 1;
   public static final int MASK_XML11_SPACE = 2;
   public static final int MASK_XML11_NAME_START = 4;
   public static final int MASK_XML11_NAME = 8;
   public static final int MASK_XML11_CONTROL = 16;
   public static final int MASK_XML11_CONTENT = 32;
   public static final int MASK_XML11_NCNAME_START = 64;
   public static final int MASK_XML11_NCNAME = 128;
   public static final int MASK_XML11_CONTENT_INTERNAL = 48;

   public static boolean isXML11Space(int var0) {
      return var0 < 65536 && (XML11CHARS[var0] & 2) != 0;
   }

   public static boolean isXML11Valid(int var0) {
      return var0 < 65536 && (XML11CHARS[var0] & 1) != 0 || 65536 <= var0 && var0 <= 1114111;
   }

   public static boolean isXML11Invalid(int var0) {
      return !isXML11Valid(var0);
   }

   public static boolean isXML11ValidLiteral(int var0) {
      return var0 < 65536 && (XML11CHARS[var0] & 1) != 0 && (XML11CHARS[var0] & 16) == 0 || 65536 <= var0 && var0 <= 1114111;
   }

   public static boolean isXML11Content(int var0) {
      return var0 < 65536 && (XML11CHARS[var0] & 32) != 0 || 65536 <= var0 && var0 <= 1114111;
   }

   public static boolean isXML11InternalEntityContent(int var0) {
      return var0 < 65536 && (XML11CHARS[var0] & 48) != 0 || 65536 <= var0 && var0 <= 1114111;
   }

   public static boolean isXML11NameStart(int var0) {
      return var0 < 65536 && (XML11CHARS[var0] & 4) != 0 || 65536 <= var0 && var0 < 983040;
   }

   public static boolean isXML11Name(int var0) {
      return var0 < 65536 && (XML11CHARS[var0] & 8) != 0 || var0 >= 65536 && var0 < 983040;
   }

   public static boolean isXML11NCNameStart(int var0) {
      return var0 < 65536 && (XML11CHARS[var0] & 64) != 0 || 65536 <= var0 && var0 < 983040;
   }

   public static boolean isXML11NCName(int var0) {
      return var0 < 65536 && (XML11CHARS[var0] & 128) != 0 || 65536 <= var0 && var0 < 983040;
   }

   public static boolean isXML11NameHighSurrogate(int var0) {
      return 55296 <= var0 && var0 <= 56191;
   }

   public static boolean isXML11ValidName(String var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return false;
      } else {
         int var2 = 1;
         char var3 = var0.charAt(0);
         char var4;
         if (!isXML11NameStart(var3)) {
            if (var1 <= 1 || !isXML11NameHighSurrogate(var3)) {
               return false;
            }

            var4 = var0.charAt(1);
            if (!XMLChar.isLowSurrogate(var4) || !isXML11NameStart(XMLChar.supplemental(var3, var4))) {
               return false;
            }

            var2 = 2;
         }

         for(; var2 < var1; ++var2) {
            var3 = var0.charAt(var2);
            if (!isXML11Name(var3)) {
               ++var2;
               if (var2 >= var1 || !isXML11NameHighSurrogate(var3)) {
                  return false;
               }

               var4 = var0.charAt(var2);
               if (!XMLChar.isLowSurrogate(var4) || !isXML11Name(XMLChar.supplemental(var3, var4))) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public static boolean isXML11ValidNCName(String var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return false;
      } else {
         int var2 = 1;
         char var3 = var0.charAt(0);
         char var4;
         if (!isXML11NCNameStart(var3)) {
            if (var1 <= 1 || !isXML11NameHighSurrogate(var3)) {
               return false;
            }

            var4 = var0.charAt(1);
            if (!XMLChar.isLowSurrogate(var4) || !isXML11NCNameStart(XMLChar.supplemental(var3, var4))) {
               return false;
            }

            var2 = 2;
         }

         for(; var2 < var1; ++var2) {
            var3 = var0.charAt(var2);
            if (!isXML11NCName(var3)) {
               ++var2;
               if (var2 >= var1 || !isXML11NameHighSurrogate(var3)) {
                  return false;
               }

               var4 = var0.charAt(var2);
               if (!XMLChar.isLowSurrogate(var4) || !isXML11NCName(XMLChar.supplemental(var3, var4))) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public static boolean isXML11ValidNmtoken(String var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1; ++var2) {
            char var3 = var0.charAt(var2);
            if (!isXML11Name(var3)) {
               ++var2;
               if (var2 >= var1 || !isXML11NameHighSurrogate(var3)) {
                  return false;
               }

               char var4 = var0.charAt(var2);
               if (!XMLChar.isLowSurrogate(var4) || !isXML11Name(XMLChar.supplemental(var3, var4))) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   static {
      Arrays.fill(XML11CHARS, 1, 9, (byte)17);
      XML11CHARS[9] = 35;
      XML11CHARS[10] = 3;
      Arrays.fill(XML11CHARS, 11, 13, (byte)17);
      XML11CHARS[13] = 3;
      Arrays.fill(XML11CHARS, 14, 32, (byte)17);
      XML11CHARS[32] = 35;
      Arrays.fill(XML11CHARS, 33, 38, (byte)33);
      XML11CHARS[38] = 1;
      Arrays.fill(XML11CHARS, 39, 45, (byte)33);
      Arrays.fill(XML11CHARS, 45, 47, (byte)-87);
      XML11CHARS[47] = 33;
      Arrays.fill(XML11CHARS, 48, 58, (byte)-87);
      XML11CHARS[58] = 45;
      XML11CHARS[59] = 33;
      XML11CHARS[60] = 1;
      Arrays.fill(XML11CHARS, 61, 65, (byte)33);
      Arrays.fill(XML11CHARS, 65, 91, (byte)-19);
      Arrays.fill(XML11CHARS, 91, 93, (byte)33);
      XML11CHARS[93] = 1;
      XML11CHARS[94] = 33;
      XML11CHARS[95] = -19;
      XML11CHARS[96] = 33;
      Arrays.fill(XML11CHARS, 97, 123, (byte)-19);
      Arrays.fill(XML11CHARS, 123, 127, (byte)33);
      Arrays.fill(XML11CHARS, 127, 133, (byte)17);
      XML11CHARS[133] = 35;
      Arrays.fill(XML11CHARS, 134, 160, (byte)17);
      Arrays.fill(XML11CHARS, 160, 183, (byte)33);
      XML11CHARS[183] = -87;
      Arrays.fill(XML11CHARS, 184, 192, (byte)33);
      Arrays.fill(XML11CHARS, 192, 215, (byte)-19);
      XML11CHARS[215] = 33;
      Arrays.fill(XML11CHARS, 216, 247, (byte)-19);
      XML11CHARS[247] = 33;
      Arrays.fill(XML11CHARS, 248, 768, (byte)-19);
      Arrays.fill(XML11CHARS, 768, 880, (byte)-87);
      Arrays.fill(XML11CHARS, 880, 894, (byte)-19);
      XML11CHARS[894] = 33;
      Arrays.fill(XML11CHARS, 895, 8192, (byte)-19);
      Arrays.fill(XML11CHARS, 8192, 8204, (byte)33);
      Arrays.fill(XML11CHARS, 8204, 8206, (byte)-19);
      Arrays.fill(XML11CHARS, 8206, 8232, (byte)33);
      XML11CHARS[8232] = 35;
      Arrays.fill(XML11CHARS, 8233, 8255, (byte)33);
      Arrays.fill(XML11CHARS, 8255, 8257, (byte)-87);
      Arrays.fill(XML11CHARS, 8257, 8304, (byte)33);
      Arrays.fill(XML11CHARS, 8304, 8592, (byte)-19);
      Arrays.fill(XML11CHARS, 8592, 11264, (byte)33);
      Arrays.fill(XML11CHARS, 11264, 12272, (byte)-19);
      Arrays.fill(XML11CHARS, 12272, 12289, (byte)33);
      Arrays.fill(XML11CHARS, 12289, 55296, (byte)-19);
      Arrays.fill(XML11CHARS, 57344, 63744, (byte)33);
      Arrays.fill(XML11CHARS, 63744, 64976, (byte)-19);
      Arrays.fill(XML11CHARS, 64976, 65008, (byte)33);
      Arrays.fill(XML11CHARS, 65008, 65534, (byte)-19);
   }
}

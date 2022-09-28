package com.xmlmind.fo.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public final class URLUtil {
   private static final int[] fromHexDigit = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15};

   private URLUtil() {
   }

   public static String fileToLocation(File var0) {
      String var1;
      try {
         var1 = var0.getCanonicalPath();
      } catch (IOException var7) {
         var1 = var0.getAbsolutePath();
      }

      if (var1 == null) {
         var1 = var0.getPath();
      }

      StringBuffer var2 = new StringBuffer("file:");
      if (!var1.startsWith(File.separator)) {
         var2.append('/');
      }

      String[] var3 = StringUtil.split(var1, File.separatorChar);
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (var5 > 0) {
            var2.append('/');
         }

         escapePathSegment(var6, "UTF8", var2);
      }

      if (var0.isDirectory() && var2.charAt(var2.length() - 1) != '/') {
         var2.append('/');
      }

      return var2.toString();
   }

   private static void escapePathSegment(String var0, String var1, StringBuffer var2) {
      char[] var3 = new char[1];
      int var4 = var0.length();

      for(int var5 = 0; var5 < var4; ++var5) {
         char var6 = var0.charAt(var5);
         switch (var6) {
            case '!':
            case '$':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case ':':
            case '=':
            case '@':
            case '_':
            case '~':
               var2.append(var6);
               continue;
         }

         if (var6 >= '0' && var6 <= '9' || var6 >= 'A' && var6 <= 'Z' || var6 >= 'a' && var6 <= 'z') {
            var2.append(var6);
         } else {
            byte[] var7;
            try {
               var3[0] = var6;
               var7 = (new String(var3)).getBytes(var1);
            } catch (UnsupportedEncodingException var10) {
               var10.printStackTrace();
               return;
            }

            for(int var8 = 0; var8 < var7.length; ++var8) {
               String var9 = Integer.toHexString(var7[var8] & 255);
               var2.append('%');
               if (var9.length() == 1) {
                  var2.append('0');
               }

               var2.append(var9.toUpperCase());
            }
         }
      }

   }

   public static URL fileToURL(File var0) {
      try {
         return new URL(fileToLocation(var0));
      } catch (MalformedURLException var2) {
         return null;
      }
   }

   public static File urlToFile(URL var0) {
      return !"file".equals(var0.getProtocol()) ? null : urlToFile(var0.getHost(), var0.getFile());
   }

   private static File urlToFile(String var0, String var1) {
      if (var0 != null && var0.length() == 0) {
         var0 = null;
      }

      if (var1 == null || var1.length() == 0) {
         var1 = "/";
      }

      StringBuffer var2 = new StringBuffer();
      String[] var3 = StringUtil.split(var1, '/');
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (var5 > 0) {
            var2.append(File.separatorChar);
         }

         var2.append(decode(var6, "UTF8"));
      }

      if (SystemUtil.IS_WINDOWS) {
         if (var0 != null) {
            var2.insert(0, decode(var0, "UTF8"));
            var2.insert(0, "\\\\");
         } else {
            if (var2.length() >= 4 && var2.charAt(0) == File.separatorChar && isDriveLetter(var2.charAt(1)) && var2.charAt(2) == ':' && var2.charAt(3) == File.separatorChar) {
            }

            var2.deleteCharAt(0);
         }
      }

      String var7 = var2.toString();
      int var8 = var7.length() - 1;
      if (var8 > 0 && var7.charAt(var8) == File.separatorChar && var7.lastIndexOf(File.separatorChar, var8 - 1) >= 0) {
         var7 = var7.substring(0, var8);
      }

      return new File(var7);
   }

   private static boolean isDriveLetter(char var0) {
      return var0 >= 'A' && var0 <= 'Z' || var0 >= 'a' && var0 <= 'z';
   }

   private static String decode(String var0, String var1) {
      if (var0.indexOf(37) < 0) {
         return var0;
      } else {
         byte[] var2;
         try {
            var2 = var0.getBytes(var1);
         } catch (UnsupportedEncodingException var12) {
            var12.printStackTrace();
            return null;
         }

         int var3 = var2.length;
         byte[] var4 = new byte[var3];
         int var5 = 0;

         for(int var6 = 0; var6 < var3; ++var6) {
            byte var7 = var2[var6];
            int var8;
            int var9;
            int var10;
            if (var7 == 37 && var6 + 2 < var3 && (var8 = fromHexDigit(var2[var6 + 1])) >= 0 && (var9 = fromHexDigit(var2[var6 + 2])) >= 0) {
               var10 = var8 << 4 | var9;
               var6 += 2;
            } else {
               var10 = var7;
            }

            var4[var5++] = (byte)var10;
         }

         try {
            return new String(var4, 0, var5, var1);
         } catch (UnsupportedEncodingException var11) {
            var11.printStackTrace();
            return null;
         }
      }
   }

   private static final int fromHexDigit(int var0) {
      return var0 >= 48 && var0 <= 102 ? fromHexDigit[var0 - 48] : -1;
   }

   public static File locationToFile(String var0) {
      if (!var0.startsWith("file:")) {
         return null;
      } else {
         var0 = var0.substring(5);
         String var1 = null;
         if (var0.startsWith("//")) {
            int var2 = var0.indexOf(47, 2);
            if (var2 >= 2) {
               if (var2 > 2) {
                  var1 = var0.substring(2, var2);
               }

               var0 = var0.substring(var2);
            }
         }

         return urlToFile(var1, var0);
      }
   }

   public static String locationToFilename(String var0) {
      File var1 = locationToFile(var0);
      return var1 == null ? null : var1.getPath();
   }

   public static URL urlOrFile(String var0) {
      try {
         return new URL(var0);
      } catch (MalformedURLException var2) {
         File var1 = new File(var0);
         return var1.isFile() && var1.isAbsolute() ? fileToURL(var1) : null;
      }
   }

   public static String locationOrFilename(String var0) {
      try {
         new URL(var0);
         return var0;
      } catch (MalformedURLException var2) {
         File var1 = new File(var0);
         return var1.isFile() && var1.isAbsolute() ? fileToLocation(var1) : var0;
      }
   }

   public static String normalizeMIMEType(String var0) {
      int var1 = var0.indexOf(59);
      if (var1 >= 0) {
         var0 = var0.substring(0, var1);
      }

      return var0.toLowerCase().trim();
   }

   public static InputStream openStream(String var0) throws IOException, MalformedURLException {
      if (var0.startsWith("data:")) {
         int var1 = var0.indexOf(";base64,", 5);
         if (var1 < 0) {
            throw new IOException("expects the data of '" + var0 + "' to be encoded as base64");
         } else {
            byte[] var2 = Base64.decode(var0.substring(var1 + 8));
            if (var2 == null) {
               throw new IOException("cannot decode the base64-encoded data of '" + var0 + "'");
            } else {
               return new ByteArrayInputStream(var2);
            }
         }
      } else {
         return (InputStream)(var0.startsWith("file:") ? new FileInputStream(locationToFile(var0)) : (new URL(var0)).openStream());
      }
   }
}

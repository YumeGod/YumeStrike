package org.apache.batik.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ParsedURLDataProtocolHandler extends AbstractParsedURLProtocolHandler {
   static final String DATA_PROTOCOL = "data";
   static final String BASE64 = "base64";
   static final String CHARSET = "charset";

   public ParsedURLDataProtocolHandler() {
      super("data");
   }

   public ParsedURLData parseURL(ParsedURL var1, String var2) {
      return this.parseURL(var2);
   }

   public ParsedURLData parseURL(String var1) {
      DataParsedURLData var2 = new DataParsedURLData();
      int var3 = 0;
      int var5 = var1.length();
      int var4 = var1.indexOf(35);
      var2.ref = null;
      if (var4 != -1) {
         if (var4 + 1 < var5) {
            var2.ref = var1.substring(var4 + 1);
         }

         var1 = var1.substring(0, var4);
         var5 = var1.length();
      }

      var4 = var1.indexOf(58);
      if (var4 != -1) {
         var2.protocol = var1.substring(var3, var4);
         if (var2.protocol.indexOf(47) == -1) {
            var3 = var4 + 1;
         } else {
            var2.protocol = null;
            var3 = 0;
         }
      }

      var4 = var1.indexOf(44, var3);
      if (var4 != -1 && var4 != var3) {
         var2.host = var1.substring(var3, var4);
         var3 = var4 + 1;
         int var6 = var2.host.lastIndexOf(59);
         if (var6 != -1 && var6 != var2.host.length()) {
            String var7 = var2.host.substring(var6 + 1);
            var4 = var7.indexOf(61);
            if (var4 == -1) {
               var2.contentEncoding = var7;
               var2.contentType = var2.host.substring(0, var6);
            } else {
               var2.contentType = var2.host;
            }

            byte var10 = 0;
            var4 = var2.contentType.indexOf(59, var10);
            if (var4 != -1) {
               for(var6 = var4 + 1; var6 < var2.contentType.length(); var6 = var4 + 1) {
                  var4 = var2.contentType.indexOf(59, var6);
                  if (var4 == -1) {
                     var4 = var2.contentType.length();
                  }

                  String var8 = var2.contentType.substring(var6, var4);
                  int var9 = var8.indexOf(61);
                  if (var9 != -1 && "charset".equals(var8.substring(0, var9))) {
                     var2.charset = var8.substring(var9 + 1);
                  }
               }
            }
         } else {
            var2.contentType = var2.host;
         }
      }

      if (var3 < var1.length()) {
         var2.path = var1.substring(var3);
      }

      return var2;
   }

   static class DataParsedURLData extends ParsedURLData {
      String charset;

      public boolean complete() {
         return this.path != null;
      }

      public String getPortStr() {
         String var1 = "data:";
         if (this.host != null) {
            var1 = var1 + this.host;
         }

         var1 = var1 + ",";
         return var1;
      }

      public String toString() {
         String var1 = this.getPortStr();
         if (this.path != null) {
            var1 = var1 + this.path;
         }

         if (this.ref != null) {
            var1 = var1 + '#' + this.ref;
         }

         return var1;
      }

      public String getContentType(String var1) {
         return this.contentType;
      }

      public String getContentEncoding(String var1) {
         return this.contentEncoding;
      }

      protected InputStream openStreamInternal(String var1, Iterator var2, Iterator var3) throws IOException {
         this.stream = decode(this.path);
         if ("base64".equals(this.contentEncoding)) {
            this.stream = new Base64DecodeStream(this.stream);
         }

         return this.stream;
      }

      public static InputStream decode(String var0) {
         int var1 = var0.length();
         byte[] var2 = new byte[var1];
         int var3 = 0;

         for(int var4 = 0; var4 < var1; ++var4) {
            char var5 = var0.charAt(var4);
            switch (var5) {
               case '%':
                  if (var4 + 2 >= var1) {
                     break;
                  }

                  var4 += 2;
                  char var7 = var0.charAt(var4 - 1);
                  byte var6;
                  if (var7 >= '0' && var7 <= '9') {
                     var6 = (byte)(var7 - 48);
                  } else if (var7 >= 'a' && var7 <= 'z') {
                     var6 = (byte)(var7 - 97 + 10);
                  } else {
                     if (var7 < 'A' || var7 > 'Z') {
                        break;
                     }

                     var6 = (byte)(var7 - 65 + 10);
                  }

                  var6 = (byte)(var6 * 16);
                  char var8 = var0.charAt(var4);
                  if (var8 >= '0' && var8 <= '9') {
                     var6 += (byte)(var8 - 48);
                  } else if (var8 >= 'a' && var8 <= 'z') {
                     var6 += (byte)(var8 - 97 + 10);
                  } else {
                     if (var8 < 'A' || var8 > 'Z') {
                        break;
                     }

                     var6 += (byte)(var8 - 65 + 10);
                  }

                  var2[var3++] = var6;
                  break;
               default:
                  var2[var3++] = (byte)var5;
            }
         }

         return new ByteArrayInputStream(var2, 0, var3);
      }
   }
}

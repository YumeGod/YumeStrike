package org.apache.batik.util;

import java.net.MalformedURLException;
import java.net.URL;

public class ParsedURLDefaultProtocolHandler extends AbstractParsedURLProtocolHandler {
   public ParsedURLDefaultProtocolHandler() {
      super((String)null);
   }

   protected ParsedURLDefaultProtocolHandler(String var1) {
      super(var1);
   }

   protected ParsedURLData constructParsedURLData() {
      return new ParsedURLData();
   }

   protected ParsedURLData constructParsedURLData(URL var1) {
      return new ParsedURLData(var1);
   }

   public ParsedURLData parseURL(String var1) {
      try {
         URL var13 = new URL(var1);
         return this.constructParsedURLData(var13);
      } catch (MalformedURLException var12) {
         ParsedURLData var2 = this.constructParsedURLData();
         if (var1 == null) {
            return var2;
         } else {
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

            if (var5 == 0) {
               return var2;
            } else {
               var4 = 0;

               char var6;
               for(var6 = var1.charAt(var4); var6 == '-' || var6 == '+' || var6 == '.' || var6 >= 'a' && var6 <= 'z' || var6 >= 'A' && var6 <= 'Z'; var6 = var1.charAt(var4)) {
                  ++var4;
                  if (var4 == var5) {
                     var6 = 0;
                     break;
                  }
               }

               if (var6 == ':') {
                  var2.protocol = var1.substring(var3, var4).toLowerCase();
                  var3 = var4 + 1;
               }

               var4 = var1.indexOf(47);
               if (var4 == -1 || var3 + 2 < var5 && var1.charAt(var3) == '/' && var1.charAt(var3 + 1) == '/') {
                  if (var4 != -1) {
                     var3 += 2;
                  }

                  var4 = var1.indexOf(47, var3);
                  String var7;
                  if (var4 == -1) {
                     var7 = var1.substring(var3);
                  } else {
                     var7 = var1.substring(var3, var4);
                  }

                  int var8 = var4;
                  var4 = var7.indexOf(58);
                  var2.port = -1;
                  if (var4 == -1) {
                     if (var7.length() == 0) {
                        var2.host = null;
                     } else {
                        var2.host = var7;
                     }
                  } else {
                     if (var4 == 0) {
                        var2.host = null;
                     } else {
                        var2.host = var7.substring(0, var4);
                     }

                     if (var4 + 1 < var7.length()) {
                        String var9 = var7.substring(var4 + 1);

                        try {
                           var2.port = Integer.parseInt(var9);
                        } catch (NumberFormatException var11) {
                        }
                     }
                  }

                  if ((var2.host == null || var2.host.indexOf(46) == -1) && var2.port == -1) {
                     var2.host = null;
                  } else {
                     var3 = var8;
                  }
               }

               if (var3 != -1 && var3 < var5) {
                  var2.path = var1.substring(var3);
                  return var2;
               } else {
                  return var2;
               }
            }
         }
      }
   }

   public static String unescapeStr(String var0) {
      int var1 = var0.indexOf(37);
      if (var1 == -1) {
         return var0;
      } else {
         int var2 = 0;
         StringBuffer var3 = new StringBuffer();

         while(var1 != -1) {
            if (var1 != var2) {
               var3.append(var0.substring(var2, var1));
            }

            if (var1 + 2 >= var0.length()) {
               break;
            }

            var2 = var1 + 3;
            var1 = var0.indexOf(37, var2);
            int var4 = charToHex(var0.charAt(var1 + 1));
            int var5 = charToHex(var0.charAt(var1 + 1));
            if (var4 != -1 && var5 != -1) {
               var3.append((char)(var4 << 4 | var5));
            }
         }

         return var3.toString();
      }
   }

   public static int charToHex(int var0) {
      switch (var0) {
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
            return var0 - 48;
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         default:
            return -1;
         case 65:
         case 97:
            return 10;
         case 66:
         case 98:
            return 11;
         case 67:
         case 99:
            return 12;
         case 68:
         case 100:
            return 13;
         case 69:
         case 101:
            return 14;
         case 70:
         case 102:
            return 15;
      }
   }

   public ParsedURLData parseURL(ParsedURL var1, String var2) {
      if (var2.length() == 0) {
         return var1.data;
      } else {
         int var3 = 0;
         int var4 = var2.length();
         if (var4 == 0) {
            return var1.data;
         } else {
            char var5;
            for(var5 = var2.charAt(var3); var5 == '-' || var5 == '+' || var5 == '.' || var5 >= 'a' && var5 <= 'z' || var5 >= 'A' && var5 <= 'Z'; var5 = var2.charAt(var3)) {
               ++var3;
               if (var3 == var4) {
                  var5 = 0;
                  break;
               }
            }

            String var6 = null;
            if (var5 == ':') {
               var6 = var2.substring(0, var3).toLowerCase();
            }

            if (var6 != null) {
               if (!var6.equals(var1.getProtocol())) {
                  return this.parseURL(var2);
               }

               ++var3;
               if (var3 == var2.length()) {
                  return this.parseURL(var2);
               }

               if (var2.charAt(var3) == '/') {
                  return this.parseURL(var2);
               }

               var2 = var2.substring(var3);
            }

            if (var2.startsWith("/")) {
               return var2.length() > 1 && var2.charAt(1) == '/' ? this.parseURL(var1.getProtocol() + ":" + var2) : this.parseURL(var1.getPortStr() + var2);
            } else {
               String var7;
               if (var2.startsWith("#")) {
                  var7 = var1.getPortStr();
                  if (var1.getPath() != null) {
                     var7 = var7 + var1.getPath();
                  }

                  return this.parseURL(var7 + var2);
               } else {
                  var7 = var1.getPath();
                  if (var7 == null) {
                     var7 = "";
                  }

                  var3 = var7.lastIndexOf(47);
                  if (var3 == -1) {
                     var7 = "";
                  } else {
                     var7 = var7.substring(0, var3 + 1);
                  }

                  return this.parseURL(var1.getPortStr() + var7 + var2);
               }
            }
         }
      }
   }
}

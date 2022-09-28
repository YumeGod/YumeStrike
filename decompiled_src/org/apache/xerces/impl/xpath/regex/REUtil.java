package org.apache.xerces.impl.xpath.regex;

import java.text.CharacterIterator;

public final class REUtil {
   static final int CACHESIZE = 20;
   static final RegularExpression[] regexCache = new RegularExpression[20];

   private REUtil() {
   }

   static final int composeFromSurrogates(int var0, int var1) {
      return 65536 + (var0 - '\ud800' << 10) + var1 - '\udc00';
   }

   static final boolean isLowSurrogate(int var0) {
      return (var0 & 'ﰀ') == 56320;
   }

   static final boolean isHighSurrogate(int var0) {
      return (var0 & 'ﰀ') == 55296;
   }

   static final String decomposeToSurrogates(int var0) {
      char[] var1 = new char[2];
      var0 -= 65536;
      var1[0] = (char)((var0 >> 10) + '\ud800');
      var1[1] = (char)((var0 & 1023) + '\udc00');
      return new String(var1);
   }

   static final String substring(CharacterIterator var0, int var1, int var2) {
      char[] var3 = new char[var2 - var1];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = var0.setIndex(var4 + var1);
      }

      return new String(var3);
   }

   static final int getOptionValue(int var0) {
      short var1 = 0;
      switch (var0) {
         case 44:
            var1 = 1024;
            break;
         case 70:
            var1 = 256;
            break;
         case 72:
            var1 = 128;
            break;
         case 88:
            var1 = 512;
            break;
         case 105:
            var1 = 2;
            break;
         case 109:
            var1 = 8;
            break;
         case 115:
            var1 = 4;
            break;
         case 117:
            var1 = 32;
            break;
         case 119:
            var1 = 64;
            break;
         case 120:
            var1 = 16;
      }

      return var1;
   }

   static final int parseOptions(String var0) throws ParseException {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < var0.length(); ++var2) {
            int var3 = getOptionValue(var0.charAt(var2));
            if (var3 == 0) {
               throw new ParseException("Unknown Option: " + var0.substring(var2), -1);
            }

            var1 |= var3;
         }

         return var1;
      }
   }

   static final String createOptionString(int var0) {
      StringBuffer var1 = new StringBuffer(9);
      if ((var0 & 256) != 0) {
         var1.append('F');
      }

      if ((var0 & 128) != 0) {
         var1.append('H');
      }

      if ((var0 & 512) != 0) {
         var1.append('X');
      }

      if ((var0 & 2) != 0) {
         var1.append('i');
      }

      if ((var0 & 8) != 0) {
         var1.append('m');
      }

      if ((var0 & 4) != 0) {
         var1.append('s');
      }

      if ((var0 & 32) != 0) {
         var1.append('u');
      }

      if ((var0 & 64) != 0) {
         var1.append('w');
      }

      if ((var0 & 16) != 0) {
         var1.append('x');
      }

      if ((var0 & 1024) != 0) {
         var1.append(',');
      }

      return var1.toString().intern();
   }

   static String stripExtendedComment(String var0) {
      int var1 = var0.length();
      StringBuffer var2 = new StringBuffer(var1);
      int var3 = 0;

      while(true) {
         while(true) {
            char var4;
            do {
               do {
                  do {
                     do {
                        do {
                           if (var3 >= var1) {
                              return var2.toString();
                           }

                           var4 = var0.charAt(var3++);
                        } while(var4 == '\t');
                     } while(var4 == '\n');
                  } while(var4 == '\f');
               } while(var4 == '\r');
            } while(var4 == ' ');

            if (var4 == '#') {
               while(var3 < var1) {
                  var4 = var0.charAt(var3++);
                  if (var4 == '\r' || var4 == '\n') {
                     break;
                  }
               }
            } else if (var4 == '\\' && var3 < var1) {
               char var5;
               if ((var5 = var0.charAt(var3)) != '#' && var5 != '\t' && var5 != '\n' && var5 != '\f' && var5 != '\r' && var5 != ' ') {
                  var2.append('\\');
                  var2.append((char)var5);
                  ++var3;
               } else {
                  var2.append((char)var5);
                  ++var3;
               }
            } else {
               var2.append((char)var4);
            }
         }
      }
   }

   public static void main(String[] var0) {
      String var1 = null;

      String var3;
      int var4;
      try {
         String var2 = "";
         var3 = null;
         if (var0.length == 0) {
            System.out.println("Error:Usage: java REUtil -i|-m|-s|-u|-w|-X regularExpression String");
            System.exit(0);
         }

         for(var4 = 0; var4 < var0.length; ++var4) {
            if (var0[var4].length() != 0 && var0[var4].charAt(0) == '-') {
               if (var0[var4].equals("-i")) {
                  var2 = var2 + "i";
               } else if (var0[var4].equals("-m")) {
                  var2 = var2 + "m";
               } else if (var0[var4].equals("-s")) {
                  var2 = var2 + "s";
               } else if (var0[var4].equals("-u")) {
                  var2 = var2 + "u";
               } else if (var0[var4].equals("-w")) {
                  var2 = var2 + "w";
               } else if (var0[var4].equals("-X")) {
                  var2 = var2 + "X";
               } else {
                  System.err.println("Unknown option: " + var0[var4]);
               }
            } else if (var1 == null) {
               var1 = var0[var4];
            } else if (var3 == null) {
               var3 = var0[var4];
            } else {
               System.err.println("Unnecessary: " + var0[var4]);
            }
         }

         RegularExpression var10 = new RegularExpression(var1, var2);
         System.out.println("RegularExpression: " + var10);
         Match var6 = new Match();
         var10.matches(var3, var6);

         for(int var7 = 0; var7 < var6.getNumberOfGroups(); ++var7) {
            if (var7 == 0) {
               System.out.print("Matched range for the whole pattern: ");
            } else {
               System.out.print("[" + var7 + "]: ");
            }

            if (var6.getBeginning(var7) < 0) {
               System.out.println("-1");
            } else {
               System.out.print(var6.getBeginning(var7) + ", " + var6.getEnd(var7) + ", ");
               System.out.println("\"" + var6.getCapturedText(var7) + "\"");
            }
         }
      } catch (ParseException var8) {
         if (var1 == null) {
            var8.printStackTrace();
         } else {
            System.err.println("org.apache.xerces.utils.regex.ParseException: " + var8.getMessage());
            var3 = "        ";
            System.err.println(var3 + var1);
            var4 = var8.getLocation();
            if (var4 >= 0) {
               System.err.print(var3);

               for(int var5 = 0; var5 < var4; ++var5) {
                  System.err.print("-");
               }

               System.err.println("^");
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public static RegularExpression createRegex(String var0, String var1) throws ParseException {
      RegularExpression var2 = null;
      int var3 = parseOptions(var1);
      RegularExpression[] var4 = regexCache;
      synchronized(var4) {
         int var5;
         for(var5 = 0; var5 < 20; ++var5) {
            RegularExpression var6 = regexCache[var5];
            if (var6 == null) {
               var5 = -1;
               break;
            }

            if (var6.equals(var0, var3)) {
               var2 = var6;
               break;
            }
         }

         if (var2 != null) {
            if (var5 != 0) {
               System.arraycopy(regexCache, 0, regexCache, 1, var5);
               regexCache[0] = var2;
            }
         } else {
            var2 = new RegularExpression(var0, var1);
            System.arraycopy(regexCache, 0, regexCache, 1, 19);
            regexCache[0] = var2;
         }

         return var2;
      }
   }

   public static boolean matches(String var0, String var1) throws ParseException {
      return createRegex(var0, (String)null).matches(var1);
   }

   public static boolean matches(String var0, String var1, String var2) throws ParseException {
      return createRegex(var0, var1).matches(var2);
   }

   public static String quoteMeta(String var0) {
      int var1 = var0.length();
      StringBuffer var2 = null;

      for(int var3 = 0; var3 < var1; ++var3) {
         char var4 = var0.charAt(var3);
         if (".*+?{[()|\\^$".indexOf(var4) >= 0) {
            if (var2 == null) {
               var2 = new StringBuffer(var3 + (var1 - var3) * 2);
               if (var3 > 0) {
                  var2.append(var0.substring(0, var3));
               }
            }

            var2.append('\\');
            var2.append((char)var4);
         } else if (var2 != null) {
            var2.append((char)var4);
         }
      }

      return var2 != null ? var2.toString() : var0;
   }

   static void dumpString(String var0) {
      for(int var1 = 0; var1 < var0.length(); ++var1) {
         System.out.print(Integer.toHexString(var0.charAt(var1)));
         System.out.print(" ");
      }

      System.out.println();
   }
}

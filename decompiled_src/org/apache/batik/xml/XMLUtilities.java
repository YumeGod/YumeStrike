package org.apache.batik.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import org.apache.batik.util.EncodingUtilities;

public class XMLUtilities extends XMLCharacters {
   protected XMLUtilities() {
   }

   public static boolean isXMLSpace(char var0) {
      return var0 <= ' ' && (4294977024L >> var0 & 1L) != 0L;
   }

   public static boolean isXMLNameFirstCharacter(char var0) {
      return (NAME_FIRST_CHARACTER[var0 / 32] & 1 << var0 % 32) != 0;
   }

   public static boolean isXML11NameFirstCharacter(char var0) {
      return (NAME11_FIRST_CHARACTER[var0 / 32] & 1 << var0 % 32) != 0;
   }

   public static boolean isXMLNameCharacter(char var0) {
      return (NAME_CHARACTER[var0 / 32] & 1 << var0 % 32) != 0;
   }

   public static boolean isXML11NameCharacter(char var0) {
      return (NAME11_CHARACTER[var0 / 32] & 1 << var0 % 32) != 0;
   }

   public static boolean isXMLCharacter(int var0) {
      return (XML_CHARACTER[var0 >>> 5] & 1 << (var0 & 31)) != 0 || var0 >= 65536 && var0 <= 1114111;
   }

   public static boolean isXML11Character(int var0) {
      return var0 >= 1 && var0 <= 55295 || var0 >= 57344 && var0 <= 65533 || var0 >= 65536 && var0 <= 1114111;
   }

   public static boolean isXMLPublicIdCharacter(char var0) {
      return var0 < 128 && (PUBLIC_ID_CHARACTER[var0 / 32] & 1 << var0 % 32) != 0;
   }

   public static boolean isXMLVersionCharacter(char var0) {
      return var0 < 128 && (VERSION_CHARACTER[var0 / 32] & 1 << var0 % 32) != 0;
   }

   public static boolean isXMLAlphabeticCharacter(char var0) {
      return var0 < 128 && (ALPHABETIC_CHARACTER[var0 / 32] & 1 << var0 % 32) != 0;
   }

   public static Reader createXMLDocumentReader(InputStream var0) throws IOException {
      PushbackInputStream var1 = new PushbackInputStream(var0, 128);
      byte[] var2 = new byte[4];
      int var3 = var1.read(var2);
      if (var3 > 0) {
         var1.unread(var2, 0, var3);
      }

      if (var3 == 4) {
         Reader var4;
         String var5;
         switch (var2[0] & 255) {
            case 0:
               if (var2[1] == 60 && var2[2] == 0 && var2[3] == 63) {
                  return new InputStreamReader(var1, "UnicodeBig");
               }
               break;
            case 60:
               switch (var2[1] & 255) {
                  case 0:
                     if (var2[2] == 63 && var2[3] == 0) {
                        return new InputStreamReader(var1, "UnicodeLittle");
                     }

                     return new InputStreamReader(var1, "UTF8");
                  case 63:
                     if (var2[2] == 120 && var2[3] == 109) {
                        var4 = createXMLDeclarationReader(var1, "UTF8");
                        var5 = getXMLDeclarationEncoding(var4, "UTF8");
                        return new InputStreamReader(var1, var5);
                     }

                     return new InputStreamReader(var1, "UTF8");
                  default:
                     return new InputStreamReader(var1, "UTF8");
               }
            case 76:
               if (var2[1] == 111 && (var2[2] & 255) == 167 && (var2[3] & 255) == 148) {
                  var4 = createXMLDeclarationReader(var1, "CP037");
                  var5 = getXMLDeclarationEncoding(var4, "CP037");
                  return new InputStreamReader(var1, var5);
               }
               break;
            case 254:
               if ((var2[1] & 255) == 255) {
                  return new InputStreamReader(var1, "Unicode");
               }
               break;
            case 255:
               if ((var2[1] & 255) == 254) {
                  return new InputStreamReader(var1, "Unicode");
               }
         }
      }

      return new InputStreamReader(var1, "UTF8");
   }

   protected static Reader createXMLDeclarationReader(PushbackInputStream var0, String var1) throws IOException {
      byte[] var2 = new byte[128];
      int var3 = var0.read(var2);
      if (var3 > 0) {
         var0.unread(var2, 0, var3);
      }

      return new InputStreamReader(new ByteArrayInputStream(var2, 4, var3), var1);
   }

   protected static String getXMLDeclarationEncoding(Reader var0, String var1) throws IOException {
      if (var0.read() != 108) {
         return var1;
      } else if (!isXMLSpace((char)var0.read())) {
         return var1;
      } else {
         int var2;
         while(isXMLSpace((char)(var2 = var0.read()))) {
         }

         if (var2 != 118) {
            return var1;
         } else if (var0.read() != 101) {
            return var1;
         } else if (var0.read() != 114) {
            return var1;
         } else if (var0.read() != 115) {
            return var1;
         } else if (var0.read() != 105) {
            return var1;
         } else if (var0.read() != 111) {
            return var1;
         } else if (var0.read() != 110) {
            return var1;
         } else {
            for(var2 = var0.read(); isXMLSpace((char)var2); var2 = var0.read()) {
            }

            if (var2 != 61) {
               return var1;
            } else {
               while(isXMLSpace((char)(var2 = var0.read()))) {
               }

               if (var2 != 34 && var2 != 39) {
                  return var1;
               } else {
                  char var3 = (char)var2;

                  do {
                     var2 = var0.read();
                     if (var2 == var3) {
                        if (!isXMLSpace((char)var0.read())) {
                           return var1;
                        } else {
                           while(isXMLSpace((char)(var2 = var0.read()))) {
                           }

                           if (var2 != 101) {
                              return var1;
                           } else if (var0.read() != 110) {
                              return var1;
                           } else if (var0.read() != 99) {
                              return var1;
                           } else if (var0.read() != 111) {
                              return var1;
                           } else if (var0.read() != 100) {
                              return var1;
                           } else if (var0.read() != 105) {
                              return var1;
                           } else if (var0.read() != 110) {
                              return var1;
                           } else if (var0.read() != 103) {
                              return var1;
                           } else {
                              for(var2 = var0.read(); isXMLSpace((char)var2); var2 = var0.read()) {
                              }

                              if (var2 != 61) {
                                 return var1;
                              } else {
                                 while(isXMLSpace((char)(var2 = var0.read()))) {
                                 }

                                 if (var2 != 34 && var2 != 39) {
                                    return var1;
                                 } else {
                                    var3 = (char)var2;
                                    StringBuffer var4 = new StringBuffer();

                                    while(true) {
                                       var2 = var0.read();
                                       if (var2 == -1) {
                                          return var1;
                                       }

                                       if (var2 == var3) {
                                          return encodingToJavaEncoding(var4.toString(), var1);
                                       }

                                       var4.append((char)var2);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  } while(isXMLVersionCharacter((char)var2));

                  return var1;
               }
            }
         }
      }
   }

   public static String encodingToJavaEncoding(String var0, String var1) {
      String var2 = EncodingUtilities.javaEncoding(var0);
      return var2 == null ? var1 : var2;
   }
}

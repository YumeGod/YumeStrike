package org.apache.xml.serialize;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.dom.DOMMessageFormatter;

public final class HTMLdtd {
   public static final String HTMLPublicId = "-//W3C//DTD HTML 4.01//EN";
   public static final String HTMLSystemId = "http://www.w3.org/TR/html4/strict.dtd";
   public static final String XHTMLPublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
   public static final String XHTMLSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
   private static Hashtable _byChar;
   private static Hashtable _byName;
   private static Hashtable _boolAttrs;
   private static Hashtable _elemDefs = new Hashtable();
   private static final String ENTITIES_RESOURCE = "HTMLEntities.res";
   private static final int ONLY_OPENING = 1;
   private static final int ELEM_CONTENT = 2;
   private static final int PRESERVE = 4;
   private static final int OPT_CLOSING = 8;
   private static final int EMPTY = 17;
   private static final int ALLOWED_HEAD = 32;
   private static final int CLOSE_P = 64;
   private static final int CLOSE_DD_DT = 128;
   private static final int CLOSE_SELF = 256;
   private static final int CLOSE_TABLE = 512;
   private static final int CLOSE_TH_TD = 16384;
   // $FF: synthetic field
   static Class class$org$apache$xml$serialize$HTMLdtd;

   public static boolean isEmptyTag(String var0) {
      return isElement(var0, 17);
   }

   public static boolean isElementContent(String var0) {
      return isElement(var0, 2);
   }

   public static boolean isPreserveSpace(String var0) {
      return isElement(var0, 4);
   }

   public static boolean isOptionalClosing(String var0) {
      return isElement(var0, 8);
   }

   public static boolean isOnlyOpening(String var0) {
      return isElement(var0, 1);
   }

   public static boolean isClosing(String var0, String var1) {
      if (var1.equalsIgnoreCase("HEAD")) {
         return !isElement(var0, 32);
      } else if (var1.equalsIgnoreCase("P")) {
         return isElement(var0, 64);
      } else if (!var1.equalsIgnoreCase("DT") && !var1.equalsIgnoreCase("DD")) {
         if (!var1.equalsIgnoreCase("LI") && !var1.equalsIgnoreCase("OPTION")) {
            if (!var1.equalsIgnoreCase("THEAD") && !var1.equalsIgnoreCase("TFOOT") && !var1.equalsIgnoreCase("TBODY") && !var1.equalsIgnoreCase("TR") && !var1.equalsIgnoreCase("COLGROUP")) {
               return !var1.equalsIgnoreCase("TH") && !var1.equalsIgnoreCase("TD") ? false : isElement(var0, 16384);
            } else {
               return isElement(var0, 512);
            }
         } else {
            return isElement(var0, 256);
         }
      } else {
         return isElement(var0, 128);
      }
   }

   public static boolean isURI(String var0, String var1) {
      return var1.equalsIgnoreCase("href") || var1.equalsIgnoreCase("src");
   }

   public static boolean isBoolean(String var0, String var1) {
      String[] var2 = (String[])_boolAttrs.get(var0.toUpperCase(Locale.ENGLISH));
      if (var2 == null) {
         return false;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].equalsIgnoreCase(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public static int charFromName(String var0) {
      initialize();
      Object var1 = _byName.get(var0);
      return var1 != null && var1 instanceof Integer ? (Integer)var1 : -1;
   }

   public static String fromChar(int var0) {
      if (var0 > 65535) {
         return null;
      } else {
         initialize();
         String var1 = (String)_byChar.get(new Integer(var0));
         return var1;
      }
   }

   private static void initialize() {
      InputStream var0 = null;
      BufferedReader var1 = null;
      if (_byName == null) {
         try {
            _byName = new Hashtable();
            _byChar = new Hashtable();
            var0 = (class$org$apache$xml$serialize$HTMLdtd == null ? (class$org$apache$xml$serialize$HTMLdtd = class$("org.apache.xml.serialize.HTMLdtd")) : class$org$apache$xml$serialize$HTMLdtd).getResourceAsStream("HTMLEntities.res");
            if (var0 == null) {
               throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResourceNotFound", new Object[]{"HTMLEntities.res"}));
            } else {
               var1 = new BufferedReader(new InputStreamReader(var0, "ASCII"));
               String var6 = var1.readLine();

               while(true) {
                  while(var6 != null) {
                     if (var6.length() != 0 && var6.charAt(0) != '#') {
                        int var2 = var6.indexOf(32);
                        if (var2 > 1) {
                           String var3 = var6.substring(0, var2);
                           ++var2;
                           if (var2 < var6.length()) {
                              String var4 = var6.substring(var2);
                              var2 = var4.indexOf(32);
                              if (var2 > 0) {
                                 var4 = var4.substring(0, var2);
                              }

                              int var5 = Integer.parseInt(var4);
                              defineEntity(var3, (char)var5);
                           }
                        }

                        var6 = var1.readLine();
                     } else {
                        var6 = var1.readLine();
                     }
                  }

                  var0.close();
                  return;
               }
            }
         } catch (Exception var16) {
            throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResourceNotLoaded", new Object[]{"HTMLEntities.res", var16.toString()}));
         } finally {
            if (var0 != null) {
               try {
                  var0.close();
               } catch (Exception var15) {
               }
            }

         }
      }
   }

   private static void defineEntity(String var0, char var1) {
      if (_byName.get(var0) == null) {
         _byName.put(var0, new Integer(var1));
         _byChar.put(new Integer(var1), var0);
      }

   }

   private static void defineElement(String var0, int var1) {
      _elemDefs.put(var0, new Integer(var1));
   }

   private static void defineBoolean(String var0, String var1) {
      defineBoolean(var0, new String[]{var1});
   }

   private static void defineBoolean(String var0, String[] var1) {
      _boolAttrs.put(var0, var1);
   }

   private static boolean isElement(String var0, int var1) {
      Integer var2 = (Integer)_elemDefs.get(var0.toUpperCase(Locale.ENGLISH));
      if (var2 == null) {
         return false;
      } else {
         return (var2 & var1) == var1;
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      defineElement("ADDRESS", 64);
      defineElement("AREA", 17);
      defineElement("BASE", 49);
      defineElement("BASEFONT", 17);
      defineElement("BLOCKQUOTE", 64);
      defineElement("BODY", 8);
      defineElement("BR", 17);
      defineElement("COL", 17);
      defineElement("COLGROUP", 522);
      defineElement("DD", 137);
      defineElement("DIV", 64);
      defineElement("DL", 66);
      defineElement("DT", 137);
      defineElement("FIELDSET", 64);
      defineElement("FORM", 64);
      defineElement("FRAME", 25);
      defineElement("H1", 64);
      defineElement("H2", 64);
      defineElement("H3", 64);
      defineElement("H4", 64);
      defineElement("H5", 64);
      defineElement("H6", 64);
      defineElement("HEAD", 10);
      defineElement("HR", 81);
      defineElement("HTML", 10);
      defineElement("IMG", 17);
      defineElement("INPUT", 17);
      defineElement("ISINDEX", 49);
      defineElement("LI", 265);
      defineElement("LINK", 49);
      defineElement("MAP", 32);
      defineElement("META", 49);
      defineElement("OL", 66);
      defineElement("OPTGROUP", 2);
      defineElement("OPTION", 265);
      defineElement("P", 328);
      defineElement("PARAM", 17);
      defineElement("PRE", 68);
      defineElement("SCRIPT", 36);
      defineElement("NOSCRIPT", 36);
      defineElement("SELECT", 2);
      defineElement("STYLE", 36);
      defineElement("TABLE", 66);
      defineElement("TBODY", 522);
      defineElement("TD", 16392);
      defineElement("TEXTAREA", 4);
      defineElement("TFOOT", 522);
      defineElement("TH", 16392);
      defineElement("THEAD", 522);
      defineElement("TITLE", 32);
      defineElement("TR", 522);
      defineElement("UL", 66);
      _boolAttrs = new Hashtable();
      defineBoolean("AREA", "href");
      defineBoolean("BUTTON", "disabled");
      defineBoolean("DIR", "compact");
      defineBoolean("DL", "compact");
      defineBoolean("FRAME", "noresize");
      defineBoolean("HR", "noshade");
      defineBoolean("IMAGE", "ismap");
      defineBoolean("INPUT", new String[]{"defaultchecked", "checked", "readonly", "disabled"});
      defineBoolean("LINK", "link");
      defineBoolean("MENU", "compact");
      defineBoolean("OBJECT", "declare");
      defineBoolean("OL", "compact");
      defineBoolean("OPTGROUP", "disabled");
      defineBoolean("OPTION", new String[]{"default-selected", "selected", "disabled"});
      defineBoolean("SCRIPT", "defer");
      defineBoolean("SELECT", new String[]{"multiple", "disabled"});
      defineBoolean("STYLE", "disabled");
      defineBoolean("TD", "nowrap");
      defineBoolean("TH", "nowrap");
      defineBoolean("TEXTAREA", new String[]{"disabled", "readonly"});
      defineBoolean("UL", "compact");
      initialize();
   }
}

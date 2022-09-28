package com.xmlmind.fo.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Vector;

public final class Encoding {
   private static final Name[] names = new Name[]{new Name("ISO-8859-1", "ISO8859_1", (String)null), new Name("ISO-8859-2", "ISO8859_2", (String)null), new Name("ISO-8859-4", "ISO8859_4", (String)null), new Name("ISO-8859-5", "ISO8859_5", (String)null), new Name("ISO-8859-7", "ISO8859_7", (String)null), new Name("ISO-8859-9", "ISO8859_9", (String)null), new Name("ISO-8859-13", "ISO8859_13", (String)null), new Name("ISO-8859-15", "ISO8859_15", "ISO8859_15_FDIS"), new Name("KOI8-R", "KOI8_R", (String)null), new Name("US-ASCII", "ASCII", (String)null), new Name("UTF-8", "UTF8", (String)null), new Name("UTF-16", "UTF-16", (String)null), new Name("UTF-16BE", "UnicodeBigUnmarked", (String)null), new Name("UTF-16LE", "UnicodeLittleUnmarked", (String)null), new Name("windows-1250", "Cp1250", (String)null), new Name("windows-1251", "Cp1251", (String)null), new Name("windows-1252", "Cp1252", (String)null), new Name("windows-1253", "Cp1253", (String)null), new Name("windows-1254", "Cp1254", (String)null), new Name("windows-1257", "Cp1257", (String)null), new Name((String)null, "UnicodeBig", (String)null), new Name((String)null, "UnicodeLittle", (String)null), new Name("Big5", "Big5", (String)null), new Name("Big5-HKSCS", "Big5_HKSCS", (String)null), new Name("EUC-JP", "EUC_JP", (String)null), new Name("EUC-KR", "EUC_KR", (String)null), new Name("GB18030", "GB18030", (String)null), new Name("GB2312", "EUC_CN", (String)null), new Name("GBK", "GBK", (String)null), new Name("IBM-Thai", "Cp838", (String)null), new Name("IBM00858", "Cp858", (String)null), new Name("IBM01140", "Cp1140", (String)null), new Name("IBM01141", "Cp1141", (String)null), new Name("IBM01142", "Cp1142", (String)null), new Name("IBM01143", "Cp1143", (String)null), new Name("IBM01144", "Cp1144", (String)null), new Name("IBM01145", "Cp1145", (String)null), new Name("IBM01146", "Cp1146", (String)null), new Name("IBM01147", "Cp1147", (String)null), new Name("IBM01148", "Cp1148", (String)null), new Name("IBM01149", "Cp1149", (String)null), new Name("IBM037", "Cp037", (String)null), new Name("IBM1026", "Cp1026", (String)null), new Name("IBM1047", "Cp1047", (String)null), new Name("IBM273", "Cp273", (String)null), new Name("IBM277", "Cp277", (String)null), new Name("IBM278", "Cp278", (String)null), new Name("IBM280", "Cp280", (String)null), new Name("IBM284", "Cp284", (String)null), new Name("IBM285", "Cp285", (String)null), new Name("IBM297", "Cp297", (String)null), new Name("IBM297", "Cp297", (String)null), new Name("IBM420", "Cp420", (String)null), new Name("IBM424", "Cp424", (String)null), new Name("IBM437", "Cp437", (String)null), new Name("IBM500", "Cp500", (String)null), new Name("IBM775", "Cp775", (String)null), new Name("IBM850", "Cp850", (String)null), new Name("IBM852", "Cp852", (String)null), new Name("IBM855", "Cp855", (String)null), new Name("IBM857", "Cp857", (String)null), new Name("IBM860", "Cp860", (String)null), new Name("IBM861", "Cp861", (String)null), new Name("IBM862", "Cp862", (String)null), new Name("IBM863", "Cp863", (String)null), new Name("IBM864", "Cp864", (String)null), new Name("IBM865", "Cp865", (String)null), new Name("IBM866", "Cp866", (String)null), new Name("IBM868", "Cp868", (String)null), new Name("IBM869", "Cp869", (String)null), new Name("IBM870", "Cp870", (String)null), new Name("IBM871", "Cp871", (String)null), new Name("IBM918", "Cp918", (String)null), new Name("ISO-2022-CN", "ISO2022CN", (String)null), new Name("ISO-2022-JP", "ISO2022JP", (String)null), new Name("ISO-2022-KR", "ISO2022KR", (String)null), new Name("ISO-8859-3", "ISO8859_3", (String)null), new Name("ISO-8859-6", "ISO8859_6", (String)null), new Name("ISO-8859-8", "ISO8859_8", (String)null), new Name("Shift_JIS", "SJIS", (String)null), new Name("TIS-620", "TIS620", (String)null), new Name("windows-1255", "Cp1255", (String)null), new Name("windows-1256", "Cp1256", (String)null), new Name("windows-1258", "Cp1258", (String)null), new Name("windows-31j", "MS932", (String)null), new Name("x-Big5_Solaris", "Big5_Solaris", (String)null), new Name("x-euc-jp-linux", "EUC_JP_LINUX", (String)null), new Name("x-EUC-TW", "EUC_TW", (String)null), new Name("x-eucJP-Open", "EUC_JP_Solaris", (String)null), new Name("x-IBM1006", "Cp1006", (String)null), new Name("x-IBM1025", "Cp1025", (String)null), new Name("x-IBM1046", "Cp1046", (String)null), new Name("x-IBM1097", "Cp1097", (String)null), new Name("x-IBM1098", "Cp1098", (String)null), new Name("x-IBM1112", "Cp1112", (String)null), new Name("x-IBM1122", "Cp1122", (String)null), new Name("x-IBM1123", "Cp1123", (String)null), new Name("x-IBM1124", "Cp1124", (String)null), new Name("x-IBM1381", "Cp1381", (String)null), new Name("x-IBM1383", "Cp1383", (String)null), new Name("x-IBM33722", "Cp33722", (String)null), new Name("x-IBM737", "Cp737", (String)null), new Name("x-IBM856", "Cp856", (String)null), new Name("x-IBM874", "Cp874", (String)null), new Name("x-IBM875", "Cp875", (String)null), new Name("x-IBM921", "Cp921", (String)null), new Name("x-IBM922", "Cp922", (String)null), new Name("x-IBM930", "Cp930", (String)null), new Name("x-IBM933", "Cp933", (String)null), new Name("x-IBM935", "Cp935", (String)null), new Name("x-IBM937", "Cp937", (String)null), new Name("x-IBM939", "Cp939", (String)null), new Name("x-IBM942", "Cp942", (String)null), new Name("x-IBM942C", "Cp942C", (String)null), new Name("x-IBM943", "Cp943", (String)null), new Name("x-IBM943C", "Cp943C", (String)null), new Name("x-IBM948", "Cp948", (String)null), new Name("x-IBM949", "Cp949", (String)null), new Name("x-IBM949C", "Cp949C", (String)null), new Name("x-IBM950", "Cp950", (String)null), new Name("x-IBM964", "Cp964", (String)null), new Name("x-IBM970", "Cp970", (String)null), new Name("x-ISCII91", "ISCII91", (String)null), new Name("x-ISO2022-CN-CNS", "ISO2022_CN_CNS", (String)null), new Name("x-ISO2022-CN-GB", "ISO2022_CN_GB", (String)null), new Name("x-iso-8859-11", "x-iso-8859-11", (String)null), new Name("x-JISAutoDetect", "JISAutoDetect", (String)null), new Name("x-Johab", "x-Johab", (String)null), new Name("x-MacArabic", "MacArabic", (String)null), new Name("x-MacCentralEurope", "MacCentralEurope", (String)null), new Name("x-MacCroatian", "MacCroatian", (String)null), new Name("x-MacCyrillic", "MacCyrillic", (String)null), new Name("x-MacDingBat", "MacDingBat", (String)null), new Name("x-MacGreek", "MacGreek", (String)null), new Name("x-MacHebrew", "MacHebrew", (String)null), new Name("x-MacIceland", "MacIceland", (String)null), new Name("x-MacRoman", "MacRoman", (String)null), new Name("x-MacRomania", "MacRomania", (String)null), new Name("x-MacSymbol", "MacSymbol", (String)null), new Name("x-MacThai", "MacThai", (String)null), new Name("x-MacTurkish", "MacTurkish", (String)null), new Name("x-MacUkraine", "MacUkraine", (String)null), new Name("x-MS950-HKSCS", "MS950_HKSCS", (String)null), new Name("x-mswin-936", "MS936", (String)null), new Name("x-PCK", "PCK", (String)null), new Name("x-windows-874", "MS874", (String)null), new Name("x-windows-949", "MS949", (String)null), new Name("x-windows-950", "MS950", (String)null)};
   private static Hashtable lookup = new Hashtable();

   public static String officialName(String var0) {
      Integer var1 = (Integer)lookup.get(var0.toUpperCase());
      return var1 != null ? names[var1].official : null;
   }

   public static String internalName(String var0) {
      Integer var1 = (Integer)lookup.get(var0.toUpperCase());
      if (var1 != null) {
         int var2 = var1;
         var0 = names[var2].internal;
         if (names[var2].variant != null) {
            String var3 = System.getProperty("java.version");
            if (var0.equals("ISO8859_15") && var3 != null && var3.compareTo("1.4") < 0) {
               var0 = names[var2].variant;
            }
         }

         return var0;
      } else {
         return null;
      }
   }

   public static String[] list() {
      Vector var0 = new Vector();
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();

      for(int var3 = 0; var3 < names.length; ++var3) {
         OutputStreamWriter var2;
         try {
            var2 = new OutputStreamWriter(var1, names[var3].internal);
            var0.addElement(names[var3].internal);
            var2.close();
         } catch (Throwable var7) {
            if (names[var3].variant != null) {
               try {
                  var2 = new OutputStreamWriter(var1, names[var3].variant);
                  var0.addElement(names[var3].variant);
                  var2.close();
               } catch (Throwable var6) {
               }
            }
         }
      }

      String[] var8 = new String[var0.size()];

      for(int var4 = 0; var4 < var8.length; ++var4) {
         var8[var4] = (String)var0.elementAt(var4);
      }

      return var8;
   }

   static {
      for(int var0 = 0; var0 < names.length; ++var0) {
         Integer var1 = new Integer(var0);
         if (names[var0].official != null) {
            lookup.put(names[var0].official.toUpperCase(), var1);
         }

         lookup.put(names[var0].internal.toUpperCase(), var1);
         if (names[var0].variant != null) {
            lookup.put(names[var0].variant.toUpperCase(), var1);
         }
      }

   }

   private static final class Name {
      final String official;
      final String internal;
      final String variant;

      Name(String var1, String var2, String var3) {
         this.official = var1;
         this.internal = var2;
         this.variant = var3;
      }
   }
}

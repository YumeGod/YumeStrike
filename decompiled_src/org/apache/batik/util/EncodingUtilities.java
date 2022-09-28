package org.apache.batik.util;

import java.util.HashMap;
import java.util.Map;

public class EncodingUtilities {
   protected static final Map ENCODINGS = new HashMap();

   protected EncodingUtilities() {
   }

   public static String javaEncoding(String var0) {
      return (String)ENCODINGS.get(var0.toUpperCase());
   }

   static {
      ENCODINGS.put("UTF-8", "UTF8");
      ENCODINGS.put("UTF-16", "Unicode");
      ENCODINGS.put("US-ASCII", "ASCII");
      ENCODINGS.put("ISO-8859-1", "8859_1");
      ENCODINGS.put("ISO-8859-2", "8859_2");
      ENCODINGS.put("ISO-8859-3", "8859_3");
      ENCODINGS.put("ISO-8859-4", "8859_4");
      ENCODINGS.put("ISO-8859-5", "8859_5");
      ENCODINGS.put("ISO-8859-6", "8859_6");
      ENCODINGS.put("ISO-8859-7", "8859_7");
      ENCODINGS.put("ISO-8859-8", "8859_8");
      ENCODINGS.put("ISO-8859-9", "8859_9");
      ENCODINGS.put("ISO-2022-JP", "JIS");
      ENCODINGS.put("WINDOWS-31J", "MS932");
      ENCODINGS.put("EUC-JP", "EUCJIS");
      ENCODINGS.put("GB2312", "GB2312");
      ENCODINGS.put("BIG5", "Big5");
      ENCODINGS.put("EUC-KR", "KSC5601");
      ENCODINGS.put("ISO-2022-KR", "ISO2022KR");
      ENCODINGS.put("KOI8-R", "KOI8_R");
      ENCODINGS.put("EBCDIC-CP-US", "Cp037");
      ENCODINGS.put("EBCDIC-CP-CA", "Cp037");
      ENCODINGS.put("EBCDIC-CP-NL", "Cp037");
      ENCODINGS.put("EBCDIC-CP-WT", "Cp037");
      ENCODINGS.put("EBCDIC-CP-DK", "Cp277");
      ENCODINGS.put("EBCDIC-CP-NO", "Cp277");
      ENCODINGS.put("EBCDIC-CP-FI", "Cp278");
      ENCODINGS.put("EBCDIC-CP-SE", "Cp278");
      ENCODINGS.put("EBCDIC-CP-IT", "Cp280");
      ENCODINGS.put("EBCDIC-CP-ES", "Cp284");
      ENCODINGS.put("EBCDIC-CP-GB", "Cp285");
      ENCODINGS.put("EBCDIC-CP-FR", "Cp297");
      ENCODINGS.put("EBCDIC-CP-AR1", "Cp420");
      ENCODINGS.put("EBCDIC-CP-HE", "Cp424");
      ENCODINGS.put("EBCDIC-CP-BE", "Cp500");
      ENCODINGS.put("EBCDIC-CP-CH", "Cp500");
      ENCODINGS.put("EBCDIC-CP-ROECE", "Cp870");
      ENCODINGS.put("EBCDIC-CP-YU", "Cp870");
      ENCODINGS.put("EBCDIC-CP-IS", "Cp871");
      ENCODINGS.put("EBCDIC-CP-AR2", "Cp918");
      ENCODINGS.put("CP1252", "Cp1252");
   }
}

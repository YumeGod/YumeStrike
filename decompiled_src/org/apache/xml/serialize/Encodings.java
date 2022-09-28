package org.apache.xml.serialize;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.util.EncodingMap;

public class Encodings {
   static final int DEFAULT_LAST_PRINTABLE = 127;
   static final int LAST_PRINTABLE_UNICODE = 65535;
   static final String[] UNICODE_ENCODINGS = new String[]{"Unicode", "UnicodeBig", "UnicodeLittle", "GB2312", "UTF8", "UTF-16"};
   static final String DEFAULT_ENCODING = "UTF8";
   static Hashtable _encodings = new Hashtable();
   static final String JIS_DANGER_CHARS = "\\~\u007f¢£¥¬—―‖…‾‾∥∯〜＼～￠￡￢￣";

   static EncodingInfo getEncodingInfo(String var0, boolean var1) throws UnsupportedEncodingException {
      EncodingInfo var2 = null;
      if (var0 == null) {
         if ((var2 = (EncodingInfo)_encodings.get("UTF8")) != null) {
            return var2;
         } else {
            var2 = new EncodingInfo(EncodingMap.getJava2IANAMapping("UTF8"), "UTF8", 65535);
            _encodings.put("UTF8", var2);
            return var2;
         }
      } else {
         var0 = var0.toUpperCase(Locale.ENGLISH);
         String var3 = EncodingMap.getIANA2JavaMapping(var0);
         int var4;
         if (var3 == null) {
            if (!var1) {
               throw new UnsupportedEncodingException(var0);
            } else {
               EncodingInfo.testJavaEncodingName(var0);
               if ((var2 = (EncodingInfo)_encodings.get(var0)) != null) {
                  return var2;
               } else {
                  for(var4 = 0; var4 < UNICODE_ENCODINGS.length; ++var4) {
                     if (UNICODE_ENCODINGS[var4].equalsIgnoreCase(var0)) {
                        var2 = new EncodingInfo(EncodingMap.getJava2IANAMapping(var0), var0, 65535);
                        break;
                     }
                  }

                  if (var4 == UNICODE_ENCODINGS.length) {
                     var2 = new EncodingInfo(EncodingMap.getJava2IANAMapping(var0), var0, 127);
                  }

                  _encodings.put(var0, var2);
                  return var2;
               }
            }
         } else if ((var2 = (EncodingInfo)_encodings.get(var3)) != null) {
            return var2;
         } else {
            for(var4 = 0; var4 < UNICODE_ENCODINGS.length; ++var4) {
               if (UNICODE_ENCODINGS[var4].equalsIgnoreCase(var3)) {
                  var2 = new EncodingInfo(var0, var3, 65535);
                  break;
               }
            }

            if (var4 == UNICODE_ENCODINGS.length) {
               var2 = new EncodingInfo(var0, var3, 127);
            }

            _encodings.put(var3, var2);
            return var2;
         }
      }
   }
}

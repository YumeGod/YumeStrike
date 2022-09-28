package com.xmlmind.fo.util;

import java.nio.charset.Charset;

public class EncoderFactory {
   public static Encoder newEncoder(String var0) {
      try {
         String var1 = Encoding.officialName(var0);
         Charset var2 = Charset.forName(var1);
         return new Encoder(var2.newEncoder());
      } catch (Exception var3) {
         return null;
      }
   }
}

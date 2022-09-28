package com.xmlmind.fo.util;

import java.nio.charset.CharsetEncoder;

public class Encoder {
   private CharsetEncoder encoder;

   public Encoder(CharsetEncoder var1) {
      this.encoder = var1;
   }

   public boolean canEncode(char var1) {
      return this.encoder.canEncode(String.valueOf(var1));
   }
}

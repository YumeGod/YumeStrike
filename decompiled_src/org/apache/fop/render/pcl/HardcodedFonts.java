package org.apache.fop.render.pcl;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class HardcodedFonts {
   private static Log log;

   public static boolean setFont(PCLGenerator gen, String name, int size, String text) throws IOException {
      byte[] encoded = text.getBytes("ISO-8859-1");
      int i = 0;

      for(int c = encoded.length; i < c; ++i) {
         if (encoded[i] == 63 && text.charAt(i) != '?') {
            return false;
         }
      }

      return selectFont(gen, name, size);
   }

   private static boolean selectFont(PCLGenerator gen, String name, int size) throws IOException {
      int fontcode = 0;
      if (name.length() > 1 && name.charAt(0) == 'F') {
         try {
            fontcode = Integer.parseInt(name.substring(1));
         } catch (Exception var5) {
            log.error(var5);
         }
      }

      String formattedSize = gen.formatDouble2((double)size / 1000.0);
      switch (fontcode) {
         case 1:
            gen.writeCommand("(0N");
            gen.writeCommand("(s1p" + formattedSize + "v0s0b16602T");
            break;
         case 2:
            gen.writeCommand("(0N");
            gen.writeCommand("(s1p" + formattedSize + "v1s0b16602T");
            break;
         case 3:
            gen.writeCommand("(0N");
            gen.writeCommand("(s1p" + formattedSize + "v0s3b16602T");
            break;
         case 4:
            gen.writeCommand("(0N");
            gen.writeCommand("(s1p" + formattedSize + "v1s3b16602T");
            break;
         case 5:
            gen.writeCommand("(0N");
            gen.writeCommand("(s1p" + formattedSize + "v0s0b16901T");
            break;
         case 6:
            gen.writeCommand("(0N");
            gen.writeCommand("(s1p" + formattedSize + "v1s0b16901T");
            break;
         case 7:
            gen.writeCommand("(0N");
            gen.writeCommand("(s1p" + formattedSize + "v0s3b16901T");
            break;
         case 8:
            gen.writeCommand("(0N");
            gen.writeCommand("(s1p" + formattedSize + "v1s3b16901T");
            break;
         case 9:
            gen.writeCommand("(0N");
            gen.writeCommand("(s0p" + gen.formatDouble2((double)(120.01F / ((float)size / 1000.0F))) + "h0s0b4099T");
            break;
         case 10:
            gen.writeCommand("(0N");
            gen.writeCommand("(s0p" + gen.formatDouble2((double)(120.01F / ((float)size / 1000.0F))) + "h1s0b4099T");
            break;
         case 11:
            gen.writeCommand("(0N");
            gen.writeCommand("(s0p" + gen.formatDouble2((double)(120.01F / ((float)size / 1000.0F))) + "h0s3b4099T");
            break;
         case 12:
            gen.writeCommand("(0N");
            gen.writeCommand("(s0p" + gen.formatDouble2((double)(120.01F / ((float)size / 1000.0F))) + "h1s3b4099T");
            break;
         case 13:
            return false;
         case 14:
            return false;
         default:
            return false;
      }

      return true;
   }

   static {
      log = LogFactory.getLog(HardcodedFonts.class);
   }
}

package org.apache.fop.util;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.UnsupportedEncodingException;

public class ColorProfileUtil {
   public static String getICCProfileDescription(ICC_Profile profile) {
      byte[] data = profile.getData(1684370275);
      if (data == null) {
         return null;
      } else {
         int length = data[8] << 24 | data[9] << 16 | data[10] << 8 | data[11];
         --length;

         try {
            return new String(data, 12, length, "US-ASCII");
         } catch (UnsupportedEncodingException var4) {
            throw new UnsupportedOperationException("Incompatible VM");
         }
      }
   }

   public static boolean isDefaultsRGB(ICC_Profile profile) {
      ColorSpace sRGB = ColorSpace.getInstance(1000);
      ICC_Profile sRGBProfile = null;
      if (sRGB instanceof ICC_ColorSpace) {
         sRGBProfile = ((ICC_ColorSpace)sRGB).getProfile();
      }

      return profile == sRGBProfile;
   }
}

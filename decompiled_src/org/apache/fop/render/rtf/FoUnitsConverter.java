package org.apache.fop.render.rtf;

import java.util.HashMap;
import java.util.Map;
import org.apache.fop.apps.FOPException;

final class FoUnitsConverter {
   private static final FoUnitsConverter INSTANCE = new FoUnitsConverter();
   public static final float POINT_TO_TWIPS = 20.0F;
   public static final float IN_TO_TWIPS = 1440.0F;
   public static final float MM_TO_TWIPS = 56.692913F;
   public static final float CM_TO_TWIPS = 566.92914F;
   private static final Map TWIP_FACTORS = new HashMap();

   private FoUnitsConverter() {
   }

   static FoUnitsConverter getInstance() {
      return INSTANCE;
   }

   float convertToTwips(String foValue) throws FOPException {
      foValue = foValue.trim();
      StringBuffer number = new StringBuffer();
      StringBuffer units = new StringBuffer();

      for(int i = 0; i < foValue.length(); ++i) {
         char c = foValue.charAt(i);
         if (!Character.isDigit(c) && c != '.') {
            units.append(foValue.substring(i).trim());
            break;
         }

         number.append(c);
      }

      return this.numberToTwips(number.toString(), units.toString());
   }

   private float numberToTwips(String number, String units) throws FOPException {
      float result = 0.0F;

      try {
         if (number != null && number.trim().length() > 0) {
            result = Float.valueOf(number);
         }
      } catch (Exception var5) {
         throw new FOPException("number format error: cannot convert '" + number + "' to float value");
      }

      if (units != null && units.trim().length() > 0) {
         Float factor = (Float)TWIP_FACTORS.get(units.toLowerCase());
         if (factor == null) {
            throw new FOPException("conversion factor not found for '" + units + "' units");
         }

         result *= factor;
      }

      return result;
   }

   int convertFontSize(String size) throws FOPException {
      size = size.trim();
      String sFONTSUFFIX = "pt";
      if (!size.endsWith("pt")) {
         throw new FOPException("Invalid font size '" + size + "', must end with '" + "pt" + "'");
      } else {
         float result = 0.0F;
         size = size.substring(0, size.length() - "pt".length());

         try {
            result = Float.valueOf(size);
         } catch (Exception var5) {
            throw new FOPException("Invalid font size value '" + size + "'");
         }

         return (int)((double)result * 2.0);
      }
   }

   public float convertMptToTwips(int width) {
      return (float)width * 20.0F / 1000.0F;
   }

   static {
      TWIP_FACTORS.put("mm", new Float(56.692913F));
      TWIP_FACTORS.put("cm", new Float(566.92914F));
      TWIP_FACTORS.put("pt", new Float(20.0F));
      TWIP_FACTORS.put("in", new Float(1440.0F));
   }
}

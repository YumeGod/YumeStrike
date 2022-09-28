package org.apache.fop.render.rtf;

import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfAttributes;

public final class BorderAttributesConverter {
   private BorderAttributesConverter() {
   }

   public static void makeBorder(CommonBorderPaddingBackground border, int side, RtfAttributes attributes, String controlWord) {
      int styleEnum = border.getBorderStyle(side);
      if (styleEnum != 95) {
         FOPRtfAttributes attrs = new FOPRtfAttributes();
         attrs.set("brdrcf", border.getBorderColor(side));
         attrs.set(convertAttributetoRtf(styleEnum));
         attrs.set("brdrw", border.getBorderWidth(side, false) / 50);
         attributes.set(controlWord, (RtfAttributes)attrs);
      } else {
         double paddingPt = (double)border.getPadding(side, false, (PercentBaseContext)null) / 1000.0;
         int padding = (int)Math.round(paddingPt * 20.0);
         if (padding != 0) {
            if (side == 0) {
               attributes.addIntegerValue(padding, "sb");
            } else if (side == 1) {
               attributes.addIntegerValue(padding, "sa");
            }
         }
      }

   }

   public static String convertAttributetoRtf(int iBorderStyle) {
      if (iBorderStyle == 95) {
         return "brdrnil";
      } else if (iBorderStyle == 133) {
         return "brdrs";
      } else if (iBorderStyle == 37) {
         return "brdrdb";
      } else if (iBorderStyle == 36) {
         return "brdrdot";
      } else if (iBorderStyle == 31) {
         return "brdrdash";
      } else if (iBorderStyle == 55) {
         return "brdrengrave";
      } else if (iBorderStyle == 119) {
         return "brdremboss";
      } else if (iBorderStyle == 67) {
         return "brdrengrave";
      } else {
         return iBorderStyle == 101 ? "brdremboss" : "brdrs";
      }
   }
}

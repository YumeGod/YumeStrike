package org.apache.fop.render.extensions.prepress;

import java.awt.geom.Point2D;
import java.text.MessageFormat;
import java.util.regex.Pattern;
import org.apache.xmlgraphics.util.QName;

public final class PageScale {
   public static final QName EXT_PAGE_SCALE = new QName("http://xmlgraphics.apache.org/fop/extensions", (String)null, "scale");
   private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

   private PageScale() {
   }

   public static Point2D getScale(String scale) {
      String err = "Extension 'scale' attribute has incorrect value(s): {0}";
      if (scale != null && !scale.equals("")) {
         String[] scales = WHITESPACE_PATTERN.split(scale);

         double scaleX;
         try {
            scaleX = Double.parseDouble(scales[0]);
         } catch (NumberFormatException var9) {
            throw new IllegalArgumentException(MessageFormat.format("Extension 'scale' attribute has incorrect value(s): {0}", scale));
         }

         double var5;
         switch (scales.length) {
            case 1:
               var5 = scaleX;
               break;
            case 2:
               try {
                  var5 = Double.parseDouble(scales[1]);
                  break;
               } catch (NumberFormatException var8) {
                  throw new IllegalArgumentException(MessageFormat.format("Extension 'scale' attribute has incorrect value(s): {0}", scale));
               }
            default:
               throw new IllegalArgumentException("Too many arguments");
         }

         if (!(scaleX <= 0.0) && !(var5 <= 0.0)) {
            return new Point2D.Double(scaleX, var5);
         } else {
            throw new IllegalArgumentException(MessageFormat.format("Extension 'scale' attribute has incorrect value(s): {0}", scale));
         }
      } else {
         return null;
      }
   }
}

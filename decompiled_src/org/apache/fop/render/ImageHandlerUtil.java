package org.apache.fop.render;

import java.util.Map;
import org.apache.xmlgraphics.util.QName;

public class ImageHandlerUtil {
   public static final QName CONVERSION_MODE = new QName("http://xmlgraphics.apache.org/fop/extensions", (String)null, "conversion-mode");
   public static final String CONVERSION_MODE_BITMAP = "bitmap";

   public static boolean isConversionModeBitmap(String mode) {
      return "bitmap".equalsIgnoreCase(mode);
   }

   public static boolean isConversionModeBitmap(Map foreignAttributes) {
      if (foreignAttributes == null) {
         return false;
      } else {
         String conversionMode = (String)foreignAttributes.get(CONVERSION_MODE);
         return isConversionModeBitmap(conversionMode);
      }
   }
}

package org.apache.fop.render.intermediate;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.util.DecimalFormatCache;

public class IFUtil {
   private static String format(double value) {
      if (value == -0.0) {
         value = 0.0;
      }

      return DecimalFormatCache.getDecimalFormat(6).format(value);
   }

   public static StringBuffer toString(AffineTransform transform, StringBuffer sb) {
      if (transform.isIdentity()) {
         return sb;
      } else {
         double[] matrix = new double[6];
         transform.getMatrix(matrix);
         if (matrix[0] == 1.0 && matrix[3] == 1.0 && matrix[1] == 0.0 && matrix[2] == 0.0) {
            sb.append("translate(");
            sb.append(format(matrix[4]));
            if (matrix[5] != 0.0) {
               sb.append(',').append(format(matrix[5]));
            }
         } else {
            sb.append("matrix(");

            for(int i = 0; i < 6; ++i) {
               if (i > 0) {
                  sb.append(',');
               }

               sb.append(format(matrix[i]));
            }
         }

         sb.append(')');
         return sb;
      }
   }

   public static StringBuffer toString(AffineTransform[] transforms, StringBuffer sb) {
      int i = 0;

      for(int c = transforms.length; i < c; ++i) {
         if (i > 0) {
            sb.append(' ');
         }

         toString(transforms[i], sb);
      }

      return sb;
   }

   public static String toString(AffineTransform[] transforms) {
      return toString(transforms, new StringBuffer()).toString();
   }

   public static String toString(AffineTransform transform) {
      return toString(transform, new StringBuffer()).toString();
   }

   public static String toString(int[] coordinates) {
      if (coordinates == null) {
         return "";
      } else {
         StringBuffer sb = new StringBuffer();
         int i = 0;

         for(int c = coordinates.length; i < c; ++i) {
            if (i > 0) {
               sb.append(' ');
            }

            sb.append(Integer.toString(coordinates[i]));
         }

         return sb.toString();
      }
   }

   public static String toString(Rectangle rect) {
      if (rect == null) {
         return "";
      } else {
         StringBuffer sb = new StringBuffer();
         sb.append(rect.x).append(' ').append(rect.y).append(' ');
         sb.append(rect.width).append(' ').append(rect.height);
         return sb.toString();
      }
   }

   public static void setupFonts(IFDocumentHandler documentHandler, FontInfo fontInfo) throws FOPException {
      if (fontInfo == null) {
         fontInfo = new FontInfo();
      }

      if (documentHandler instanceof IFSerializer) {
         IFSerializer serializer = (IFSerializer)documentHandler;
         if (serializer.getMimickedDocumentHandler() != null) {
            documentHandler = serializer.getMimickedDocumentHandler();
         }
      }

      IFDocumentHandlerConfigurator configurator = documentHandler.getConfigurator();
      if (configurator != null) {
         configurator.setupFontInfo(documentHandler, fontInfo);
      } else {
         documentHandler.setDefaultFontInfo(fontInfo);
      }

   }

   public static void setupFonts(IFDocumentHandler documentHandler) throws FOPException {
      setupFonts(documentHandler, (FontInfo)null);
   }

   public static String getEffectiveMIMEType(IFDocumentHandler documentHandler) {
      if (documentHandler instanceof IFSerializer) {
         IFDocumentHandler mimic = ((IFSerializer)documentHandler).getMimickedDocumentHandler();
         if (mimic != null) {
            return mimic.getMimeType();
         }
      }

      return documentHandler.getMimeType();
   }
}

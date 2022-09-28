package org.apache.fop.render.pdf;

import java.awt.geom.AffineTransform;
import org.apache.fop.area.CTM;
import org.apache.fop.pdf.PDFNumber;

public final class CTMHelper {
   public static String toPDFString(CTM sourceMatrix) {
      if (null == sourceMatrix) {
         throw new NullPointerException("sourceMatrix must not be null");
      } else {
         double[] matrix = toPDFArray(sourceMatrix);
         return constructPDFArray(matrix);
      }
   }

   public static String toPDFString(AffineTransform transform, boolean convertMillipoints) {
      if (null == transform) {
         throw new NullPointerException("transform must not be null");
      } else {
         double[] matrix = new double[6];
         transform.getMatrix(matrix);
         if (convertMillipoints) {
            matrix[4] /= 1000.0;
            matrix[5] /= 1000.0;
         }

         return constructPDFArray(matrix);
      }
   }

   private static String constructPDFArray(double[] matrix) {
      return PDFNumber.doubleOut(matrix[0], 8) + " " + PDFNumber.doubleOut(matrix[1], 8) + " " + PDFNumber.doubleOut(matrix[2], 8) + " " + PDFNumber.doubleOut(matrix[3], 8) + " " + PDFNumber.doubleOut(matrix[4], 8) + " " + PDFNumber.doubleOut(matrix[5], 8);
   }

   public static CTM toPDFCTM(CTM sourceMatrix) {
      if (null == sourceMatrix) {
         throw new NullPointerException("sourceMatrix must not be null");
      } else {
         double[] matrix = toPDFArray(sourceMatrix);
         return new CTM(matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
      }
   }

   public static double[] toPDFArray(CTM sourceMatrix) {
      if (null == sourceMatrix) {
         throw new NullPointerException("sourceMatrix must not be null");
      } else {
         double[] matrix = sourceMatrix.toArray();
         return new double[]{matrix[0], matrix[1], matrix[2], matrix[3], matrix[4] / 1000.0, matrix[5] / 1000.0};
      }
   }
}

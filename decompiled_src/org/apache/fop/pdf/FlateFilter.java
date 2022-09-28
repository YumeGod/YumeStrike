package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.xmlgraphics.util.io.FlateEncodeOutputStream;

public class FlateFilter extends PDFFilter {
   public static final int PREDICTION_NONE = 1;
   public static final int PREDICTION_TIFF2 = 2;
   public static final int PREDICTION_PNG_NONE = 10;
   public static final int PREDICTION_PNG_SUB = 11;
   public static final int PREDICTION_PNG_UP = 12;
   public static final int PREDICTION_PNG_AVG = 13;
   public static final int PREDICTION_PNG_PAETH = 14;
   public static final int PREDICTION_PNG_OPT = 15;
   private int predictor = 1;
   private int colors;
   private int bitsPerComponent;
   private int columns;

   public String getName() {
      return "/FlateDecode";
   }

   public PDFObject getDecodeParms() {
      if (this.predictor > 1) {
         PDFDictionary dict = new PDFDictionary();
         dict.put("Predictor", this.predictor);
         if (this.colors > 1) {
            dict.put("Colors", this.colors);
         }

         if (this.bitsPerComponent > 0 && this.bitsPerComponent != 8) {
            dict.put("BitsPerComponent", this.bitsPerComponent);
         }

         if (this.columns > 1) {
            dict.put("Columns", this.columns);
         }

         return dict;
      } else {
         return null;
      }
   }

   public void setPredictor(int predictor) throws PDFFilterException {
      this.predictor = predictor;
   }

   public int getPredictor() {
      return this.predictor;
   }

   public void setColors(int colors) throws PDFFilterException {
      if (this.predictor != 1) {
         this.colors = colors;
      } else {
         throw new PDFFilterException("Prediction must not be PREDICTION_NONE in order to set Colors");
      }
   }

   public int getColors() {
      return this.colors;
   }

   public void setBitsPerComponent(int bits) throws PDFFilterException {
      if (this.predictor != 1) {
         this.bitsPerComponent = bits;
      } else {
         throw new PDFFilterException("Prediction must not be PREDICTION_NONE in order to set bitsPerComponent");
      }
   }

   public int getBitsPerComponent() {
      return this.bitsPerComponent;
   }

   public void setColumns(int columns) throws PDFFilterException {
      if (this.predictor != 1) {
         this.columns = columns;
      } else {
         throw new PDFFilterException("Prediction must not be PREDICTION_NONE in order to set Columns");
      }
   }

   public int getColumns() {
      return this.columns;
   }

   public OutputStream applyFilter(OutputStream out) throws IOException {
      return (OutputStream)(this.isApplied() ? out : new FlateEncodeOutputStream(out));
   }
}

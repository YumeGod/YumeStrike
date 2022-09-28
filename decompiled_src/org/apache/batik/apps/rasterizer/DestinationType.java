package org.apache.batik.apps.rasterizer;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;

public final class DestinationType {
   public static final String PNG_STR = "image/png";
   public static final String JPEG_STR = "image/jpeg";
   public static final String TIFF_STR = "image/tiff";
   public static final String PDF_STR = "application/pdf";
   public static final int PNG_CODE = 0;
   public static final int JPEG_CODE = 1;
   public static final int TIFF_CODE = 2;
   public static final int PDF_CODE = 3;
   public static final String PNG_EXTENSION = ".png";
   public static final String JPEG_EXTENSION = ".jpg";
   public static final String TIFF_EXTENSION = ".tif";
   public static final String PDF_EXTENSION = ".pdf";
   public static final DestinationType PNG = new DestinationType("image/png", 0, ".png");
   public static final DestinationType JPEG = new DestinationType("image/jpeg", 1, ".jpg");
   public static final DestinationType TIFF = new DestinationType("image/tiff", 2, ".tif");
   public static final DestinationType PDF = new DestinationType("application/pdf", 3, ".pdf");
   private String type;
   private int code;
   private String extension;

   private DestinationType(String var1, int var2, String var3) {
      this.type = var1;
      this.code = var2;
      this.extension = var3;
   }

   public String getExtension() {
      return this.extension;
   }

   public String toString() {
      return this.type;
   }

   public int toInt() {
      return this.code;
   }

   protected Transcoder getTranscoder() {
      switch (this.code) {
         case 0:
            return new PNGTranscoder();
         case 1:
            return new JPEGTranscoder();
         case 2:
            return new TIFFTranscoder();
         case 3:
            try {
               Class var1 = Class.forName("org.apache.fop.svg.PDFTranscoder");
               return (Transcoder)var1.newInstance();
            } catch (Exception var2) {
               return null;
            }
         default:
            return null;
      }
   }

   public DestinationType[] getValues() {
      return new DestinationType[]{PNG, JPEG, TIFF, PDF};
   }

   public Object readResolve() {
      switch (this.code) {
         case 0:
            return PNG;
         case 1:
            return JPEG;
         case 2:
            return TIFF;
         case 3:
            return PDF;
         default:
            throw new Error("unknown code:" + this.code);
      }
   }
}

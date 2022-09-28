package org.apache.xmlgraphics.image.loader;

public class ImageFlavor {
   public static final ImageFlavor RENDERED_IMAGE = new ImageFlavor("RenderedImage");
   public static final ImageFlavor BUFFERED_IMAGE;
   private static final ImageFlavor DOM;
   public static final ImageFlavor XML_DOM;
   public static final ImageFlavor RAW;
   public static final ImageFlavor RAW_PNG;
   public static final ImageFlavor RAW_JPEG;
   public static final ImageFlavor RAW_TIFF;
   public static final ImageFlavor RAW_EMF;
   public static final ImageFlavor RAW_EPS;
   public static final ImageFlavor RAW_LZW;
   public static final ImageFlavor RAW_CCITTFAX;
   public static final ImageFlavor GRAPHICS2D;
   private String name;

   public ImageFlavor(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public String getMimeType() {
      return null;
   }

   public String getNamespace() {
      return null;
   }

   public boolean isCompatible(ImageFlavor flavor) {
      return this.equals(flavor);
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         ImageFlavor other = (ImageFlavor)obj;
         if (this.name == null) {
            if (other.name != null) {
               return false;
            }
         } else if (!this.name.equals(other.name)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return this.getName();
   }

   static {
      BUFFERED_IMAGE = new SimpleRefinedImageFlavor(RENDERED_IMAGE, "BufferedImage");
      DOM = new ImageFlavor("DOM");
      XML_DOM = new MimeEnabledImageFlavor(DOM, "text/xml");
      RAW = new ImageFlavor("Raw");
      RAW_PNG = new MimeEnabledImageFlavor(RAW, "image/png");
      RAW_JPEG = new MimeEnabledImageFlavor(RAW, "image/jpeg");
      RAW_TIFF = new MimeEnabledImageFlavor(RAW, "image/tiff");
      RAW_EMF = new MimeEnabledImageFlavor(RAW, "image/x-emf");
      RAW_EPS = new MimeEnabledImageFlavor(RAW, "application/postscript");
      RAW_LZW = new ImageFlavor("RawLZW");
      RAW_CCITTFAX = new ImageFlavor("RawCCITTFax");
      GRAPHICS2D = new ImageFlavor("Graphics2DImage");
   }
}

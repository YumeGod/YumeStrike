package org.apache.xmlgraphics.image.loader;

public class MimeEnabledImageFlavor extends RefinedImageFlavor {
   private String mime;

   public MimeEnabledImageFlavor(ImageFlavor parentFlavor, String mime) {
      super(mime + ";" + parentFlavor.getName(), parentFlavor);
      this.mime = mime;
   }

   public String getMimeType() {
      return this.mime;
   }
}

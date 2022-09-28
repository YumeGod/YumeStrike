package org.apache.xmlgraphics.image.loader;

public abstract class RefinedImageFlavor extends ImageFlavor {
   private ImageFlavor parentFlavor;

   protected RefinedImageFlavor(ImageFlavor parentFlavor) {
      this(parentFlavor.getName(), parentFlavor);
   }

   protected RefinedImageFlavor(String name, ImageFlavor parentFlavor) {
      super(name);
      this.parentFlavor = parentFlavor;
   }

   public ImageFlavor getParentFlavor() {
      return this.parentFlavor;
   }

   public String getMimeType() {
      return this.parentFlavor.getMimeType();
   }

   public String getNamespace() {
      return this.parentFlavor.getNamespace();
   }

   public boolean isCompatible(ImageFlavor flavor) {
      return this.getParentFlavor().isCompatible(flavor) || super.isCompatible(flavor);
   }
}

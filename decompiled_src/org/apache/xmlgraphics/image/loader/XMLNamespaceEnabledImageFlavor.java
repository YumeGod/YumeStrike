package org.apache.xmlgraphics.image.loader;

public class XMLNamespaceEnabledImageFlavor extends RefinedImageFlavor {
   public static final ImageFlavor SVG_DOM;
   private String namespace;

   public XMLNamespaceEnabledImageFlavor(ImageFlavor parentFlavor, String namespace) {
      super(parentFlavor.getName() + ";namespace=" + namespace, parentFlavor);
      this.namespace = namespace;
   }

   public String getNamespace() {
      return this.namespace;
   }

   static {
      SVG_DOM = new XMLNamespaceEnabledImageFlavor(ImageFlavor.XML_DOM, "http://www.w3.org/2000/svg");
   }
}

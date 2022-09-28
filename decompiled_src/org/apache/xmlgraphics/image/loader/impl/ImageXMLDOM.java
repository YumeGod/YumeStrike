package org.apache.xmlgraphics.image.loader.impl;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.XMLNamespaceEnabledImageFlavor;
import org.w3c.dom.Document;

public class ImageXMLDOM extends AbstractImage {
   private ImageFlavor flavor;
   private Document doc;
   private String rootNamespace;

   public ImageXMLDOM(ImageInfo info, Document doc, String rootNamespace) {
      super(info);
      this.doc = doc;
      this.rootNamespace = rootNamespace;
      this.flavor = new XMLNamespaceEnabledImageFlavor(ImageFlavor.XML_DOM, rootNamespace);
   }

   public ImageXMLDOM(ImageInfo info, Document doc, XMLNamespaceEnabledImageFlavor flavor) {
      super(info);
      this.doc = doc;
      this.rootNamespace = flavor.getNamespace();
      this.flavor = flavor;
   }

   public ImageFlavor getFlavor() {
      return this.flavor;
   }

   public boolean isCacheable() {
      return true;
   }

   public Document getDocument() {
      return this.doc;
   }

   public String getRootNamespace() {
      return this.rootNamespace;
   }
}

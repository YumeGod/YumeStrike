package org.apache.fop.render.pdf;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Map;
import org.apache.fop.pdf.PDFXObject;
import org.apache.fop.render.RendererContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.w3c.dom.Document;

public class PDFImageHandlerXML implements PDFImageHandler {
   private static final ImageFlavor[] FLAVORS;

   public PDFXObject generateImage(RendererContext context, Image image, Point origin, Rectangle pos) throws IOException {
      PDFRenderer renderer = (PDFRenderer)context.getRenderer();
      ImageXMLDOM imgXML = (ImageXMLDOM)image;
      Document doc = imgXML.getDocument();
      String ns = imgXML.getRootNamespace();
      Map foreignAttributes = (Map)context.getProperty("foreign-attributes");
      renderer.renderDocument(doc, ns, pos, foreignAttributes);
      return null;
   }

   public int getPriority() {
      return 400;
   }

   public Class getSupportedImageClass() {
      return ImageXMLDOM.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return FLAVORS;
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.XML_DOM};
   }
}

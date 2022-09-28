package org.apache.fop.render.afp;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Map;
import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.render.RendererContext;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.w3c.dom.Document;

public class AFPImageHandlerXML extends AFPImageHandler {
   private static final ImageFlavor[] FLAVORS;

   public AFPDataObjectInfo generateDataObjectInfo(AFPRendererImageInfo rendererImageInfo) throws IOException {
      RendererContext rendererContext = rendererImageInfo.getRendererContext();
      AFPRenderer renderer = (AFPRenderer)rendererContext.getRenderer();
      ImageXMLDOM imgXML = (ImageXMLDOM)rendererImageInfo.getImage();
      Document doc = imgXML.getDocument();
      String ns = imgXML.getRootNamespace();
      Map foreignAttributes = (Map)rendererContext.getProperty("foreign-attributes");
      Rectangle2D pos = rendererImageInfo.getPosition();
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

   protected AFPDataObjectInfo createDataObjectInfo() {
      return null;
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.XML_DOM};
   }
}

package org.apache.fop.render.xml;

import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.XMLHandler;
import org.apache.fop.util.DOM2SAX;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;

public class XMLXMLHandler implements XMLHandler {
   public static final String HANDLER = "handler";

   public void handleXML(RendererContext context, Document doc, String ns) throws Exception {
      ContentHandler handler = (ContentHandler)context.getProperty("handler");
      (new DOM2SAX(handler)).writeDocument(doc, true);
   }

   public boolean supportsRenderer(Renderer renderer) {
      return renderer instanceof XMLRenderer;
   }

   public String getNamespace() {
      return null;
   }
}

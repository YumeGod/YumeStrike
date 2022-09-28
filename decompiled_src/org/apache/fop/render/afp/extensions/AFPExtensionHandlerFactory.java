package org.apache.fop.render.afp.extensions;

import org.apache.fop.util.ContentHandlerFactory;
import org.xml.sax.ContentHandler;

public class AFPExtensionHandlerFactory implements ContentHandlerFactory {
   private static final String[] NAMESPACES = new String[]{"apache:fop:extensions:afp"};

   public String[] getSupportedNamespaces() {
      return NAMESPACES;
   }

   public ContentHandler createContentHandler() {
      return new AFPExtensionHandler();
   }
}

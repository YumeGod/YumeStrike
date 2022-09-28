package org.apache.fop.render.ps.extensions;

import org.apache.fop.util.ContentHandlerFactory;
import org.xml.sax.ContentHandler;

public class PSExtensionHandlerFactory implements ContentHandlerFactory {
   private static final String[] NAMESPACES = new String[]{"apache:fop:extensions:postscript"};

   public String[] getSupportedNamespaces() {
      return NAMESPACES;
   }

   public ContentHandler createContentHandler() {
      return new PSExtensionHandler();
   }
}

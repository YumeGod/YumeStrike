package org.apache.fop.fo.extensions.xmp;

import org.apache.fop.util.ContentHandlerFactory;
import org.apache.xmlgraphics.xmp.XMPHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class XMPContentHandlerFactory implements ContentHandlerFactory {
   private static final String[] NAMESPACES = new String[]{"adobe:ns:meta/", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"};

   public String[] getSupportedNamespaces() {
      return NAMESPACES;
   }

   public ContentHandler createContentHandler() throws SAXException {
      return new FOPXMPHandler();
   }

   private class FOPXMPHandler extends XMPHandler implements ContentHandlerFactory.ObjectSource {
      private ContentHandlerFactory.ObjectBuiltListener obListener;

      private FOPXMPHandler() {
      }

      public Object getObject() {
         return this.getMetadata();
      }

      public void setObjectBuiltListener(ContentHandlerFactory.ObjectBuiltListener listener) {
         this.obListener = listener;
      }

      public void endDocument() throws SAXException {
         if (this.obListener != null) {
            this.obListener.notifyObjectBuilt(this.getObject());
         }

      }

      // $FF: synthetic method
      FOPXMPHandler(Object x1) {
         this();
      }
   }
}

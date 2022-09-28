package org.apache.fop.util;

import java.util.EventListener;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public interface ContentHandlerFactory {
   String[] getSupportedNamespaces();

   ContentHandler createContentHandler() throws SAXException;

   public interface ObjectBuiltListener extends EventListener {
      void notifyObjectBuilt(Object var1);
   }

   public interface ObjectSource {
      Object getObject();

      void setObjectBuiltListener(ObjectBuiltListener var1);
   }
}

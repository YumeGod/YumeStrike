package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.XMLDocumentHandler;

public interface XMLDocumentSource {
   void setDocumentHandler(XMLDocumentHandler var1);

   XMLDocumentHandler getDocumentHandler();
}

package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.XMLDTDHandler;

public interface XMLDTDSource {
   void setDTDHandler(XMLDTDHandler var1);

   XMLDTDHandler getDTDHandler();
}

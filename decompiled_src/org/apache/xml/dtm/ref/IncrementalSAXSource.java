package org.apache.xml.dtm.ref;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public interface IncrementalSAXSource {
   void setContentHandler(ContentHandler var1);

   void setLexicalHandler(LexicalHandler var1);

   void setDTDHandler(DTDHandler var1);

   Object deliverMoreNodes(boolean var1);

   void startParse(InputSource var1) throws SAXException;
}

package org.xml.sax;

import java.io.IOException;

public interface XMLReader {
   boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException;

   void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException;

   Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException;

   void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException;

   void setEntityResolver(EntityResolver var1);

   EntityResolver getEntityResolver();

   void setDTDHandler(DTDHandler var1);

   DTDHandler getDTDHandler();

   void setContentHandler(ContentHandler var1);

   ContentHandler getContentHandler();

   void setErrorHandler(ErrorHandler var1);

   ErrorHandler getErrorHandler();

   void parse(InputSource var1) throws IOException, SAXException;

   void parse(String var1) throws IOException, SAXException;
}

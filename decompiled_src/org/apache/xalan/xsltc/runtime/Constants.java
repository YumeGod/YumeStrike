package org.apache.xalan.xsltc.runtime;

public interface Constants {
   int ANY = -1;
   int ATTRIBUTE = -2;
   int ROOT = 0;
   int TEXT = 3;
   int ELEMENT = 1;
   int COMMENT = 8;
   int PROCESSING_INSTRUCTION = 7;
   String XSLT_URI = "http://www.w3.org/1999/XSL/Transform";
   String NAMESPACE_FEATURE = "http://xml.org/sax/features/namespaces";
   String EMPTYSTRING = "";
   String XML_PREFIX = "xml";
   String XMLNS_PREFIX = "xmlns";
   String XMLNS_STRING = "xmlns:";
   String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
}

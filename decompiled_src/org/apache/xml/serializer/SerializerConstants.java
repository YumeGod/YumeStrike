package org.apache.xml.serializer;

interface SerializerConstants {
   String CDATA_CONTINUE = "]]]]><![CDATA[>";
   String CDATA_DELIMITER_CLOSE = "]]>";
   String CDATA_DELIMITER_OPEN = "<![CDATA[";
   String EMPTYSTRING = "";
   String ENTITY_AMP = "&amp;";
   String ENTITY_CRLF = "&#xA;";
   String ENTITY_GT = "&gt;";
   String ENTITY_LT = "&lt;";
   String ENTITY_QUOT = "&quot;";
   String XML_PREFIX = "xml";
   String XMLNS_PREFIX = "xmlns";
   String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
   String DEFAULT_SAX_SERIALIZER = "org.apache.xml.serializer.ToXMLSAXHandler";
   String XMLVERSION11 = "1.1";
   String XMLVERSION10 = "1.0";
}

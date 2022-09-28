package org.apache.xml.serializer;

import java.io.IOException;
import javax.xml.transform.Transformer;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;

public interface SerializationHandler extends ExtendedContentHandler, ExtendedLexicalHandler, XSLOutputAttributes, DeclHandler, DTDHandler, ErrorHandler, DOMSerializer, Serializer {
   void setContentHandler(ContentHandler var1);

   void close();

   void serialize(Node var1) throws IOException;

   boolean setEscaping(boolean var1) throws SAXException;

   void setIndentAmount(int var1);

   void setTransformer(Transformer var1);

   Transformer getTransformer();

   void setNamespaceMappings(NamespaceMappings var1);

   void flushPending() throws SAXException;

   void setDTDEntityExpansion(boolean var1);
}

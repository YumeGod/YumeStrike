package org.apache.xerces.jaxp.validation;

import javax.xml.transform.stream.StreamSource;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

final class Util {
   public static final XMLInputSource toXMLInputSource(StreamSource var0) {
      if (var0.getReader() != null) {
         return new XMLInputSource(var0.getPublicId(), var0.getSystemId(), var0.getSystemId(), var0.getReader(), (String)null);
      } else {
         return var0.getInputStream() != null ? new XMLInputSource(var0.getPublicId(), var0.getSystemId(), var0.getSystemId(), var0.getInputStream(), (String)null) : new XMLInputSource(var0.getPublicId(), var0.getSystemId(), var0.getSystemId());
      }
   }

   public static SAXException toSAXException(XNIException var0) {
      if (var0 instanceof XMLParseException) {
         return toSAXParseException((XMLParseException)var0);
      } else {
         return var0.getException() instanceof SAXException ? (SAXException)var0.getException() : new SAXException(var0.getMessage(), var0.getException());
      }
   }

   public static SAXParseException toSAXParseException(XMLParseException var0) {
      return var0.getException() instanceof SAXParseException ? (SAXParseException)var0.getException() : new SAXParseException(var0.getMessage(), var0.getPublicId(), var0.getExpandedSystemId(), var0.getLineNumber(), var0.getColumnNumber(), var0.getException());
   }
}

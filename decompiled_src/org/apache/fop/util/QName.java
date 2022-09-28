package org.apache.fop.util;

/** @deprecated */
public class QName extends org.apache.xmlgraphics.util.QName {
   private static final long serialVersionUID = -5225376740044770690L;

   public QName(String namespaceURI, String prefix, String localName) {
      super(namespaceURI, prefix, localName);
   }

   public QName(String namespaceURI, String qName) {
      super(namespaceURI, qName);
   }
}

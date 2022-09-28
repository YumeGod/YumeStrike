package org.apache.fop.util;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class DOMBuilderContentHandlerFactory implements ContentHandlerFactory {
   private static SAXTransformerFactory tFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
   private String namespaceURI;
   private DOMImplementation domImplementation;

   public DOMBuilderContentHandlerFactory(String namespaceURI, DOMImplementation domImplementation) {
      this.namespaceURI = namespaceURI;
      this.domImplementation = domImplementation;
   }

   public String[] getSupportedNamespaces() {
      return new String[]{this.namespaceURI};
   }

   public ContentHandler createContentHandler() throws SAXException {
      return new Handler();
   }

   private class Handler extends DelegatingContentHandler implements ContentHandlerFactory.ObjectSource {
      private Document doc;
      private ContentHandlerFactory.ObjectBuiltListener obListener;

      public Handler() throws SAXException {
      }

      public Document getDocument() {
         return this.doc;
      }

      public Object getObject() {
         return this.getDocument();
      }

      public void setObjectBuiltListener(ContentHandlerFactory.ObjectBuiltListener listener) {
         this.obListener = listener;
      }

      public void startDocument() throws SAXException {
         if (this.doc != null) {
            super.startDocument();
         }

      }

      public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
         if (this.doc == null) {
            TransformerHandler handler;
            try {
               handler = DOMBuilderContentHandlerFactory.tFactory.newTransformerHandler();
            } catch (TransformerConfigurationException var7) {
               throw new SAXException("Error creating a new TransformerHandler", var7);
            }

            this.doc = DOMBuilderContentHandlerFactory.this.domImplementation.createDocument(DOMBuilderContentHandlerFactory.this.namespaceURI, qName, (DocumentType)null);
            this.doc.removeChild(this.doc.getDocumentElement());
            handler.setResult(new DOMResult(this.doc));
            this.setDelegateContentHandler(handler);
            this.setDelegateLexicalHandler(handler);
            this.setDelegateDTDHandler(handler);
            handler.startDocument();
         }

         super.startElement(uri, localName, qName, atts);
      }

      public void endDocument() throws SAXException {
         super.endDocument();
         if (this.obListener != null) {
            this.obListener.notifyObjectBuilt(this.getObject());
         }

      }
   }
}

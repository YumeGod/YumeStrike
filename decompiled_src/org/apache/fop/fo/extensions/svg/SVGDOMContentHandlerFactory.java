package org.apache.fop.fo.extensions.svg;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.fop.util.ContentHandlerFactory;
import org.apache.fop.util.DelegatingContentHandler;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class SVGDOMContentHandlerFactory implements ContentHandlerFactory {
   private static SAXTransformerFactory tFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();

   public String[] getSupportedNamespaces() {
      return new String[]{"http://www.w3.org/2000/svg"};
   }

   public ContentHandler createContentHandler() throws SAXException {
      return new Handler();
   }

   private static class Handler extends DelegatingContentHandler implements ContentHandlerFactory.ObjectSource {
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

      private DOMImplementation getDOMImplementation(String ver) {
         if (ver != null && ver.length() != 0 && !ver.equals("1.0") && !ver.equals("1.1")) {
            if (ver.equals("1.2")) {
               try {
                  Class clazz = Class.forName("org.apache.batik.dom.svg12.SVG12DOMImplementation");
                  return (DOMImplementation)clazz.getMethod("getDOMImplementation", (Class[])null).invoke((Object)null, (Object[])null);
               } catch (Exception var3) {
                  return SVGDOMImplementation.getDOMImplementation();
               }
            } else {
               throw new RuntimeException("Unsupport SVG version '" + ver + "'");
            }
         } else {
            return SVGDOMImplementation.getDOMImplementation();
         }
      }

      public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
         if (this.doc == null) {
            TransformerHandler handler;
            try {
               handler = SVGDOMContentHandlerFactory.tFactory.newTransformerHandler();
            } catch (TransformerConfigurationException var8) {
               throw new SAXException("Error creating a new TransformerHandler", var8);
            }

            String version = atts.getValue("version");
            DOMImplementation domImplementation = this.getDOMImplementation(version);
            this.doc = domImplementation.createDocument(uri, qName, (DocumentType)null);
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

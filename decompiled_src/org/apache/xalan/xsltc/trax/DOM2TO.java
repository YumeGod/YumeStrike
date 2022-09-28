package org.apache.xalan.xsltc.trax;

import java.io.IOException;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class DOM2TO implements XMLReader, Locator {
   private static final String EMPTYSTRING = "";
   private static final String XMLNS_PREFIX = "xmlns";
   private Node _dom;
   private SerializationHandler _handler;

   public DOM2TO(Node root, SerializationHandler handler) {
      this._dom = root;
      this._handler = handler;
   }

   public ContentHandler getContentHandler() {
      return null;
   }

   public void setContentHandler(ContentHandler handler) {
   }

   public void parse(InputSource unused) throws IOException, SAXException {
      this.parse(this._dom);
   }

   public void parse() throws IOException, SAXException {
      if (this._dom != null) {
         boolean isIncomplete = this._dom.getNodeType() != 9;
         if (isIncomplete) {
            this._handler.startDocument();
            this.parse(this._dom);
            this._handler.endDocument();
         } else {
            this.parse(this._dom);
         }
      }

   }

   private void parse(Node node) throws IOException, SAXException {
      if (node != null) {
         Node next;
         switch (node.getNodeType()) {
            case 1:
               String qname = node.getNodeName();
               this._handler.startElement((String)null, (String)null, qname);
               NamedNodeMap map = node.getAttributes();
               int length = map.getLength();

               int colon;
               String prefix;
               String uri;
               for(int i = 0; i < length; ++i) {
                  Node attr = map.item(i);
                  String qnameAttr = attr.getNodeName();
                  if (qnameAttr.startsWith("xmlns")) {
                     uri = attr.getNodeValue();
                     colon = qnameAttr.lastIndexOf(58);
                     prefix = colon > 0 ? qnameAttr.substring(colon + 1) : "";
                     this._handler.namespaceAfterStartElement(prefix, uri);
                  }
               }

               NamespaceMappings nm = new NamespaceMappings();

               String qnameAttr;
               for(int i = 0; i < length; ++i) {
                  Node attr = map.item(i);
                  qnameAttr = attr.getNodeName();
                  if (!qnameAttr.startsWith("xmlns")) {
                     String uriAttr = attr.getNamespaceURI();
                     if (uriAttr != null && !uriAttr.equals("")) {
                        colon = qnameAttr.lastIndexOf(58);
                        String newPrefix = nm.lookupPrefix(uriAttr);
                        if (newPrefix == null) {
                           newPrefix = nm.generateNextPrefix();
                        }

                        prefix = colon > 0 ? qnameAttr.substring(0, colon) : newPrefix;
                        this._handler.namespaceAfterStartElement(prefix, uriAttr);
                        this._handler.addAttribute(prefix + ":" + qnameAttr, attr.getNodeValue());
                     } else {
                        this._handler.addAttribute(qnameAttr, attr.getNodeValue());
                     }
                  }
               }

               uri = node.getNamespaceURI();
               qnameAttr = node.getLocalName();
               if (uri != null) {
                  colon = qname.lastIndexOf(58);
                  prefix = colon > 0 ? qname.substring(0, colon) : "";
                  this._handler.namespaceAfterStartElement(prefix, uri);
               } else if (uri == null && qnameAttr != null) {
                  prefix = "";
                  this._handler.namespaceAfterStartElement(prefix, "");
               }

               for(next = node.getFirstChild(); next != null; next = next.getNextSibling()) {
                  this.parse(next);
               }

               this._handler.endElement(qname);
            case 2:
            case 5:
            case 6:
            case 10:
            case 12:
            default:
               break;
            case 3:
               this._handler.characters(node.getNodeValue());
               break;
            case 4:
               this._handler.startCDATA();
               this._handler.characters(node.getNodeValue());
               this._handler.endCDATA();
               break;
            case 7:
               this._handler.processingInstruction(node.getNodeName(), node.getNodeValue());
               break;
            case 8:
               this._handler.comment(node.getNodeValue());
               break;
            case 9:
               this._handler.startDocument();

               for(next = node.getFirstChild(); next != null; next = next.getNextSibling()) {
                  this.parse(next);
               }

               this._handler.endDocument();
               break;
            case 11:
               for(next = node.getFirstChild(); next != null; next = next.getNextSibling()) {
                  this.parse(next);
               }
         }

      }
   }

   public DTDHandler getDTDHandler() {
      return null;
   }

   public ErrorHandler getErrorHandler() {
      return null;
   }

   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
      return false;
   }

   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
   }

   public void parse(String sysId) throws IOException, SAXException {
      throw new IOException("This method is not yet implemented.");
   }

   public void setDTDHandler(DTDHandler handler) throws NullPointerException {
   }

   public void setEntityResolver(EntityResolver resolver) throws NullPointerException {
   }

   public EntityResolver getEntityResolver() {
      return null;
   }

   public void setErrorHandler(ErrorHandler handler) throws NullPointerException {
   }

   public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
   }

   public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
      return null;
   }

   public int getColumnNumber() {
      return 0;
   }

   public int getLineNumber() {
      return 0;
   }

   public String getPublicId() {
      return null;
   }

   public String getSystemId() {
      return null;
   }

   private String getNodeTypeFromCode(short code) {
      String retval = null;
      switch (code) {
         case 1:
            retval = "ELEMENT_NODE";
            break;
         case 2:
            retval = "ATTRIBUTE_NODE";
            break;
         case 3:
            retval = "TEXT_NODE";
            break;
         case 4:
            retval = "CDATA_SECTION_NODE";
            break;
         case 5:
            retval = "ENTITY_REFERENCE_NODE";
            break;
         case 6:
            retval = "ENTITY_NODE";
            break;
         case 7:
            retval = "PROCESSING_INSTRUCTION_NODE";
            break;
         case 8:
            retval = "COMMENT_NODE";
            break;
         case 9:
            retval = "DOCUMENT_NODE";
            break;
         case 10:
            retval = "DOCUMENT_TYPE_NODE";
            break;
         case 11:
            retval = "DOCUMENT_FRAGMENT_NODE";
            break;
         case 12:
            retval = "NOTATION_NODE";
      }

      return retval;
   }
}

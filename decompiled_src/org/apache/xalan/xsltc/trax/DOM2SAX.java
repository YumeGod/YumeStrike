package org.apache.xalan.xsltc.trax;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import org.apache.xalan.xsltc.dom.SAXImpl;
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
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

public class DOM2SAX implements XMLReader, Locator {
   private static final String EMPTYSTRING = "";
   private static final String XMLNS_PREFIX = "xmlns";
   private Node _dom = null;
   private ContentHandler _sax = null;
   private LexicalHandler _lex = null;
   private SAXImpl _saxImpl = null;
   private Hashtable _nsPrefixes = new Hashtable();

   public DOM2SAX(Node root) {
      this._dom = root;
   }

   public ContentHandler getContentHandler() {
      return this._sax;
   }

   public void setContentHandler(ContentHandler handler) throws NullPointerException {
      this._sax = handler;
      if (handler instanceof LexicalHandler) {
         this._lex = (LexicalHandler)handler;
      }

      if (handler instanceof SAXImpl) {
         this._saxImpl = (SAXImpl)handler;
      }

   }

   private boolean startPrefixMapping(String prefix, String uri) throws SAXException {
      boolean pushed = true;
      Stack uriStack = (Stack)this._nsPrefixes.get(prefix);
      if (uriStack != null) {
         if (uriStack.isEmpty()) {
            this._sax.startPrefixMapping(prefix, uri);
            uriStack.push(uri);
         } else {
            String lastUri = (String)uriStack.peek();
            if (!lastUri.equals(uri)) {
               this._sax.startPrefixMapping(prefix, uri);
               uriStack.push(uri);
            } else {
               pushed = false;
            }
         }
      } else {
         this._sax.startPrefixMapping(prefix, uri);
         this._nsPrefixes.put(prefix, uriStack = new Stack());
         uriStack.push(uri);
      }

      return pushed;
   }

   private void endPrefixMapping(String prefix) throws SAXException {
      Stack uriStack = (Stack)this._nsPrefixes.get(prefix);
      if (uriStack != null) {
         this._sax.endPrefixMapping(prefix);
         uriStack.pop();
      }

   }

   private static String getLocalName(Node node) {
      String localName = node.getLocalName();
      if (localName == null) {
         String qname = node.getNodeName();
         int col = qname.lastIndexOf(58);
         return col > 0 ? qname.substring(col + 1) : qname;
      } else {
         return localName;
      }
   }

   public void parse(InputSource unused) throws IOException, SAXException {
      this.parse(this._dom);
   }

   public void parse() throws IOException, SAXException {
      if (this._dom != null) {
         boolean isIncomplete = this._dom.getNodeType() != 9;
         if (isIncomplete) {
            this._sax.startDocument();
            this.parse(this._dom);
            this._sax.endDocument();
         } else {
            this.parse(this._dom);
         }
      }

   }

   private void parse(Node node) throws IOException, SAXException {
      Node first = null;
      if (node != null) {
         Node next;
         switch (node.getNodeType()) {
            case 1:
               Vector pushedPrefixes = new Vector();
               AttributesImpl attrs = new AttributesImpl();
               NamedNodeMap map = node.getAttributes();
               int length = map.getLength();

               String prefix;
               String qname;
               String uri;
               for(int i = 0; i < length; ++i) {
                  Node attr = map.item(i);
                  qname = attr.getNodeName();
                  if (qname.startsWith("xmlns")) {
                     uri = attr.getNodeValue();
                     int colon = qname.lastIndexOf(58);
                     prefix = colon > 0 ? qname.substring(colon + 1) : "";
                     if (this.startPrefixMapping(prefix, uri)) {
                        pushedPrefixes.addElement(prefix);
                     }
                  }
               }

               int i;
               String localName;
               for(int i = 0; i < length; ++i) {
                  Node attr = map.item(i);
                  uri = attr.getNodeName();
                  if (!uri.startsWith("xmlns")) {
                     localName = attr.getNamespaceURI();
                     String localNameAttr = getLocalName(attr);
                     if (localName != null) {
                        i = uri.lastIndexOf(58);
                        prefix = i > 0 ? uri.substring(0, i) : "";
                        if (this.startPrefixMapping(prefix, localName)) {
                           pushedPrefixes.addElement(prefix);
                        }
                     }

                     attrs.addAttribute(attr.getNamespaceURI(), getLocalName(attr), uri, "CDATA", attr.getNodeValue());
                  }
               }

               qname = node.getNodeName();
               uri = node.getNamespaceURI();
               localName = getLocalName(node);
               int nPushedPrefixes;
               if (uri != null) {
                  nPushedPrefixes = qname.lastIndexOf(58);
                  prefix = nPushedPrefixes > 0 ? qname.substring(0, nPushedPrefixes) : "";
                  if (this.startPrefixMapping(prefix, uri)) {
                     pushedPrefixes.addElement(prefix);
                  }
               }

               if (this._saxImpl != null) {
                  this._saxImpl.startElement(uri, localName, qname, attrs, node);
               } else {
                  this._sax.startElement(uri, localName, qname, attrs);
               }

               for(next = node.getFirstChild(); next != null; next = next.getNextSibling()) {
                  this.parse(next);
               }

               this._sax.endElement(uri, localName, qname);
               nPushedPrefixes = pushedPrefixes.size();

               for(i = 0; i < nPushedPrefixes; ++i) {
                  this.endPrefixMapping((String)pushedPrefixes.elementAt(i));
               }
            case 2:
            case 5:
            case 6:
            case 10:
            case 11:
            case 12:
            default:
               break;
            case 3:
               String data = node.getNodeValue();
               this._sax.characters(data.toCharArray(), 0, data.length());
               break;
            case 4:
               String cdata = node.getNodeValue();
               if (this._lex != null) {
                  this._lex.startCDATA();
                  this._sax.characters(cdata.toCharArray(), 0, cdata.length());
                  this._lex.endCDATA();
               } else {
                  this._sax.characters(cdata.toCharArray(), 0, cdata.length());
               }
               break;
            case 7:
               this._sax.processingInstruction(node.getNodeName(), node.getNodeValue());
               break;
            case 8:
               if (this._lex != null) {
                  String value = node.getNodeValue();
                  this._lex.comment(value.toCharArray(), 0, value.length());
               }
               break;
            case 9:
               this._sax.setDocumentLocator(this);
               this._sax.startDocument();

               for(next = node.getFirstChild(); next != null; next = next.getNextSibling()) {
                  this.parse(next);
               }

               this._sax.endDocument();
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

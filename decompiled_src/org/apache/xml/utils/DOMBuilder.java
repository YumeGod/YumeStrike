package org.apache.xml.utils;

import java.io.Writer;
import java.util.Stack;
import org.apache.xml.res.XMLMessages;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class DOMBuilder implements ContentHandler, LexicalHandler {
   public Document m_doc;
   protected Node m_currentNode = null;
   protected Node m_root = null;
   protected Node m_nextSibling = null;
   public DocumentFragment m_docFrag = null;
   protected Stack m_elemStack = new Stack();
   protected boolean m_inCData = false;

   public DOMBuilder(Document doc, Node node) {
      this.m_doc = doc;
      this.m_currentNode = this.m_root = node;
      if (node instanceof Element) {
         this.m_elemStack.push(node);
      }

   }

   public DOMBuilder(Document doc, DocumentFragment docFrag) {
      this.m_doc = doc;
      this.m_docFrag = docFrag;
   }

   public DOMBuilder(Document doc) {
      this.m_doc = doc;
   }

   public Node getRootDocument() {
      return (Node)(null != this.m_docFrag ? this.m_docFrag : this.m_doc);
   }

   public Node getRootNode() {
      return this.m_root;
   }

   public Node getCurrentNode() {
      return this.m_currentNode;
   }

   public void setNextSibling(Node nextSibling) {
      this.m_nextSibling = nextSibling;
   }

   public Node getNextSibling() {
      return this.m_nextSibling;
   }

   public Writer getWriter() {
      return null;
   }

   protected void append(Node newNode) throws SAXException {
      Node currentNode = this.m_currentNode;
      if (null != currentNode) {
         if (currentNode == this.m_root && this.m_nextSibling != null) {
            currentNode.insertBefore(newNode, this.m_nextSibling);
         } else {
            currentNode.appendChild(newNode);
         }
      } else if (null != this.m_docFrag) {
         if (this.m_nextSibling != null) {
            this.m_docFrag.insertBefore(newNode, this.m_nextSibling);
         } else {
            this.m_docFrag.appendChild(newNode);
         }
      } else {
         boolean ok = true;
         short type = newNode.getNodeType();
         if (type == 3) {
            String data = newNode.getNodeValue();
            if (null != data && data.trim().length() > 0) {
               throw new SAXException(XMLMessages.createXMLMessage("ER_CANT_OUTPUT_TEXT_BEFORE_DOC", (Object[])null));
            }

            ok = false;
         } else if (type == 1 && this.m_doc.getDocumentElement() != null) {
            ok = false;
            throw new SAXException(XMLMessages.createXMLMessage("ER_CANT_HAVE_MORE_THAN_ONE_ROOT", (Object[])null));
         }

         if (ok) {
            if (this.m_nextSibling != null) {
               this.m_doc.insertBefore(newNode, this.m_nextSibling);
            } else {
               this.m_doc.appendChild(newNode);
            }
         }
      }

   }

   public void setDocumentLocator(Locator locator) {
   }

   public void startDocument() throws SAXException {
   }

   public void endDocument() throws SAXException {
   }

   public void startElement(String ns, String localName, String name, Attributes atts) throws SAXException {
      Element elem;
      if (null != ns && ns.length() != 0) {
         elem = this.m_doc.createElementNS(ns, name);
      } else {
         elem = this.m_doc.createElementNS((String)null, name);
      }

      this.append(elem);

      try {
         int nAtts = atts.getLength();
         if (0 != nAtts) {
            for(int i = 0; i < nAtts; ++i) {
               if (atts.getType(i).equalsIgnoreCase("ID")) {
                  this.setIDAttribute(atts.getValue(i), elem);
               }

               String attrNS = atts.getURI(i);
               if ("".equals(attrNS)) {
                  attrNS = null;
               }

               String attrQName = atts.getQName(i);
               if (attrQName.startsWith("xmlns:") || attrQName.equals("xmlns")) {
                  attrNS = "http://www.w3.org/2000/xmlns/";
               }

               elem.setAttributeNS(attrNS, attrQName, atts.getValue(i));
            }
         }

         this.m_elemStack.push(elem);
         this.m_currentNode = elem;
      } catch (Exception var10) {
         throw new SAXException(var10);
      }
   }

   public void endElement(String ns, String localName, String name) throws SAXException {
      this.m_elemStack.pop();
      this.m_currentNode = this.m_elemStack.isEmpty() ? null : (Node)this.m_elemStack.peek();
   }

   public void setIDAttribute(String id, Element elem) {
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      if (!this.isOutsideDocElem() || !XMLCharacterRecognizer.isWhiteSpace(ch, start, length)) {
         if (this.m_inCData) {
            this.cdata(ch, start, length);
         } else {
            String s = new String(ch, start, length);
            Node childNode = this.m_currentNode != null ? this.m_currentNode.getLastChild() : null;
            if (childNode != null && childNode.getNodeType() == 3) {
               ((Text)childNode).appendData(s);
            } else {
               Text text = this.m_doc.createTextNode(s);
               this.append(text);
            }

         }
      }
   }

   public void charactersRaw(char[] ch, int start, int length) throws SAXException {
      if (!this.isOutsideDocElem() || !XMLCharacterRecognizer.isWhiteSpace(ch, start, length)) {
         String s = new String(ch, start, length);
         this.append(this.m_doc.createProcessingInstruction("xslt-next-is-raw", "formatter-to-dom"));
         this.append(this.m_doc.createTextNode(s));
      }
   }

   public void startEntity(String name) throws SAXException {
   }

   public void endEntity(String name) throws SAXException {
   }

   public void entityReference(String name) throws SAXException {
      this.append(this.m_doc.createEntityReference(name));
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      if (!this.isOutsideDocElem()) {
         String s = new String(ch, start, length);
         this.append(this.m_doc.createTextNode(s));
      }
   }

   private boolean isOutsideDocElem() {
      return null == this.m_docFrag && this.m_elemStack.size() == 0 && (null == this.m_currentNode || this.m_currentNode.getNodeType() == 9);
   }

   public void processingInstruction(String target, String data) throws SAXException {
      this.append(this.m_doc.createProcessingInstruction(target, data));
   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      this.append(this.m_doc.createComment(new String(ch, start, length)));
   }

   public void startCDATA() throws SAXException {
      this.m_inCData = true;
      this.append(this.m_doc.createCDATASection(""));
   }

   public void endCDATA() throws SAXException {
      this.m_inCData = false;
   }

   public void cdata(char[] ch, int start, int length) throws SAXException {
      if (!this.isOutsideDocElem() || !XMLCharacterRecognizer.isWhiteSpace(ch, start, length)) {
         String s = new String(ch, start, length);
         CDATASection section = (CDATASection)this.m_currentNode.getLastChild();
         section.appendData(s);
      }
   }

   public void startDTD(String name, String publicId, String systemId) throws SAXException {
   }

   public void endDTD() throws SAXException {
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
   }

   public void endPrefixMapping(String prefix) throws SAXException {
   }

   public void skippedEntity(String name) throws SAXException {
   }
}

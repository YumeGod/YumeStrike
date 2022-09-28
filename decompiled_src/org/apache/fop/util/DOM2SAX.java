package org.apache.fop.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

public class DOM2SAX {
   private static final String EMPTYSTRING = "";
   private static final String XMLNS_PREFIX = "xmlns";
   private ContentHandler contentHandler;
   private LexicalHandler lexicalHandler;
   private Map prefixes = new HashMap();

   public DOM2SAX(ContentHandler handler) {
      this.contentHandler = handler;
      if (handler instanceof LexicalHandler) {
         this.lexicalHandler = (LexicalHandler)handler;
      }

   }

   public void writeDocument(Document doc, boolean fragment) throws SAXException {
      if (!fragment) {
         this.contentHandler.startDocument();
      }

      for(Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
         this.writeNode(n);
      }

      if (!fragment) {
         this.contentHandler.endDocument();
      }

   }

   public void writeFragment(Node node) throws SAXException {
      this.writeNode(node);
   }

   private boolean startPrefixMapping(String prefix, String uri) throws SAXException {
      boolean pushed = true;
      Stack uriStack = (Stack)this.prefixes.get(prefix);
      if (uriStack != null) {
         if (uriStack.isEmpty()) {
            this.contentHandler.startPrefixMapping(prefix, uri);
            uriStack.push(uri);
         } else {
            String lastUri = (String)uriStack.peek();
            if (!lastUri.equals(uri)) {
               this.contentHandler.startPrefixMapping(prefix, uri);
               uriStack.push(uri);
            } else {
               pushed = false;
            }
         }
      } else {
         this.contentHandler.startPrefixMapping(prefix, uri);
         uriStack = new Stack();
         this.prefixes.put(prefix, uriStack);
         uriStack.push(uri);
      }

      return pushed;
   }

   private void endPrefixMapping(String prefix) throws SAXException {
      Stack uriStack = (Stack)this.prefixes.get(prefix);
      if (uriStack != null) {
         this.contentHandler.endPrefixMapping(prefix);
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

   private void writeNode(Node node) throws SAXException {
      if (node != null) {
         Node next;
         switch (node.getNodeType()) {
            case 1:
               List pushedPrefixes = new ArrayList();
               AttributesImpl attrs = new AttributesImpl();
               NamedNodeMap map = node.getAttributes();
               int length = map.getLength();

               String prefix;
               int i;
               Node attr;
               String localName;
               String uriAttr;
               int i;
               for(i = 0; i < length; ++i) {
                  attr = map.item(i);
                  localName = attr.getNodeName();
                  if (localName.startsWith("xmlns")) {
                     uriAttr = attr.getNodeValue();
                     i = localName.lastIndexOf(58);
                     prefix = i > 0 ? localName.substring(i + 1) : "";
                     if (this.startPrefixMapping(prefix, uriAttr)) {
                        pushedPrefixes.add(prefix);
                     }
                  }
               }

               for(i = 0; i < length; ++i) {
                  attr = map.item(i);
                  localName = attr.getNodeName();
                  if (!localName.startsWith("xmlns")) {
                     uriAttr = attr.getNamespaceURI();
                     if (uriAttr != null) {
                        i = localName.lastIndexOf(58);
                        prefix = i > 0 ? localName.substring(0, i) : "";
                        if (this.startPrefixMapping(prefix, uriAttr)) {
                           pushedPrefixes.add(prefix);
                        }
                     }

                     attrs.addAttribute(attr.getNamespaceURI(), getLocalName(attr), localName, "CDATA", attr.getNodeValue());
                  }
               }

               String qname = node.getNodeName();
               String uri = node.getNamespaceURI();
               localName = getLocalName(node);
               int nPushedPrefixes;
               if (uri != null) {
                  nPushedPrefixes = qname.lastIndexOf(58);
                  prefix = nPushedPrefixes > 0 ? qname.substring(0, nPushedPrefixes) : "";
                  if (this.startPrefixMapping(prefix, uri)) {
                     pushedPrefixes.add(prefix);
                  }
               }

               this.contentHandler.startElement(uri, localName, qname, attrs);

               for(next = node.getFirstChild(); next != null; next = next.getNextSibling()) {
                  this.writeNode(next);
               }

               this.contentHandler.endElement(uri, localName, qname);
               nPushedPrefixes = pushedPrefixes.size();

               for(i = 0; i < nPushedPrefixes; ++i) {
                  this.endPrefixMapping((String)pushedPrefixes.get(i));
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
               this.contentHandler.characters(data.toCharArray(), 0, data.length());
               break;
            case 4:
               String cdata = node.getNodeValue();
               if (this.lexicalHandler != null) {
                  this.lexicalHandler.startCDATA();
                  this.contentHandler.characters(cdata.toCharArray(), 0, cdata.length());
                  this.lexicalHandler.endCDATA();
               } else {
                  this.contentHandler.characters(cdata.toCharArray(), 0, cdata.length());
               }
               break;
            case 7:
               this.contentHandler.processingInstruction(node.getNodeName(), node.getNodeValue());
               break;
            case 8:
               if (this.lexicalHandler != null) {
                  String value = node.getNodeValue();
                  this.lexicalHandler.comment(value.toCharArray(), 0, value.length());
               }
               break;
            case 9:
               this.contentHandler.startDocument();

               for(next = node.getFirstChild(); next != null; next = next.getNextSibling()) {
                  this.writeNode(next);
               }

               this.contentHandler.endDocument();
         }

      }
   }
}

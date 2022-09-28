package org.apache.xml.utils;

import java.io.File;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTM;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.LocatorImpl;

public class TreeWalker {
   private ContentHandler m_contentHandler = null;
   protected DOMHelper m_dh;
   private LocatorImpl m_locator = new LocatorImpl();
   boolean nextIsRaw = false;

   public ContentHandler getContentHandler() {
      return this.m_contentHandler;
   }

   public void setContentHandler(ContentHandler ch) {
      this.m_contentHandler = ch;
   }

   public TreeWalker(ContentHandler contentHandler, DOMHelper dh, String systemId) {
      this.m_contentHandler = contentHandler;
      this.m_contentHandler.setDocumentLocator(this.m_locator);
      if (systemId != null) {
         this.m_locator.setSystemId(systemId);
      } else {
         try {
            this.m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
         } catch (SecurityException var5) {
         }
      }

      this.m_dh = dh;
   }

   public TreeWalker(ContentHandler contentHandler, DOMHelper dh) {
      this.m_contentHandler = contentHandler;
      this.m_contentHandler.setDocumentLocator(this.m_locator);

      try {
         this.m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
      } catch (SecurityException var4) {
      }

      this.m_dh = dh;
   }

   public TreeWalker(ContentHandler contentHandler) {
      this.m_contentHandler = contentHandler;
      if (this.m_contentHandler != null) {
         this.m_contentHandler.setDocumentLocator(this.m_locator);
      }

      try {
         this.m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
      } catch (SecurityException var3) {
      }

      this.m_dh = new DOM2Helper();
   }

   public void traverse(Node pos) throws SAXException {
      this.m_contentHandler.startDocument();

      Node nextNode;
      label39:
      for(Node top = pos; null != pos; pos = nextNode) {
         this.startNode(pos);
         nextNode = pos.getFirstChild();

         do {
            do {
               if (null != nextNode) {
                  continue label39;
               }

               this.endNode(pos);
               if (top.equals(pos)) {
                  continue label39;
               }

               nextNode = pos.getNextSibling();
            } while(null != nextNode);

            pos = pos.getParentNode();
         } while(null != pos && !top.equals(pos));

         if (null != pos) {
            this.endNode(pos);
         }

         nextNode = null;
      }

      this.m_contentHandler.endDocument();
   }

   public void traverse(Node pos, Node top) throws SAXException {
      this.m_contentHandler.startDocument();

      Node nextNode;
      label40:
      for(; null != pos; pos = nextNode) {
         this.startNode(pos);
         nextNode = pos.getFirstChild();

         do {
            do {
               if (null != nextNode) {
                  continue label40;
               }

               this.endNode(pos);
               if (null != top && top.equals(pos)) {
                  continue label40;
               }

               nextNode = pos.getNextSibling();
            } while(null != nextNode);

            pos = pos.getParentNode();
         } while(null != pos && (null == top || !top.equals(pos)));

         nextNode = null;
      }

      this.m_contentHandler.endDocument();
   }

   private final void dispatachChars(Node node) throws SAXException {
      if (this.m_contentHandler instanceof DOM2DTM.CharacterNodeHandler) {
         ((DOM2DTM.CharacterNodeHandler)this.m_contentHandler).characters(node);
      } else {
         String data = ((Text)node).getData();
         this.m_contentHandler.characters(data.toCharArray(), 0, data.length());
      }

   }

   protected void startNode(Node node) throws SAXException {
      if (this.m_contentHandler instanceof NodeConsumer) {
         ((NodeConsumer)this.m_contentHandler).setOriginatingNode(node);
      }

      if (node instanceof Locator) {
         Locator loc = (Locator)node;
         this.m_locator.setColumnNumber(loc.getColumnNumber());
         this.m_locator.setLineNumber(loc.getLineNumber());
         this.m_locator.setPublicId(loc.getPublicId());
         this.m_locator.setSystemId(loc.getSystemId());
      } else {
         this.m_locator.setColumnNumber(0);
         this.m_locator.setLineNumber(0);
      }

      switch (node.getNodeType()) {
         case 1:
            NamedNodeMap atts = ((Element)node).getAttributes();
            int nAttrs = atts.getLength();

            for(int i = 0; i < nAttrs; ++i) {
               Node attr = atts.item(i);
               String attrName = attr.getNodeName();
               if (attrName.equals("xmlns") || attrName.startsWith("xmlns:")) {
                  int index;
                  String prefix = (index = attrName.indexOf(":")) < 0 ? "" : attrName.substring(index + 1);
                  this.m_contentHandler.startPrefixMapping(prefix, attr.getNodeValue());
               }
            }

            String ns = this.m_dh.getNamespaceOfNode(node);
            if (null == ns) {
               ns = "";
            }

            this.m_contentHandler.startElement(ns, this.m_dh.getLocalNameOfNode(node), node.getNodeName(), new AttList(atts, this.m_dh));
         case 2:
         case 6:
         case 9:
         case 10:
         case 11:
         default:
            break;
         case 3:
            if (this.nextIsRaw) {
               this.nextIsRaw = false;
               this.m_contentHandler.processingInstruction("javax.xml.transform.disable-output-escaping", "");
               this.dispatachChars(node);
               this.m_contentHandler.processingInstruction("javax.xml.transform.enable-output-escaping", "");
            } else {
               this.dispatachChars(node);
            }
            break;
         case 4:
            boolean isLexH = this.m_contentHandler instanceof LexicalHandler;
            LexicalHandler lh = isLexH ? (LexicalHandler)this.m_contentHandler : null;
            if (isLexH) {
               lh.startCDATA();
            }

            this.dispatachChars(node);
            if (isLexH) {
               lh.endCDATA();
            }
            break;
         case 5:
            EntityReference eref = (EntityReference)node;
            if (this.m_contentHandler instanceof LexicalHandler) {
               ((LexicalHandler)this.m_contentHandler).startEntity(eref.getNodeName());
            }
            break;
         case 7:
            ProcessingInstruction pi = (ProcessingInstruction)node;
            String name = pi.getNodeName();
            if (name.equals("xslt-next-is-raw")) {
               this.nextIsRaw = true;
            } else {
               this.m_contentHandler.processingInstruction(pi.getNodeName(), pi.getData());
            }
            break;
         case 8:
            String data = ((Comment)node).getData();
            if (this.m_contentHandler instanceof LexicalHandler) {
               LexicalHandler lh = (LexicalHandler)this.m_contentHandler;
               lh.comment(data.toCharArray(), 0, data.length());
            }
      }

   }

   protected void endNode(Node node) throws SAXException {
      switch (node.getNodeType()) {
         case 1:
            String ns = this.m_dh.getNamespaceOfNode(node);
            if (null == ns) {
               ns = "";
            }

            this.m_contentHandler.endElement(ns, this.m_dh.getLocalNameOfNode(node), node.getNodeName());
            NamedNodeMap atts = ((Element)node).getAttributes();
            int nAttrs = atts.getLength();

            for(int i = 0; i < nAttrs; ++i) {
               Node attr = atts.item(i);
               String attrName = attr.getNodeName();
               if (attrName.equals("xmlns") || attrName.startsWith("xmlns:")) {
                  int index;
                  String prefix = (index = attrName.indexOf(":")) < 0 ? "" : attrName.substring(index + 1);
                  this.m_contentHandler.endPrefixMapping(prefix);
               }
            }
         case 2:
         case 3:
         case 4:
         case 6:
         case 7:
         case 8:
         case 9:
         default:
            break;
         case 5:
            EntityReference eref = (EntityReference)node;
            if (this.m_contentHandler instanceof LexicalHandler) {
               LexicalHandler lh = (LexicalHandler)this.m_contentHandler;
               lh.endEntity(eref.getNodeName());
            }
      }

   }
}

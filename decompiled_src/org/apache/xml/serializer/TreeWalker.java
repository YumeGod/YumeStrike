package org.apache.xml.serializer;

import java.io.File;
import org.apache.xml.serializer.utils.AttList;
import org.apache.xml.serializer.utils.DOM2Helper;
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

public final class TreeWalker {
   private final ContentHandler m_contentHandler;
   private final SerializationHandler m_Serializer;
   protected final DOM2Helper m_dh;
   private final LocatorImpl m_locator;
   boolean nextIsRaw;

   public ContentHandler getContentHandler() {
      return this.m_contentHandler;
   }

   public TreeWalker(ContentHandler ch) {
      this(ch, (String)null);
   }

   public TreeWalker(ContentHandler contentHandler, String systemId) {
      this.m_locator = new LocatorImpl();
      this.nextIsRaw = false;
      this.m_contentHandler = contentHandler;
      if (this.m_contentHandler instanceof SerializationHandler) {
         this.m_Serializer = (SerializationHandler)this.m_contentHandler;
      } else {
         this.m_Serializer = null;
      }

      this.m_contentHandler.setDocumentLocator(this.m_locator);
      if (systemId != null) {
         this.m_locator.setSystemId(systemId);
      } else {
         try {
            this.m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
         } catch (SecurityException var5) {
         }
      }

      if (this.m_contentHandler != null) {
         this.m_contentHandler.setDocumentLocator(this.m_locator);
      }

      try {
         this.m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
      } catch (SecurityException var4) {
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
      if (this.m_Serializer != null) {
         this.m_Serializer.characters(node);
      } else {
         String data = ((Text)node).getData();
         this.m_contentHandler.characters(data.toCharArray(), 0, data.length());
      }

   }

   protected void startNode(Node node) throws SAXException {
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
            Element elem_node = (Element)node;
            String uri = elem_node.getNamespaceURI();
            if (uri != null) {
               String prefix = elem_node.getPrefix();
               if (prefix == null) {
                  prefix = "";
               }

               this.m_contentHandler.startPrefixMapping(prefix, uri);
            }

            NamedNodeMap atts = elem_node.getAttributes();
            int nAttrs = atts.getLength();

            for(int i = 0; i < nAttrs; ++i) {
               Node attr = atts.item(i);
               String attrName = attr.getNodeName();
               int colon = attrName.indexOf(58);
               String prefix;
               if (!attrName.equals("xmlns") && !attrName.startsWith("xmlns:")) {
                  if (colon > 0) {
                     prefix = attrName.substring(0, colon);
                     String uri = attr.getNamespaceURI();
                     if (uri != null) {
                        this.m_contentHandler.startPrefixMapping(prefix, uri);
                     }
                  }
               } else {
                  if (colon < 0) {
                     prefix = "";
                  } else {
                     prefix = attrName.substring(colon + 1);
                  }

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
            if (this.m_Serializer == null) {
               Element elem_node = (Element)node;
               NamedNodeMap atts = elem_node.getAttributes();
               int nAttrs = atts.getLength();

               String prefix;
               for(int i = nAttrs - 1; 0 <= i; --i) {
                  Node attr = atts.item(i);
                  prefix = attr.getNodeName();
                  int colon = prefix.indexOf(58);
                  String prefix;
                  if (!prefix.equals("xmlns") && !prefix.startsWith("xmlns:")) {
                     if (colon > 0) {
                        prefix = prefix.substring(0, colon);
                        this.m_contentHandler.endPrefixMapping(prefix);
                     }
                  } else {
                     if (colon < 0) {
                        prefix = "";
                     } else {
                        prefix = prefix.substring(colon + 1);
                     }

                     this.m_contentHandler.endPrefixMapping(prefix);
                  }
               }

               String uri = elem_node.getNamespaceURI();
               if (uri != null) {
                  prefix = elem_node.getPrefix();
                  if (prefix == null) {
                     prefix = "";
                  }

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

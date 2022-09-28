package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.NodeConsumer;
import org.apache.xml.utils.XMLString;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

public class DTMTreeWalker {
   private ContentHandler m_contentHandler = null;
   protected DTM m_dtm;
   boolean nextIsRaw = false;

   public void setDTM(DTM dtm) {
      this.m_dtm = dtm;
   }

   public ContentHandler getcontentHandler() {
      return this.m_contentHandler;
   }

   public void setcontentHandler(ContentHandler ch) {
      this.m_contentHandler = ch;
   }

   public DTMTreeWalker() {
   }

   public DTMTreeWalker(ContentHandler contentHandler, DTM dtm) {
      this.m_contentHandler = contentHandler;
      this.m_dtm = dtm;
   }

   public void traverse(int pos) throws SAXException {
      int nextNode;
      label39:
      for(int top = pos; -1 != pos; pos = nextNode) {
         this.startNode(pos);
         nextNode = this.m_dtm.getFirstChild(pos);

         do {
            do {
               if (-1 != nextNode) {
                  continue label39;
               }

               this.endNode(pos);
               if (top == pos) {
                  continue label39;
               }

               nextNode = this.m_dtm.getNextSibling(pos);
            } while(-1 != nextNode);

            pos = this.m_dtm.getParent(pos);
         } while(-1 != pos && top != pos);

         if (-1 != pos) {
            this.endNode(pos);
         }

         nextNode = -1;
      }

   }

   public void traverse(int pos, int top) throws SAXException {
      int nextNode;
      for(; -1 != pos; pos = nextNode) {
         this.startNode(pos);
         nextNode = this.m_dtm.getFirstChild(pos);

         while(-1 == nextNode) {
            this.endNode(pos);
            if (-1 != top && top == pos) {
               break;
            }

            nextNode = this.m_dtm.getNextSibling(pos);
            if (-1 == nextNode) {
               pos = this.m_dtm.getParent(pos);
               if (-1 == pos || -1 != top && top == pos) {
                  nextNode = -1;
                  break;
               }
            }
         }
      }

   }

   private final void dispatachChars(int node) throws SAXException {
      this.m_dtm.dispatchCharactersEvents(node, this.m_contentHandler, false);
   }

   protected void startNode(int node) throws SAXException {
      if (this.m_contentHandler instanceof NodeConsumer) {
      }

      switch (this.m_dtm.getNodeType(node)) {
         case 1:
            DTM dtm = this.m_dtm;

            String ns;
            for(int nsn = dtm.getFirstNamespaceNode(node, true); -1 != nsn; nsn = dtm.getNextNamespaceNode(node, nsn, true)) {
               ns = dtm.getNodeNameX(nsn);
               this.m_contentHandler.startPrefixMapping(ns, dtm.getNodeValue(nsn));
            }

            ns = dtm.getNamespaceURI(node);
            if (null == ns) {
               ns = "";
            }

            AttributesImpl attrs = new AttributesImpl();

            for(int i = dtm.getFirstAttribute(node); i != -1; i = dtm.getNextAttribute(i)) {
               attrs.addAttribute(dtm.getNamespaceURI(i), dtm.getLocalName(i), dtm.getNodeName(i), "CDATA", dtm.getNodeValue(i));
            }

            this.m_contentHandler.startElement(ns, this.m_dtm.getLocalName(node), this.m_dtm.getNodeName(node), attrs);
         case 2:
         case 6:
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
            if (this.m_contentHandler instanceof LexicalHandler) {
               ((LexicalHandler)this.m_contentHandler).startEntity(this.m_dtm.getNodeName(node));
            }
            break;
         case 7:
            String name = this.m_dtm.getNodeName(node);
            if (name.equals("xslt-next-is-raw")) {
               this.nextIsRaw = true;
            } else {
               this.m_contentHandler.processingInstruction(name, this.m_dtm.getNodeValue(node));
            }
            break;
         case 8:
            XMLString data = this.m_dtm.getStringValue(node);
            if (this.m_contentHandler instanceof LexicalHandler) {
               LexicalHandler lh = (LexicalHandler)this.m_contentHandler;
               data.dispatchAsComment(lh);
            }
            break;
         case 9:
            this.m_contentHandler.startDocument();
      }

   }

   protected void endNode(int node) throws SAXException {
      switch (this.m_dtm.getNodeType(node)) {
         case 1:
            String ns = this.m_dtm.getNamespaceURI(node);
            if (null == ns) {
               ns = "";
            }

            this.m_contentHandler.endElement(ns, this.m_dtm.getLocalName(node), this.m_dtm.getNodeName(node));

            for(int nsn = this.m_dtm.getFirstNamespaceNode(node, true); -1 != nsn; nsn = this.m_dtm.getNextNamespaceNode(node, nsn, true)) {
               String prefix = this.m_dtm.getNodeNameX(nsn);
               this.m_contentHandler.endPrefixMapping(prefix);
            }
         case 2:
         case 3:
         case 4:
         case 6:
         case 7:
         case 8:
         default:
            break;
         case 5:
            if (this.m_contentHandler instanceof LexicalHandler) {
               LexicalHandler lh = (LexicalHandler)this.m_contentHandler;
               lh.endEntity(this.m_dtm.getNodeName(node));
            }
            break;
         case 9:
            this.m_contentHandler.endDocument();
      }

   }
}

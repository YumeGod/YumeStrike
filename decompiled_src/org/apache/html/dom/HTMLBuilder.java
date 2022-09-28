package org.apache.html.dom;

import java.util.Vector;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ProcessingInstructionImpl;
import org.apache.xerces.dom.TextImpl;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class HTMLBuilder implements DocumentHandler {
   protected HTMLDocumentImpl _document;
   protected ElementImpl _current;
   private Locator _locator;
   private boolean _ignoreWhitespace = true;
   private boolean _done = true;
   protected Vector _preRootNodes;

   public void startDocument() throws SAXException {
      if (!this._done) {
         throw new SAXException("HTM001 State error: startDocument fired twice on one builder.");
      } else {
         this._document = null;
         this._done = false;
      }
   }

   public void endDocument() throws SAXException {
      if (this._document == null) {
         throw new SAXException("HTM002 State error: document never started or missing document element.");
      } else if (this._current != null) {
         throw new SAXException("HTM003 State error: document ended before end of document element.");
      } else {
         this._current = null;
         this._done = true;
      }
   }

   public synchronized void startElement(String var1, AttributeList var2) throws SAXException {
      if (var1 == null) {
         throw new SAXException("HTM004 Argument 'tagName' is null.");
      } else {
         ElementImpl var3;
         int var4;
         if (this._document == null) {
            this._document = new HTMLDocumentImpl();
            var3 = (ElementImpl)this._document.getDocumentElement();
            this._current = var3;
            if (this._current == null) {
               throw new SAXException("HTM005 State error: Document.getDocumentElement returns null.");
            }

            if (this._preRootNodes != null) {
               var4 = this._preRootNodes.size();

               while(var4-- > 0) {
                  this._document.insertBefore((Node)this._preRootNodes.elementAt(var4), var3);
               }

               this._preRootNodes = null;
            }
         } else {
            if (this._current == null) {
               throw new SAXException("HTM006 State error: startElement called after end of document element.");
            }

            var3 = (ElementImpl)this._document.createElement(var1);
            this._current.appendChild(var3);
            this._current = var3;
         }

         if (var2 != null) {
            for(var4 = 0; var4 < var2.getLength(); ++var4) {
               var3.setAttribute(var2.getName(var4), var2.getValue(var4));
            }
         }

      }
   }

   public void endElement(String var1) throws SAXException {
      if (this._current == null) {
         throw new SAXException("HTM007 State error: endElement called with no current node.");
      } else if (!this._current.getNodeName().equalsIgnoreCase(var1)) {
         throw new SAXException("HTM008 State error: mismatch in closing tag name " + var1 + "\n" + var1);
      } else {
         if (this._current.getParentNode() == this._current.getOwnerDocument()) {
            this._current = null;
         } else {
            this._current = (ElementImpl)this._current.getParentNode();
         }

      }
   }

   public void characters(String var1) throws SAXException {
      if (this._current == null) {
         throw new SAXException("HTM009 State error: character data found outside of root element.");
      } else {
         this._current.appendChild(new TextImpl(this._document, var1));
      }
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (this._current == null) {
         throw new SAXException("HTM010 State error: character data found outside of root element.");
      } else {
         this._current.appendChild(new TextImpl(this._document, new String(var1, var2, var3)));
      }
   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      if (!this._ignoreWhitespace) {
         this._current.appendChild(new TextImpl(this._document, new String(var1, var2, var3)));
      }

   }

   public void processingInstruction(String var1, String var2) throws SAXException {
      if (this._current == null && this._document == null) {
         if (this._preRootNodes == null) {
            this._preRootNodes = new Vector();
         }

         this._preRootNodes.addElement(new ProcessingInstructionImpl((CoreDocumentImpl)null, var1, var2));
      } else if (this._current == null && this._document != null) {
         this._document.appendChild(new ProcessingInstructionImpl(this._document, var1, var2));
      } else {
         this._current.appendChild(new ProcessingInstructionImpl(this._document, var1, var2));
      }

   }

   public HTMLDocument getHTMLDocument() {
      return this._document;
   }

   public void setDocumentLocator(Locator var1) {
      this._locator = var1;
   }
}

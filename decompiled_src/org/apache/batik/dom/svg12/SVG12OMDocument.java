package org.apache.batik.dom.svg12;

import org.apache.batik.css.engine.CSSNavigableDocumentListener;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventListener;

public class SVG12OMDocument extends SVGOMDocument {
   protected SVG12OMDocument() {
   }

   public SVG12OMDocument(DocumentType var1, DOMImplementation var2) {
      super(var1, var2);
   }

   protected Node newNode() {
      return new SVG12OMDocument();
   }

   public void addCSSNavigableDocumentListener(CSSNavigableDocumentListener var1) {
      if (!this.cssNavigableDocumentListeners.containsKey(var1)) {
         SVGOMDocument.DOMNodeInsertedListenerWrapper var2 = new SVGOMDocument.DOMNodeInsertedListenerWrapper(var1);
         SVGOMDocument.DOMNodeRemovedListenerWrapper var3 = new SVGOMDocument.DOMNodeRemovedListenerWrapper(var1);
         SVGOMDocument.DOMSubtreeModifiedListenerWrapper var4 = new SVGOMDocument.DOMSubtreeModifiedListenerWrapper(var1);
         SVGOMDocument.DOMCharacterDataModifiedListenerWrapper var5 = new SVGOMDocument.DOMCharacterDataModifiedListenerWrapper(var1);
         SVGOMDocument.DOMAttrModifiedListenerWrapper var6 = new SVGOMDocument.DOMAttrModifiedListenerWrapper(var1);
         this.cssNavigableDocumentListeners.put(var1, new EventListener[]{var2, var3, var4, var5, var6});
         XBLEventSupport var7 = (XBLEventSupport)this.initializeEventSupport();
         var7.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", var2, false);
         var7.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", var3, false);
         var7.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", var4, false);
         var7.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", var5, false);
         var7.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", var6, false);
      }
   }

   public void removeCSSNavigableDocumentListener(CSSNavigableDocumentListener var1) {
      EventListener[] var2 = (EventListener[])this.cssNavigableDocumentListeners.get(var1);
      if (var2 != null) {
         XBLEventSupport var3 = (XBLEventSupport)this.initializeEventSupport();
         var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", var2[0], false);
         var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", var2[1], false);
         var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", var2[2], false);
         var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", var2[3], false);
         var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", var2[4], false);
         this.cssNavigableDocumentListeners.remove(var1);
      }
   }
}

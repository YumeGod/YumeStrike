package org.apache.batik.bridge.svg12;

import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.SVGTextElementBridge;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg12.XBLEventSupport;
import org.apache.batik.dom.xbl.NodeXBL;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MutationEvent;

public class SVG12TextElementBridge extends SVGTextElementBridge implements SVG12BridgeUpdateHandler {
   public Bridge getInstance() {
      return new SVG12TextElementBridge();
   }

   protected void addTextEventListeners(BridgeContext var1, NodeEventTarget var2) {
      if (this.childNodeRemovedEventListener == null) {
         this.childNodeRemovedEventListener = new DOMChildNodeRemovedEventListener();
      }

      if (this.subtreeModifiedEventListener == null) {
         this.subtreeModifiedEventListener = new DOMSubtreeModifiedEventListener();
      }

      SVG12BridgeContext var3 = (SVG12BridgeContext)var1;
      AbstractNode var4 = (AbstractNode)var2;
      XBLEventSupport var5 = (XBLEventSupport)var4.initializeEventSupport();
      var5.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.childNodeRemovedEventListener, true);
      var3.storeImplementationEventListenerNS(var2, "http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.childNodeRemovedEventListener, true);
      var5.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.subtreeModifiedEventListener, false);
      var3.storeImplementationEventListenerNS(var2, "http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.subtreeModifiedEventListener, false);
   }

   protected void removeTextEventListeners(BridgeContext var1, NodeEventTarget var2) {
      AbstractNode var3 = (AbstractNode)var2;
      XBLEventSupport var4 = (XBLEventSupport)var3.initializeEventSupport();
      var4.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.childNodeRemovedEventListener, true);
      var4.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.subtreeModifiedEventListener, false);
   }

   protected Node getFirstChild(Node var1) {
      return ((NodeXBL)var1).getXblFirstChild();
   }

   protected Node getNextSibling(Node var1) {
      return ((NodeXBL)var1).getXblNextSibling();
   }

   protected Node getParentNode(Node var1) {
      return ((NodeXBL)var1).getXblParentNode();
   }

   public void handleDOMCharacterDataModified(MutationEvent var1) {
      Node var2 = (Node)var1.getTarget();
      if (this.isParentDisplayed(var2)) {
         if (this.getParentNode(var2) != var2.getParentNode()) {
            this.computeLaidoutText(this.ctx, this.e, this.node);
         } else {
            this.laidoutText = null;
         }
      }

   }

   public void handleBindingEvent(Element var1, Element var2) {
   }

   public void handleContentSelectionChangedEvent(ContentSelectionChangedEvent var1) {
      this.computeLaidoutText(this.ctx, this.e, this.node);
   }

   protected class DOMSubtreeModifiedEventListener extends SVGTextElementBridge.DOMSubtreeModifiedEventListener {
      protected DOMSubtreeModifiedEventListener() {
         super();
      }

      public void handleEvent(Event var1) {
         super.handleEvent(EventSupport.getUltimateOriginalEvent(var1));
      }
   }

   protected class DOMChildNodeRemovedEventListener extends SVGTextElementBridge.DOMChildNodeRemovedEventListener {
      protected DOMChildNodeRemovedEventListener() {
         super();
      }

      public void handleEvent(Event var1) {
         super.handleEvent(EventSupport.getUltimateOriginalEvent(var1));
      }
   }
}

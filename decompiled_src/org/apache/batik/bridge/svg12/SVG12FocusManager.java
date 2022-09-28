package org.apache.batik.bridge.svg12;

import org.apache.batik.bridge.FocusManager;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.events.DOMUIEvent;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.svg12.XBLEventSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;

public class SVG12FocusManager extends FocusManager {
   public SVG12FocusManager(Document var1) {
      super(var1);
   }

   protected void addEventListeners(Document var1) {
      AbstractNode var2 = (AbstractNode)var1;
      XBLEventSupport var3 = (XBLEventSupport)var2.initializeEventSupport();
      this.mouseclickListener = new MouseClickTracker();
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.mouseclickListener, true);
      this.mouseoverListener = new MouseOverTracker();
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.mouseoverListener, true);
      this.mouseoutListener = new MouseOutTracker();
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.mouseoutListener, true);
      this.domFocusInListener = new DOMFocusInTracker();
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusIn", this.domFocusInListener, true);
      this.domFocusOutListener = new FocusManager.DOMFocusOutTracker();
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusOut", this.domFocusOutListener, true);
   }

   protected void removeEventListeners(Document var1) {
      AbstractNode var2 = (AbstractNode)var1;
      XBLEventSupport var3 = (XBLEventSupport)var2.getEventSupport();
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.mouseclickListener, true);
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.mouseoverListener, true);
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.mouseoutListener, true);
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusIn", this.domFocusInListener, true);
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusOut", this.domFocusOutListener, true);
   }

   protected void fireDOMFocusInEvent(EventTarget var1, EventTarget var2) {
      DocumentEvent var3 = (DocumentEvent)((Element)var1).getOwnerDocument();
      DOMUIEvent var4 = (DOMUIEvent)var3.createEvent("UIEvents");
      var4.initUIEventNS("http://www.w3.org/2001/xml-events", "DOMFocusIn", true, false, (AbstractView)null, 0);
      int var5 = DefaultXBLManager.computeBubbleLimit((Node)var2, (Node)var1);
      var4.setBubbleLimit(var5);
      var1.dispatchEvent(var4);
   }

   protected void fireDOMFocusOutEvent(EventTarget var1, EventTarget var2) {
      DocumentEvent var3 = (DocumentEvent)((Element)var1).getOwnerDocument();
      DOMUIEvent var4 = (DOMUIEvent)var3.createEvent("UIEvents");
      var4.initUIEventNS("http://www.w3.org/2001/xml-events", "DOMFocusOut", true, false, (AbstractView)null, 0);
      int var5 = DefaultXBLManager.computeBubbleLimit((Node)var1, (Node)var2);
      var4.setBubbleLimit(var5);
      var1.dispatchEvent(var4);
   }

   protected class MouseOutTracker extends FocusManager.MouseOutTracker {
      protected MouseOutTracker() {
         super();
      }

      public void handleEvent(Event var1) {
         super.handleEvent(EventSupport.getUltimateOriginalEvent(var1));
      }
   }

   protected class MouseOverTracker extends FocusManager.MouseOverTracker {
      protected MouseOverTracker() {
         super();
      }

      public void handleEvent(Event var1) {
         super.handleEvent(EventSupport.getUltimateOriginalEvent(var1));
      }
   }

   protected class DOMFocusInTracker extends FocusManager.DOMFocusInTracker {
      protected DOMFocusInTracker() {
         super();
      }

      public void handleEvent(Event var1) {
         super.handleEvent(EventSupport.getUltimateOriginalEvent(var1));
      }
   }

   protected class MouseClickTracker extends FocusManager.MouseClickTracker {
      protected MouseClickTracker() {
         super();
      }

      public void handleEvent(Event var1) {
         super.handleEvent(EventSupport.getUltimateOriginalEvent(var1));
      }
   }
}

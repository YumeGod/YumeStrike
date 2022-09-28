package org.apache.batik.bridge;

import org.apache.batik.dom.events.DOMUIEvent;
import org.apache.batik.dom.events.NodeEventTarget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

public class FocusManager {
   protected EventTarget lastFocusEventTarget;
   protected Document document;
   protected EventListener mouseclickListener;
   protected EventListener domFocusInListener;
   protected EventListener domFocusOutListener;
   protected EventListener mouseoverListener;
   protected EventListener mouseoutListener;

   public FocusManager(Document var1) {
      this.document = var1;
      this.addEventListeners(var1);
   }

   protected void addEventListeners(Document var1) {
      NodeEventTarget var2 = (NodeEventTarget)var1;
      this.mouseclickListener = new MouseClickTracker();
      var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.mouseclickListener, true, (Object)null);
      this.mouseoverListener = new MouseOverTracker();
      var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.mouseoverListener, true, (Object)null);
      this.mouseoutListener = new MouseOutTracker();
      var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.mouseoutListener, true, (Object)null);
      this.domFocusInListener = new DOMFocusInTracker();
      var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusIn", this.domFocusInListener, true, (Object)null);
      this.domFocusOutListener = new DOMFocusOutTracker();
      var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusOut", this.domFocusOutListener, true, (Object)null);
   }

   protected void removeEventListeners(Document var1) {
      NodeEventTarget var2 = (NodeEventTarget)var1;
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.mouseclickListener, true);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.mouseoverListener, true);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.mouseoutListener, true);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusIn", this.domFocusInListener, true);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusOut", this.domFocusOutListener, true);
   }

   public EventTarget getCurrentEventTarget() {
      return this.lastFocusEventTarget;
   }

   public void dispose() {
      if (this.document != null) {
         this.removeEventListeners(this.document);
         this.lastFocusEventTarget = null;
         this.document = null;
      }
   }

   protected void fireDOMFocusInEvent(EventTarget var1, EventTarget var2) {
      DocumentEvent var3 = (DocumentEvent)((Element)var1).getOwnerDocument();
      DOMUIEvent var4 = (DOMUIEvent)var3.createEvent("UIEvents");
      var4.initUIEventNS("http://www.w3.org/2001/xml-events", "DOMFocusIn", true, false, (AbstractView)null, 0);
      var1.dispatchEvent(var4);
   }

   protected void fireDOMFocusOutEvent(EventTarget var1, EventTarget var2) {
      DocumentEvent var3 = (DocumentEvent)((Element)var1).getOwnerDocument();
      DOMUIEvent var4 = (DOMUIEvent)var3.createEvent("UIEvents");
      var4.initUIEventNS("http://www.w3.org/2001/xml-events", "DOMFocusOut", true, false, (AbstractView)null, 0);
      var1.dispatchEvent(var4);
   }

   protected void fireDOMActivateEvent(EventTarget var1, int var2) {
      DocumentEvent var3 = (DocumentEvent)((Element)var1).getOwnerDocument();
      DOMUIEvent var4 = (DOMUIEvent)var3.createEvent("UIEvents");
      var4.initUIEventNS("http://www.w3.org/2001/xml-events", "DOMActivate", true, true, (AbstractView)null, 0);
      var1.dispatchEvent(var4);
   }

   protected class MouseOutTracker implements EventListener {
      public void handleEvent(Event var1) {
         MouseEvent var2 = (MouseEvent)var1;
         EventTarget var3 = var1.getTarget();
         EventTarget var4 = var2.getRelatedTarget();
         FocusManager.this.fireDOMFocusOutEvent(var3, var4);
      }
   }

   protected class MouseOverTracker implements EventListener {
      public void handleEvent(Event var1) {
         MouseEvent var2 = (MouseEvent)var1;
         EventTarget var3 = var1.getTarget();
         EventTarget var4 = var2.getRelatedTarget();
         FocusManager.this.fireDOMFocusInEvent(var3, var4);
      }
   }

   protected class DOMFocusOutTracker implements EventListener {
      public DOMFocusOutTracker() {
      }

      public void handleEvent(Event var1) {
         FocusManager.this.lastFocusEventTarget = null;
      }
   }

   protected class DOMFocusInTracker implements EventListener {
      public void handleEvent(Event var1) {
         EventTarget var2 = var1.getTarget();
         if (FocusManager.this.lastFocusEventTarget != null && FocusManager.this.lastFocusEventTarget != var2) {
            FocusManager.this.fireDOMFocusOutEvent(FocusManager.this.lastFocusEventTarget, var2);
         }

         FocusManager.this.lastFocusEventTarget = var1.getTarget();
      }
   }

   protected class MouseClickTracker implements EventListener {
      public void handleEvent(Event var1) {
         MouseEvent var2 = (MouseEvent)var1;
         FocusManager.this.fireDOMActivateEvent(var1.getTarget(), var2.getDetail());
      }
   }
}

package org.apache.batik.anim.timing;

import org.apache.batik.dom.events.DOMKeyEvent;
import org.apache.batik.dom.events.NodeEventTarget;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.KeyboardEvent;

public class AccesskeyTimingSpecifier extends EventLikeTimingSpecifier implements EventListener {
   protected char accesskey;
   protected boolean isSVG12AccessKey;
   protected String keyName;

   public AccesskeyTimingSpecifier(TimedElement var1, boolean var2, float var3, char var4) {
      super(var1, var2, var3);
      this.accesskey = var4;
   }

   public AccesskeyTimingSpecifier(TimedElement var1, boolean var2, float var3, String var4) {
      super(var1, var2, var3);
      this.isSVG12AccessKey = true;
      this.keyName = var4;
   }

   public String toString() {
      return this.isSVG12AccessKey ? "accessKey(" + this.keyName + ")" + (this.offset != 0.0F ? super.toString() : "") : "accesskey(" + this.accesskey + ")" + (this.offset != 0.0F ? super.toString() : "");
   }

   public void initialize() {
      if (this.isSVG12AccessKey) {
         NodeEventTarget var1 = (NodeEventTarget)this.owner.getRootEventTarget();
         var1.addEventListenerNS("http://www.w3.org/2001/xml-events", "keydown", this, false, (Object)null);
      } else {
         EventTarget var2 = this.owner.getRootEventTarget();
         var2.addEventListener("keypress", this, false);
      }

   }

   public void deinitialize() {
      if (this.isSVG12AccessKey) {
         NodeEventTarget var1 = (NodeEventTarget)this.owner.getRootEventTarget();
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "keydown", this, false);
      } else {
         EventTarget var2 = this.owner.getRootEventTarget();
         var2.removeEventListener("keypress", this, false);
      }

   }

   public void handleEvent(Event var1) {
      boolean var2;
      if (var1.getType().charAt(3) == 'p') {
         DOMKeyEvent var3 = (DOMKeyEvent)var1;
         var2 = var3.getCharCode() == this.accesskey;
      } else {
         KeyboardEvent var4 = (KeyboardEvent)var1;
         var2 = var4.getKeyIdentifier().equals(this.keyName);
      }

      if (var2) {
         this.owner.eventOccurred(this, var1);
      }

   }

   public void resolve(Event var1) {
      float var2 = this.owner.getRoot().convertEpochTime(var1.getTimeStamp());
      InstanceTime var3 = new InstanceTime(this, var2 + this.offset, true);
      this.owner.addInstanceTime(var3, this.isBegin);
   }
}

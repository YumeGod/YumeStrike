package org.apache.batik.anim.timing;

import org.apache.batik.dom.events.NodeEventTarget;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class EventbaseTimingSpecifier extends EventLikeTimingSpecifier implements EventListener {
   protected String eventbaseID;
   protected TimedElement eventbase;
   protected EventTarget eventTarget;
   protected String eventNamespaceURI;
   protected String eventType;
   protected String eventName;

   public EventbaseTimingSpecifier(TimedElement var1, boolean var2, float var3, String var4, String var5) {
      super(var1, var2, var3);
      this.eventbaseID = var4;
      this.eventName = var5;
      TimedDocumentRoot var6 = var1.getRoot();
      this.eventNamespaceURI = var6.getEventNamespaceURI(var5);
      this.eventType = var6.getEventType(var5);
      if (var4 == null) {
         this.eventTarget = var1.getAnimationEventTarget();
      } else {
         this.eventTarget = var1.getEventTargetById(var4);
      }

   }

   public String toString() {
      return (this.eventbaseID == null ? "" : this.eventbaseID + ".") + this.eventName + (this.offset != 0.0F ? super.toString() : "");
   }

   public void initialize() {
      ((NodeEventTarget)this.eventTarget).addEventListenerNS(this.eventNamespaceURI, this.eventType, this, false, (Object)null);
   }

   public void deinitialize() {
      ((NodeEventTarget)this.eventTarget).removeEventListenerNS(this.eventNamespaceURI, this.eventType, this, false);
   }

   public void handleEvent(Event var1) {
      this.owner.eventOccurred(this, var1);
   }

   public void resolve(Event var1) {
      float var2 = this.owner.getRoot().convertEpochTime(var1.getTimeStamp());
      InstanceTime var3 = new InstanceTime(this, var2 + this.offset, true);
      this.owner.addInstanceTime(var3, this.isBegin);
   }
}

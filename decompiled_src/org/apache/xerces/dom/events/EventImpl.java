package org.apache.xerces.dom.events;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

public class EventImpl implements Event {
   public String type = null;
   public EventTarget target;
   public EventTarget currentTarget;
   public short eventPhase;
   public boolean initialized = false;
   public boolean bubbles = true;
   public boolean cancelable = false;
   public boolean stopPropagation = false;
   public boolean preventDefault = false;
   protected long timeStamp = System.currentTimeMillis();

   public void initEvent(String var1, boolean var2, boolean var3) {
      this.type = var1;
      this.bubbles = var2;
      this.cancelable = var3;
      this.initialized = true;
   }

   public boolean getBubbles() {
      return this.bubbles;
   }

   public boolean getCancelable() {
      return this.cancelable;
   }

   public EventTarget getCurrentTarget() {
      return this.currentTarget;
   }

   public short getEventPhase() {
      return this.eventPhase;
   }

   public EventTarget getTarget() {
      return this.target;
   }

   public String getType() {
      return this.type;
   }

   public long getTimeStamp() {
      return this.timeStamp;
   }

   public void stopPropagation() {
      this.stopPropagation = true;
   }

   public void preventDefault() {
      this.preventDefault = true;
   }
}

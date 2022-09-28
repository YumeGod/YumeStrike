package org.apache.batik.dom.events;

import java.util.ArrayList;
import java.util.List;
import org.apache.batik.dom.xbl.OriginalEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

public abstract class AbstractEvent implements Event, OriginalEvent, Cloneable {
   protected String type;
   protected boolean isBubbling;
   protected boolean cancelable;
   protected EventTarget currentTarget;
   protected EventTarget target;
   protected short eventPhase;
   protected long timeStamp = System.currentTimeMillis();
   protected boolean stopPropagation = false;
   protected boolean stopImmediatePropagation = false;
   protected boolean preventDefault = false;
   protected String namespaceURI;
   protected Event originalEvent;
   protected List defaultActions;
   protected int bubbleLimit = 0;

   public String getType() {
      return this.type;
   }

   public EventTarget getCurrentTarget() {
      return this.currentTarget;
   }

   public EventTarget getTarget() {
      return this.target;
   }

   public short getEventPhase() {
      return this.eventPhase;
   }

   public boolean getBubbles() {
      return this.isBubbling;
   }

   public boolean getCancelable() {
      return this.cancelable;
   }

   public long getTimeStamp() {
      return this.timeStamp;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public Event getOriginalEvent() {
      return this.originalEvent;
   }

   public void stopPropagation() {
      this.stopPropagation = true;
   }

   public void preventDefault() {
      this.preventDefault = true;
   }

   public boolean getDefaultPrevented() {
      return this.preventDefault;
   }

   public List getDefaultActions() {
      return this.defaultActions;
   }

   public void addDefaultAction(Runnable var1) {
      if (this.defaultActions == null) {
         this.defaultActions = new ArrayList();
      }

      this.defaultActions.add(var1);
   }

   public void stopImmediatePropagation() {
      this.stopImmediatePropagation = true;
   }

   public void initEvent(String var1, boolean var2, boolean var3) {
      this.type = var1;
      this.isBubbling = var2;
      this.cancelable = var3;
   }

   public void initEventNS(String var1, String var2, boolean var3, boolean var4) {
      if (this.namespaceURI != null && this.namespaceURI.length() == 0) {
         this.namespaceURI = null;
      }

      this.namespaceURI = var1;
      this.type = var2;
      this.isBubbling = var3;
      this.cancelable = var4;
   }

   boolean getStopPropagation() {
      return this.stopPropagation;
   }

   boolean getStopImmediatePropagation() {
      return this.stopImmediatePropagation;
   }

   void setEventPhase(short var1) {
      this.eventPhase = var1;
   }

   void stopPropagation(boolean var1) {
      this.stopPropagation = var1;
   }

   void stopImmediatePropagation(boolean var1) {
      this.stopImmediatePropagation = var1;
   }

   void preventDefault(boolean var1) {
      this.preventDefault = var1;
   }

   void setCurrentTarget(EventTarget var1) {
      this.currentTarget = var1;
   }

   void setTarget(EventTarget var1) {
      this.target = var1;
   }

   public Object clone() throws CloneNotSupportedException {
      AbstractEvent var1 = (AbstractEvent)super.clone();
      var1.timeStamp = System.currentTimeMillis();
      return var1;
   }

   public AbstractEvent cloneEvent() {
      try {
         AbstractEvent var1 = (AbstractEvent)this.clone();
         var1.originalEvent = this;
         return var1;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public int getBubbleLimit() {
      return this.bubbleLimit;
   }

   public void setBubbleLimit(int var1) {
      this.bubbleLimit = var1;
   }
}

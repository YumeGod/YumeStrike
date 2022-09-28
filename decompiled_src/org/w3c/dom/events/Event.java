package org.w3c.dom.events;

public interface Event {
   short CAPTURING_PHASE = 1;
   short AT_TARGET = 2;
   short BUBBLING_PHASE = 3;

   String getType();

   EventTarget getTarget();

   EventTarget getCurrentTarget();

   short getEventPhase();

   boolean getBubbles();

   boolean getCancelable();

   long getTimeStamp();

   void stopPropagation();

   void preventDefault();

   void initEvent(String var1, boolean var2, boolean var3);
}

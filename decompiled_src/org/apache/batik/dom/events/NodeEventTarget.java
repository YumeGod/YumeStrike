package org.apache.batik.dom.events;

import org.w3c.dom.DOMException;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public interface NodeEventTarget extends EventTarget {
   EventSupport getEventSupport();

   NodeEventTarget getParentNodeEventTarget();

   boolean dispatchEvent(Event var1) throws EventException, DOMException;

   void addEventListenerNS(String var1, String var2, EventListener var3, boolean var4, Object var5);

   void removeEventListenerNS(String var1, String var2, EventListener var3, boolean var4);
}

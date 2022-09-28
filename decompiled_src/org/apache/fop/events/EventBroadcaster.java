package org.apache.fop.events;

public interface EventBroadcaster {
   void addEventListener(EventListener var1);

   void removeEventListener(EventListener var1);

   boolean hasEventListeners();

   void broadcastEvent(Event var1);

   EventProducer getEventProducerFor(Class var1);
}

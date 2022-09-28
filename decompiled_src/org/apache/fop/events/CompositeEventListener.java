package org.apache.fop.events;

import java.util.ArrayList;
import java.util.List;

public class CompositeEventListener implements EventListener {
   private List listeners = new ArrayList();

   public synchronized void addEventListener(EventListener listener) {
      this.listeners.add(listener);
   }

   public synchronized void removeEventListener(EventListener listener) {
      this.listeners.remove(listener);
   }

   private synchronized int getListenerCount() {
      return this.listeners.size();
   }

   public boolean hasEventListeners() {
      return this.getListenerCount() > 0;
   }

   public synchronized void processEvent(Event event) {
      int i = 0;

      for(int c = this.getListenerCount(); i < c; ++i) {
         EventListener listener = (EventListener)this.listeners.get(i);
         listener.processEvent(event);
      }

   }
}

package org.apache.fop.render.pcl;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;

public interface PCLEventProducer extends EventProducer {
   void paperTypeUnavailable(Object var1, long var2, long var4, String var6);

   public static class Provider {
      public static PCLEventProducer get(EventBroadcaster broadcaster) {
         return (PCLEventProducer)broadcaster.getEventProducerFor(PCLEventProducer.class);
      }
   }
}

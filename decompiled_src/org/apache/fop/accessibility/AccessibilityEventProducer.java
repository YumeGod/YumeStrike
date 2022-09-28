package org.apache.fop.accessibility;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;

public interface AccessibilityEventProducer extends EventProducer {
   void noStructureTreeInXML(Object var1);

   public static final class Provider {
      private Provider() {
      }

      public static AccessibilityEventProducer get(EventBroadcaster broadcaster) {
         return (AccessibilityEventProducer)broadcaster.getEventProducerFor(AccessibilityEventProducer.class);
      }
   }
}

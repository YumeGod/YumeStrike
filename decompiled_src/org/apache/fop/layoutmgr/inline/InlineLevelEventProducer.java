package org.apache.fop.layoutmgr.inline;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;
import org.xml.sax.Locator;

public interface InlineLevelEventProducer extends EventProducer {
   void leaderWithoutContent(Object var1, Locator var2);

   void lineOverflows(Object var1, int var2, int var3, Locator var4);

   public static class Provider {
      public static InlineLevelEventProducer get(EventBroadcaster broadcaster) {
         return (InlineLevelEventProducer)broadcaster.getEventProducerFor(InlineLevelEventProducer.class);
      }
   }
}

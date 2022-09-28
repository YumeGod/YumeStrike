package org.apache.fop.render.ps;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;

public interface PSEventProducer extends EventProducer {
   void postscriptDictionaryParseError(Object var1, String var2, Exception var3);

   public static class Provider {
      public static PSEventProducer get(EventBroadcaster broadcaster) {
         return (PSEventProducer)broadcaster.getEventProducerFor(PSEventProducer.class);
      }
   }
}

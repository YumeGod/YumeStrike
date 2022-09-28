package org.apache.fop.afp;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;

public interface AFPEventProducer extends EventProducer {
   void warnDefaultFontSetup(Object var1);

   void warnMissingDefaultFont(Object var1, String var2, int var3);

   void characterSetEncodingError(Object var1, String var2, String var3);

   void resourceEmbeddingError(Object var1, String var2, Exception var3);

   public static class Provider {
      public static AFPEventProducer get(EventBroadcaster broadcaster) {
         return (AFPEventProducer)broadcaster.getEventProducerFor(AFPEventProducer.class);
      }
   }
}

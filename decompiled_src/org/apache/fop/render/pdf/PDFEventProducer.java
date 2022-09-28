package org.apache.fop.render.pdf;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;

public interface PDFEventProducer extends EventProducer {
   void nonFullyResolvedLinkTargets(Object var1, int var2);

   void nonStandardStructureType(Object var1, String var2, String var3, String var4);

   public static final class Provider {
      private Provider() {
      }

      public static PDFEventProducer get(EventBroadcaster broadcaster) {
         return (PDFEventProducer)broadcaster.getEventProducerFor(PDFEventProducer.class);
      }
   }
}

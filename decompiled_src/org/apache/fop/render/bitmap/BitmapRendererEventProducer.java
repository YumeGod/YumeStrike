package org.apache.fop.render.bitmap;

import java.io.IOException;
import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;

public interface BitmapRendererEventProducer extends EventProducer {
   void stoppingAfterFirstPageNoFilename(Object var1);

   void stoppingAfterFirstPageNoMultiWriter(Object var1);

   void noImageWriterFound(Object var1, String var2) throws IOException;

   public static class Provider {
      public static BitmapRendererEventProducer get(EventBroadcaster broadcaster) {
         return (BitmapRendererEventProducer)broadcaster.getEventProducerFor(BitmapRendererEventProducer.class);
      }
   }
}

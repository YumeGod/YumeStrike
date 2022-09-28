package org.apache.fop.fonts;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;

public interface FontEventProducer extends EventProducer {
   void fontSubstituted(Object var1, FontTriplet var2, FontTriplet var3);

   void fontLoadingErrorAtAutoDetection(Object var1, String var2, Exception var3);

   void glyphNotAvailable(Object var1, char var2, String var3);

   public static final class Provider {
      private Provider() {
      }

      public static FontEventProducer get(EventBroadcaster broadcaster) {
         return (FontEventProducer)broadcaster.getEventProducerFor(FontEventProducer.class);
      }
   }
}

package org.apache.fop.render.rtf;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;
import org.apache.fop.fo.FONode;
import org.xml.sax.Locator;

public interface RTFEventProducer extends EventProducer {
   void onlySPMSupported(Object var1, String var2, Locator var3);

   void noSPMFound(Object var1, Locator var2);

   void explicitTableColumnsRequired(Object var1, Locator var2);

   void ignoredDeferredEvent(Object var1, FONode var2, boolean var3, Locator var4);

   public static class Provider {
      public static RTFEventProducer get(EventBroadcaster broadcaster) {
         return (RTFEventProducer)broadcaster.getEventProducerFor(RTFEventProducer.class);
      }
   }
}

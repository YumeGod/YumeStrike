package org.apache.fop.area;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;

public interface AreaEventProducer extends EventProducer {
   void unresolvedIDReference(Object var1, String var2, String var3);

   void unresolvedIDReferenceOnPage(Object var1, String var2, String var3);

   void pageLoadError(Object var1, String var2, Exception var3);

   void pageSaveError(Object var1, String var2, Exception var3);

   void pageRenderingError(Object var1, String var2, Exception var3);

   public static class Provider {
      public static AreaEventProducer get(EventBroadcaster broadcaster) {
         return (AreaEventProducer)broadcaster.getEventProducerFor(AreaEventProducer.class);
      }
   }
}

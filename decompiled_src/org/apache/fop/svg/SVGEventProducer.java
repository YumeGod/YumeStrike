package org.apache.fop.svg;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;

public interface SVGEventProducer extends EventProducer {
   void error(Object var1, String var2, Exception var3);

   void alert(Object var1, String var2);

   void info(Object var1, String var2);

   void svgNotBuilt(Object var1, Exception var2, String var3);

   void svgRenderingError(Object var1, Exception var2, String var3);

   public static class Provider {
      public static SVGEventProducer get(EventBroadcaster broadcaster) {
         return (SVGEventProducer)broadcaster.getEventProducerFor(SVGEventProducer.class);
      }
   }
}

package org.apache.fop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.w3c.dom.Document;
import org.xml.sax.Locator;

public interface ResourceEventProducer extends EventProducer {
   void imageNotFound(Object var1, String var2, FileNotFoundException var3, Locator var4);

   void imageError(Object var1, String var2, ImageException var3, Locator var4);

   void imageIOError(Object var1, String var2, IOException var3, Locator var4);

   void imageWritingError(Object var1, Exception var2);

   void uriError(Object var1, String var2, Exception var3, Locator var4);

   void ifoNoIntrinsicSize(Object var1, Locator var2);

   void foreignXMLProcessingError(Object var1, Document var2, String var3, Exception var4);

   void foreignXMLNoHandler(Object var1, Document var2, String var3);

   void cannotDeleteTempFile(Object var1, File var2);

   void catalogResolverNotFound(Object var1);

   void catalogResolverNotCreated(Object var1, String var2);

   public static class Provider {
      public static ResourceEventProducer get(EventBroadcaster broadcaster) {
         return (ResourceEventProducer)broadcaster.getEventProducerFor(ResourceEventProducer.class);
      }
   }
}

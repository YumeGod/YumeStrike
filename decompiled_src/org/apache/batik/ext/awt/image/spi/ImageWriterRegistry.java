package org.apache.batik.ext.awt.image.spi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.util.Service;

public class ImageWriterRegistry {
   private static volatile ImageWriterRegistry instance;
   private final Map imageWriterMap = new HashMap();
   // $FF: synthetic field
   static Class class$org$apache$batik$ext$awt$image$spi$ImageWriterRegistry;
   // $FF: synthetic field
   static Class class$org$apache$batik$ext$awt$image$spi$ImageWriter;

   private ImageWriterRegistry() {
      this.setup();
   }

   public static ImageWriterRegistry getInstance() {
      if (instance == null) {
         synchronized(class$org$apache$batik$ext$awt$image$spi$ImageWriterRegistry == null ? (class$org$apache$batik$ext$awt$image$spi$ImageWriterRegistry = class$("org.apache.batik.ext.awt.image.spi.ImageWriterRegistry")) : class$org$apache$batik$ext$awt$image$spi$ImageWriterRegistry) {
            if (instance == null) {
               instance = new ImageWriterRegistry();
            }
         }
      }

      return instance;
   }

   private void setup() {
      Iterator var1 = Service.providers(class$org$apache$batik$ext$awt$image$spi$ImageWriter == null ? (class$org$apache$batik$ext$awt$image$spi$ImageWriter = class$("org.apache.batik.ext.awt.image.spi.ImageWriter")) : class$org$apache$batik$ext$awt$image$spi$ImageWriter);

      while(var1.hasNext()) {
         ImageWriter var2 = (ImageWriter)var1.next();
         this.register(var2);
      }

   }

   public void register(ImageWriter var1) {
      this.imageWriterMap.put(var1.getMIMEType(), var1);
   }

   public ImageWriter getWriterFor(String var1) {
      return (ImageWriter)this.imageWriterMap.get(var1);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}

package org.apache.xmlgraphics.image.writer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import org.apache.xmlgraphics.util.Service;

public class ImageWriterRegistry {
   private static ImageWriterRegistry instance;
   private Map imageWriterMap = new HashMap();
   private Map preferredOrder;

   public ImageWriterRegistry() {
      Properties props = new Properties();
      InputStream in = this.getClass().getResourceAsStream("default-preferred-order.properties");
      if (in != null) {
         try {
            try {
               props.load(in);
            } finally {
               in.close();
            }
         } catch (IOException var7) {
            throw new RuntimeException("Could not load default preferred order due to I/O error: " + var7.getMessage());
         }
      }

      this.preferredOrder = props;
      this.setup();
   }

   public ImageWriterRegistry(Properties preferredOrder) {
      this.preferredOrder = preferredOrder;
      this.setup();
   }

   public static ImageWriterRegistry getInstance() {
      if (instance == null) {
         instance = new ImageWriterRegistry();
      }

      return instance;
   }

   private void setup() {
      Iterator iter = Service.providers(ImageWriter.class);

      while(iter.hasNext()) {
         ImageWriter writer = (ImageWriter)iter.next();
         this.register(writer);
      }

   }

   private int getPriority(ImageWriter writer) {
      String key = writer.getClass().getName();

      Object value;
      for(value = this.preferredOrder.get(key); value == null; value = this.preferredOrder.get(key)) {
         int pos = key.lastIndexOf(".");
         if (pos < 0) {
            break;
         }

         key = key.substring(0, pos);
      }

      return value != null ? Integer.parseInt(value.toString()) : 0;
   }

   public void register(ImageWriter writer) {
      List entries = (List)this.imageWriterMap.get(writer.getMIMEType());
      if (entries == null) {
         entries = new ArrayList();
         this.imageWriterMap.put(writer.getMIMEType(), entries);
      }

      int priority = this.getPriority(writer);
      ListIterator li = ((List)entries).listIterator();

      ImageWriter w;
      do {
         if (!li.hasNext()) {
            li.add(writer);
            return;
         }

         w = (ImageWriter)li.next();
      } while(this.getPriority(w) >= priority);

      li.previous();
      li.add(writer);
   }

   public ImageWriter getWriterFor(String mime) {
      List entries = (List)this.imageWriterMap.get(mime);
      if (entries == null) {
         return null;
      } else {
         Iterator iter = entries.iterator();

         ImageWriter writer;
         do {
            if (!iter.hasNext()) {
               return null;
            }

            writer = (ImageWriter)iter.next();
         } while(!writer.isFunctional());

         return writer;
      }
   }
}

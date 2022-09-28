package org.apache.fop.pdf;

import java.io.IOException;

public class StreamCacheFactory {
   private static boolean defaultCacheToFile = false;
   private static StreamCacheFactory fileInstance = null;
   private static StreamCacheFactory memoryInstance = null;
   private boolean cacheToFile = false;

   public static StreamCacheFactory getInstance(boolean cacheToFile) {
      if (cacheToFile) {
         if (fileInstance == null) {
            fileInstance = new StreamCacheFactory(true);
         }

         return fileInstance;
      } else {
         if (memoryInstance == null) {
            memoryInstance = new StreamCacheFactory(false);
         }

         return memoryInstance;
      }
   }

   public static StreamCacheFactory getInstance() {
      return getInstance(defaultCacheToFile);
   }

   public static void setDefaultCacheToFile(boolean cacheToFile) {
      defaultCacheToFile = cacheToFile;
   }

   public StreamCacheFactory(boolean cacheToFile) {
      this.cacheToFile = cacheToFile;
   }

   public StreamCache createStreamCache() throws IOException {
      return (StreamCache)(this.cacheToFile ? new TempFileStreamCache() : new InMemoryStreamCache());
   }

   public StreamCache createStreamCache(int hintSize) throws IOException {
      return (StreamCache)(this.cacheToFile ? new TempFileStreamCache() : new InMemoryStreamCache(hintSize));
   }

   public boolean getCacheToFile() {
      return this.cacheToFile;
   }
}

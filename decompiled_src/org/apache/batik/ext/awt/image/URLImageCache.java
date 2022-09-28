package org.apache.batik.ext.awt.image;

import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.SoftReferenceCache;

public class URLImageCache extends SoftReferenceCache {
   static URLImageCache theCache = new URLImageCache();

   public static URLImageCache getDefaultCache() {
      return theCache;
   }

   public synchronized boolean isPresent(ParsedURL var1) {
      return super.isPresentImpl(var1);
   }

   public synchronized boolean isDone(ParsedURL var1) {
      return super.isDoneImpl(var1);
   }

   public synchronized Filter request(ParsedURL var1) {
      return (Filter)super.requestImpl(var1);
   }

   public synchronized void clear(ParsedURL var1) {
      super.clearImpl(var1);
   }

   public synchronized void put(ParsedURL var1, Filter var2) {
      super.putImpl(var1, var2);
   }
}

package org.apache.batik.ext.awt.color;

import org.apache.batik.util.SoftReferenceCache;

public class NamedProfileCache extends SoftReferenceCache {
   static NamedProfileCache theCache = new NamedProfileCache();

   public static NamedProfileCache getDefaultCache() {
      return theCache;
   }

   public synchronized boolean isPresent(String var1) {
      return super.isPresentImpl(var1);
   }

   public synchronized boolean isDone(String var1) {
      return super.isDoneImpl(var1);
   }

   public synchronized ICCColorSpaceExt request(String var1) {
      return (ICCColorSpaceExt)super.requestImpl(var1);
   }

   public synchronized void clear(String var1) {
      super.clearImpl(var1);
   }

   public synchronized void put(String var1, ICCColorSpaceExt var2) {
      super.putImpl(var1, var2);
   }
}

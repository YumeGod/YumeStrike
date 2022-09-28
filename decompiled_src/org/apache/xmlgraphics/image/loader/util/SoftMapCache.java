package org.apache.xmlgraphics.image.loader.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SoftMapCache {
   private static Log log;
   private Map map = new HashMap();
   private ReferenceQueue refQueue = new ReferenceQueue();

   public SoftMapCache(boolean synched) {
      if (synched) {
         this.map = Collections.synchronizedMap(this.map);
      }

   }

   public Object get(Object key) {
      Reference ref = (Reference)this.map.get(key);
      return this.getReference(key, ref);
   }

   public Object remove(Object key) {
      Reference ref = (Reference)this.map.remove(key);
      return this.getReference(key, ref);
   }

   private Object getReference(Object key, Reference ref) {
      Object value = null;
      if (ref != null) {
         value = ref.get();
         if (value == null) {
            if (log.isTraceEnabled()) {
               log.trace("Image has been collected: " + key);
            }

            this.checkReferenceQueue();
         }
      }

      return value;
   }

   public void put(Object key, Object value) {
      this.map.put(key, this.wrapInReference(value, key));
   }

   public void clear() {
      this.map.clear();
   }

   public void doHouseKeeping() {
      this.checkReferenceQueue();
   }

   private Reference wrapInReference(Object obj, Object key) {
      return new SoftReferenceWithKey(obj, key, this.refQueue);
   }

   private void checkReferenceQueue() {
      SoftReferenceWithKey ref;
      for(; (ref = (SoftReferenceWithKey)this.refQueue.poll()) != null; this.map.remove(ref.getKey())) {
         if (log.isTraceEnabled()) {
            log.trace("Removing ImageInfo from ref queue: " + ref.getKey());
         }
      }

   }

   static {
      log = LogFactory.getLog(SoftMapCache.class);
   }
}

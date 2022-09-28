package org.apache.xmlgraphics.image.loader.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class SoftReferenceWithKey extends SoftReference {
   private Object key;

   public SoftReferenceWithKey(Object referent, Object key, ReferenceQueue q) {
      super(referent, q);
      this.key = key;
   }

   public Object getKey() {
      return this.key;
   }
}

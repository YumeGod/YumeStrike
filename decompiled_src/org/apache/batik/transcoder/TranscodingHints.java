package org.apache.batik.transcoder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TranscodingHints extends HashMap {
   public TranscodingHints() {
      this((Map)null);
   }

   public TranscodingHints(Map var1) {
      super(7);
      if (var1 != null) {
         this.putAll(var1);
      }

   }

   public boolean containsKey(Object var1) {
      return super.containsKey(var1);
   }

   public Object get(Object var1) {
      return super.get(var1);
   }

   public Object put(Object var1, Object var2) {
      if (!((Key)var1).isCompatibleValue(var2)) {
         throw new IllegalArgumentException(var2 + " incompatible with " + var1);
      } else {
         return super.put(var1, var2);
      }
   }

   public Object remove(Object var1) {
      return super.remove(var1);
   }

   public void putAll(TranscodingHints var1) {
      super.putAll(var1);
   }

   public void putAll(Map var1) {
      if (var1 instanceof TranscodingHints) {
         this.putAll((TranscodingHints)var1);
      } else {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            this.put(var3.getKey(), var3.getValue());
         }
      }

   }

   public abstract static class Key {
      protected Key() {
      }

      public abstract boolean isCompatibleValue(Object var1);
   }
}

package com.mxgraph.util;

import java.util.Hashtable;
import java.util.Map;

public class mxEventObject {
   protected String name;
   protected Map properties;
   protected boolean consumed;

   public mxEventObject(String var1) {
      this(var1, (Object[])null);
   }

   public mxEventObject(String var1, Object... var2) {
      this.consumed = false;
      this.name = var1;
      this.properties = new Hashtable();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; var3 += 2) {
            if (var2[var3 + 1] != null) {
               this.properties.put(String.valueOf(var2[var3]), var2[var3 + 1]);
            }
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public Map getProperties() {
      return this.properties;
   }

   public Object getProperty(String var1) {
      return this.properties.get(var1);
   }

   public boolean isConsumed() {
      return this.consumed;
   }

   public void consume() {
      this.consumed = true;
   }
}

package org.apache.xalan.xsltc.compiler.util;

import java.util.Hashtable;
import java.util.Vector;

public final class MultiHashtable extends Hashtable {
   static final long serialVersionUID = -6151608290510033572L;

   public Object put(Object key, Object value) {
      Vector vector = (Vector)this.get(key);
      if (vector == null) {
         super.put(key, vector = new Vector());
      }

      vector.add(value);
      return vector;
   }

   public Object maps(Object from, Object to) {
      if (from == null) {
         return null;
      } else {
         Vector vector = (Vector)this.get(from);
         if (vector != null) {
            int n = vector.size();

            for(int i = 0; i < n; ++i) {
               Object item = vector.elementAt(i);
               if (item.equals(to)) {
                  return item;
               }
            }
         }

         return null;
      }
   }
}

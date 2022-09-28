package org.apache.james.mime4j.stream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RawBody {
   private final String value;
   private final List params;

   RawBody(String value, List params) {
      if (value == null) {
         throw new IllegalArgumentException("Field value not be null");
      } else {
         this.value = value;
         this.params = (List)(params != null ? params : new ArrayList());
      }
   }

   public String getValue() {
      return this.value;
   }

   public List getParams() {
      return new ArrayList(this.params);
   }

   public String toString() {
      StringBuilder buf = new StringBuilder();
      buf.append(this.value);
      buf.append("; ");
      Iterator i$ = this.params.iterator();

      while(i$.hasNext()) {
         NameValuePair param = (NameValuePair)i$.next();
         buf.append("; ");
         buf.append(param);
      }

      return buf.toString();
   }
}

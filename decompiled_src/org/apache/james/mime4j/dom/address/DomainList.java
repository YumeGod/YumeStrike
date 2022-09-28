package org.apache.james.mime4j.dom.address;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DomainList extends AbstractList implements Serializable {
   private static final long serialVersionUID = 1L;
   private final List domains;

   public DomainList(List domains, boolean dontCopy) {
      if (domains != null) {
         this.domains = (List)(dontCopy ? domains : new ArrayList(domains));
      } else {
         this.domains = Collections.emptyList();
      }

   }

   public int size() {
      return this.domains.size();
   }

   public String get(int index) {
      return (String)this.domains.get(index);
   }

   public String toRouteString() {
      StringBuilder sb = new StringBuilder();
      Iterator i$ = this.domains.iterator();

      while(i$.hasNext()) {
         String domain = (String)i$.next();
         if (sb.length() > 0) {
            sb.append(',');
         }

         sb.append("@");
         sb.append(domain);
      }

      return sb.toString();
   }

   public String toString() {
      return this.toRouteString();
   }
}

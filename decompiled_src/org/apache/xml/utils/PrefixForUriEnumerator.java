package org.apache.xml.utils;

import java.util.Enumeration;
import java.util.NoSuchElementException;

class PrefixForUriEnumerator implements Enumeration {
   private Enumeration allPrefixes;
   private String uri;
   private String lookahead = null;
   private NamespaceSupport2 nsup;

   PrefixForUriEnumerator(NamespaceSupport2 nsup, String uri, Enumeration allPrefixes) {
      this.nsup = nsup;
      this.uri = uri;
      this.allPrefixes = allPrefixes;
   }

   public boolean hasMoreElements() {
      if (this.lookahead != null) {
         return true;
      } else {
         while(this.allPrefixes.hasMoreElements()) {
            String prefix = (String)this.allPrefixes.nextElement();
            if (this.uri.equals(this.nsup.getURI(prefix))) {
               this.lookahead = prefix;
               return true;
            }
         }

         return false;
      }
   }

   public Object nextElement() {
      if (this.hasMoreElements()) {
         String tmp = this.lookahead;
         this.lookahead = null;
         return tmp;
      } else {
         throw new NoSuchElementException();
      }
   }
}

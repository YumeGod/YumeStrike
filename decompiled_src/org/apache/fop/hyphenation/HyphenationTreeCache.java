package org.apache.fop.hyphenation;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class HyphenationTreeCache {
   private Hashtable hyphenTrees = new Hashtable();
   private Set missingHyphenationTrees;

   public HyphenationTree getHyphenationTree(String lang, String country) {
      String key = constructKey(lang, country);
      if (this.hyphenTrees.containsKey(key)) {
         return (HyphenationTree)this.hyphenTrees.get(key);
      } else {
         return this.hyphenTrees.containsKey(lang) ? (HyphenationTree)this.hyphenTrees.get(lang) : null;
      }
   }

   public static String constructKey(String lang, String country) {
      String key = lang;
      if (country != null && !country.equals("none")) {
         key = lang + "_" + country;
      }

      return key;
   }

   public void cache(String key, HyphenationTree hTree) {
      this.hyphenTrees.put(key, hTree);
   }

   public void noteMissing(String key) {
      if (this.missingHyphenationTrees == null) {
         this.missingHyphenationTrees = new HashSet();
      }

      this.missingHyphenationTrees.add(key);
   }

   public boolean isMissing(String key) {
      return this.missingHyphenationTrees != null && this.missingHyphenationTrees.contains(key);
   }
}

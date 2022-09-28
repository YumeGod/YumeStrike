package org.apache.commons.io.filefilter;

import java.io.File;
import java.util.List;

public class PrefixFileFilter extends AbstractFileFilter {
   private String[] prefixes;

   public PrefixFileFilter(String prefix) {
      if (prefix == null) {
         throw new IllegalArgumentException("The prefix must not be null");
      } else {
         this.prefixes = new String[]{prefix};
      }
   }

   public PrefixFileFilter(String[] prefixes) {
      if (prefixes == null) {
         throw new IllegalArgumentException("The array of prefixes must not be null");
      } else {
         this.prefixes = prefixes;
      }
   }

   public PrefixFileFilter(List prefixes) {
      if (prefixes == null) {
         throw new IllegalArgumentException("The list of prefixes must not be null");
      } else {
         this.prefixes = (String[])prefixes.toArray(new String[prefixes.size()]);
      }
   }

   public boolean accept(File file) {
      String name = file.getName();

      for(int i = 0; i < this.prefixes.length; ++i) {
         if (name.startsWith(this.prefixes[i])) {
            return true;
         }
      }

      return false;
   }

   public boolean accept(File file, String name) {
      for(int i = 0; i < this.prefixes.length; ++i) {
         if (name.startsWith(this.prefixes[i])) {
            return true;
         }
      }

      return false;
   }
}

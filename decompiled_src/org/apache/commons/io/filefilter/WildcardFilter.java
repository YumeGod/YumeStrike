package org.apache.commons.io.filefilter;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/** @deprecated */
public class WildcardFilter extends AbstractFileFilter {
   private String[] wildcards;

   public WildcardFilter(String wildcard) {
      if (wildcard == null) {
         throw new IllegalArgumentException("The wildcard must not be null");
      } else {
         this.wildcards = new String[]{wildcard};
      }
   }

   public WildcardFilter(String[] wildcards) {
      if (wildcards == null) {
         throw new IllegalArgumentException("The wildcard array must not be null");
      } else {
         this.wildcards = wildcards;
      }
   }

   public WildcardFilter(List wildcards) {
      if (wildcards == null) {
         throw new IllegalArgumentException("The wildcard list must not be null");
      } else {
         this.wildcards = (String[])wildcards.toArray(new String[wildcards.size()]);
      }
   }

   public boolean accept(File dir, String name) {
      if (dir != null && (new File(dir, name)).isDirectory()) {
         return false;
      } else {
         for(int i = 0; i < this.wildcards.length; ++i) {
            if (FilenameUtils.wildcardMatch(name, this.wildcards[i])) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean accept(File file) {
      if (file.isDirectory()) {
         return false;
      } else {
         for(int i = 0; i < this.wildcards.length; ++i) {
            if (FilenameUtils.wildcardMatch(file.getName(), this.wildcards[i])) {
               return true;
            }
         }

         return false;
      }
   }
}

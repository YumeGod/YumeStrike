package org.apache.commons.io.filefilter;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;

public class WildcardFileFilter extends AbstractFileFilter {
   private String[] wildcards;
   private IOCase caseSensitivity;

   public WildcardFileFilter(String wildcard) {
      this((String)wildcard, (IOCase)null);
   }

   public WildcardFileFilter(String wildcard, IOCase caseSensitivity) {
      if (wildcard == null) {
         throw new IllegalArgumentException("The wildcard must not be null");
      } else {
         this.wildcards = new String[]{wildcard};
         this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
      }
   }

   public WildcardFileFilter(String[] wildcards) {
      this((String[])wildcards, (IOCase)null);
   }

   public WildcardFileFilter(String[] wildcards, IOCase caseSensitivity) {
      if (wildcards == null) {
         throw new IllegalArgumentException("The wildcard array must not be null");
      } else {
         this.wildcards = wildcards;
         this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
      }
   }

   public WildcardFileFilter(List wildcards) {
      this((List)wildcards, (IOCase)null);
   }

   public WildcardFileFilter(List wildcards, IOCase caseSensitivity) {
      if (wildcards == null) {
         throw new IllegalArgumentException("The wildcard list must not be null");
      } else {
         this.wildcards = (String[])wildcards.toArray(new String[wildcards.size()]);
         this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
      }
   }

   public boolean accept(File dir, String name) {
      for(int i = 0; i < this.wildcards.length; ++i) {
         if (FilenameUtils.wildcardMatch(name, this.wildcards[i], this.caseSensitivity)) {
            return true;
         }
      }

      return false;
   }

   public boolean accept(File file) {
      String name = file.getName();

      for(int i = 0; i < this.wildcards.length; ++i) {
         if (FilenameUtils.wildcardMatch(name, this.wildcards[i], this.caseSensitivity)) {
            return true;
         }
      }

      return false;
   }
}

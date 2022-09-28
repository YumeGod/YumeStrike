package org.apache.commons.io.filefilter;

import java.io.File;
import java.util.List;
import org.apache.commons.io.IOCase;

public class NameFileFilter extends AbstractFileFilter {
   private String[] names;
   private IOCase caseSensitivity;

   public NameFileFilter(String name) {
      this((String)name, (IOCase)null);
   }

   public NameFileFilter(String name, IOCase caseSensitivity) {
      if (name == null) {
         throw new IllegalArgumentException("The wildcard must not be null");
      } else {
         this.names = new String[]{name};
         this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
      }
   }

   public NameFileFilter(String[] names) {
      this((String[])names, (IOCase)null);
   }

   public NameFileFilter(String[] names, IOCase caseSensitivity) {
      if (names == null) {
         throw new IllegalArgumentException("The array of names must not be null");
      } else {
         this.names = names;
         this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
      }
   }

   public NameFileFilter(List names) {
      this((List)names, (IOCase)null);
   }

   public NameFileFilter(List names, IOCase caseSensitivity) {
      if (names == null) {
         throw new IllegalArgumentException("The list of names must not be null");
      } else {
         this.names = (String[])names.toArray(new String[names.size()]);
         this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
      }
   }

   public boolean accept(File file) {
      String name = file.getName();

      for(int i = 0; i < this.names.length; ++i) {
         if (this.caseSensitivity.checkEquals(name, this.names[i])) {
            return true;
         }
      }

      return false;
   }

   public boolean accept(File file, String name) {
      for(int i = 0; i < this.names.length; ++i) {
         if (this.caseSensitivity.checkEquals(name, this.names[i])) {
            return true;
         }
      }

      return false;
   }
}

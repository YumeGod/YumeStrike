package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class DelegateFileFilter extends AbstractFileFilter {
   private FilenameFilter filenameFilter;
   private FileFilter fileFilter;

   public DelegateFileFilter(FilenameFilter filter) {
      if (filter == null) {
         throw new IllegalArgumentException("The FilenameFilter must not be null");
      } else {
         this.filenameFilter = filter;
      }
   }

   public DelegateFileFilter(FileFilter filter) {
      if (filter == null) {
         throw new IllegalArgumentException("The FileFilter must not be null");
      } else {
         this.fileFilter = filter;
      }
   }

   public boolean accept(File file) {
      return this.fileFilter != null ? this.fileFilter.accept(file) : super.accept(file);
   }

   public boolean accept(File dir, String name) {
      return this.filenameFilter != null ? this.filenameFilter.accept(dir, name) : super.accept(dir, name);
   }
}

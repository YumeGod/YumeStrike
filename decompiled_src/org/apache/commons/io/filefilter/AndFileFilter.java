package org.apache.commons.io.filefilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AndFileFilter extends AbstractFileFilter implements ConditionalFileFilter {
   private List fileFilters;

   public AndFileFilter() {
      this.fileFilters = new ArrayList();
   }

   public AndFileFilter(List fileFilters) {
      if (fileFilters == null) {
         this.fileFilters = new ArrayList();
      } else {
         this.fileFilters = new ArrayList(fileFilters);
      }

   }

   public AndFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
      if (filter1 != null && filter2 != null) {
         this.fileFilters = new ArrayList();
         this.addFileFilter(filter1);
         this.addFileFilter(filter2);
      } else {
         throw new IllegalArgumentException("The filters must not be null");
      }
   }

   public void addFileFilter(IOFileFilter ioFileFilter) {
      this.fileFilters.add(ioFileFilter);
   }

   public List getFileFilters() {
      return Collections.unmodifiableList(this.fileFilters);
   }

   public boolean removeFileFilter(IOFileFilter ioFileFilter) {
      return this.fileFilters.remove(ioFileFilter);
   }

   public void setFileFilters(List fileFilters) {
      this.fileFilters = new ArrayList(fileFilters);
   }

   public boolean accept(File file) {
      if (this.fileFilters.size() == 0) {
         return false;
      } else {
         Iterator iter = this.fileFilters.iterator();

         while(iter.hasNext()) {
            IOFileFilter fileFilter = (IOFileFilter)iter.next();
            if (!fileFilter.accept(file)) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean accept(File file, String name) {
      if (this.fileFilters.size() == 0) {
         return false;
      } else {
         Iterator iter = this.fileFilters.iterator();

         while(iter.hasNext()) {
            IOFileFilter fileFilter = (IOFileFilter)iter.next();
            if (!fileFilter.accept(file, name)) {
               return false;
            }
         }

         return true;
      }
   }
}

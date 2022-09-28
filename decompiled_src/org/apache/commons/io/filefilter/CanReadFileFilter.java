package org.apache.commons.io.filefilter;

import java.io.File;

public class CanReadFileFilter extends AbstractFileFilter {
   public static final IOFileFilter CAN_READ = new CanReadFileFilter();
   public static final IOFileFilter CANNOT_READ;
   public static final IOFileFilter READ_ONLY;

   protected CanReadFileFilter() {
   }

   public boolean accept(File file) {
      return file.canRead();
   }

   static {
      CANNOT_READ = new NotFileFilter(CAN_READ);
      READ_ONLY = new AndFileFilter(CAN_READ, CanWriteFileFilter.CANNOT_WRITE);
   }
}

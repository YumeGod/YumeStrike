package org.apache.commons.io.filefilter;

import java.io.File;

public class CanWriteFileFilter extends AbstractFileFilter {
   public static final IOFileFilter CAN_WRITE = new CanWriteFileFilter();
   public static final IOFileFilter CANNOT_WRITE;

   protected CanWriteFileFilter() {
   }

   public boolean accept(File file) {
      return file.canWrite();
   }

   static {
      CANNOT_WRITE = new NotFileFilter(CAN_WRITE);
   }
}

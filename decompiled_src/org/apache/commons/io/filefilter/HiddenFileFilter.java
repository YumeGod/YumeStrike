package org.apache.commons.io.filefilter;

import java.io.File;

public class HiddenFileFilter extends AbstractFileFilter {
   public static final IOFileFilter HIDDEN = new HiddenFileFilter();
   public static final IOFileFilter VISIBLE;

   protected HiddenFileFilter() {
   }

   public boolean accept(File file) {
      return file.isHidden();
   }

   static {
      VISIBLE = new NotFileFilter(HIDDEN);
   }
}

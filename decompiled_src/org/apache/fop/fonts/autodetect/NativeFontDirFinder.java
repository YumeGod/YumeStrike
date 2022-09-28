package org.apache.fop.fonts.autodetect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class NativeFontDirFinder implements FontFinder {
   public List find() {
      List fontDirList = new ArrayList();
      String[] searchableDirectories = this.getSearchableDirectories();
      if (searchableDirectories != null) {
         for(int i = 0; i < searchableDirectories.length; ++i) {
            File fontDir = new File(searchableDirectories[i]);
            if (fontDir.exists() && fontDir.canRead()) {
               fontDirList.add(fontDir);
            }
         }
      }

      return fontDirList;
   }

   protected abstract String[] getSearchableDirectories();
}

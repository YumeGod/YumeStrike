package org.apache.commons.io.filefilter;

import java.io.File;
import java.util.Date;
import org.apache.commons.io.FileUtils;

public class AgeFileFilter extends AbstractFileFilter {
   private long cutoff;
   private boolean acceptOlder;

   public AgeFileFilter(long cutoff) {
      this(cutoff, true);
   }

   public AgeFileFilter(long cutoff, boolean acceptOlder) {
      this.acceptOlder = acceptOlder;
      this.cutoff = cutoff;
   }

   public AgeFileFilter(Date cutoffDate) {
      this(cutoffDate, true);
   }

   public AgeFileFilter(Date cutoffDate, boolean acceptOlder) {
      this(cutoffDate.getTime(), acceptOlder);
   }

   public AgeFileFilter(File cutoffReference) {
      this(cutoffReference, true);
   }

   public AgeFileFilter(File cutoffReference, boolean acceptOlder) {
      this(cutoffReference.lastModified(), acceptOlder);
   }

   public boolean accept(File file) {
      boolean newer = FileUtils.isFileNewer(file, this.cutoff);
      return this.acceptOlder ? !newer : newer;
   }
}

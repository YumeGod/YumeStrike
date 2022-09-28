package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;

public abstract class PDFFilter {
   private boolean applied = false;

   public boolean isApplied() {
      return this.applied;
   }

   public void setApplied(boolean b) {
      this.applied = b;
   }

   public abstract String getName();

   public boolean isASCIIFilter() {
      return false;
   }

   public abstract PDFObject getDecodeParms();

   public abstract OutputStream applyFilter(OutputStream var1) throws IOException;
}

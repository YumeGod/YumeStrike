package org.apache.fop.util;

import java.io.FilterInputStream;
import java.io.InputStream;

public class UnclosableInputStream extends FilterInputStream {
   public UnclosableInputStream(InputStream in) {
      super(in);
   }

   public void close() {
   }
}

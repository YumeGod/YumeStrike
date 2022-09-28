package org.apache.fop.util;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.ProxyOutputStream;

public class CloseBlockerOutputStream extends ProxyOutputStream {
   public CloseBlockerOutputStream(OutputStream out) {
      super(out);
   }

   public void close() throws IOException {
      try {
         this.flush();
      } catch (IOException var2) {
      }

   }
}

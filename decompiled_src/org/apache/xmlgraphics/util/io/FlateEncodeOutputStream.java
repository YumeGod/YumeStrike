package org.apache.xmlgraphics.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

public class FlateEncodeOutputStream extends DeflaterOutputStream implements Finalizable {
   public FlateEncodeOutputStream(OutputStream out) {
      super(out);
   }

   public void finalizeStream() throws IOException {
      this.finish();
      this.flush();
      this.def.end();
      if (this.out instanceof Finalizable) {
         ((Finalizable)this.out).finalizeStream();
      }

   }
}

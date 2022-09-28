package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractStructuredObject extends AbstractAFPObject {
   protected AbstractStructuredObject() {
   }

   protected void writeStart(OutputStream os) throws IOException {
   }

   protected void writeEnd(OutputStream os) throws IOException {
   }

   protected void writeContent(OutputStream os) throws IOException {
   }

   public void writeToStream(OutputStream os) throws IOException {
      this.writeStart(os);
      this.writeContent(os);
      this.writeEnd(os);
   }
}

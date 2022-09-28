package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.Factory;

public final class Document extends AbstractResourceEnvironmentGroupContainer {
   public Document(Factory factory, String name) {
      super(factory, name);
   }

   public void endDocument() {
      this.complete = true;
   }

   public boolean isComplete() {
      return this.complete;
   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)-88);
      os.write(data);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-88);
      os.write(data);
   }

   public String toString() {
      return this.name;
   }
}

package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.Completable;

public class StreamedResourceGroup extends ResourceGroup implements Completable {
   private final OutputStream os;
   private boolean started = false;
   private boolean complete = false;

   public StreamedResourceGroup(String name, OutputStream os) {
      super(name);
      this.os = os;
   }

   public void addObject(AbstractNamedAFPObject namedObject) throws IOException {
      if (!this.started) {
         this.writeStart(this.os);
         this.started = true;
      }

      try {
         namedObject.writeToStream(this.os);
      } finally {
         this.os.flush();
      }

   }

   public void close() throws IOException {
      this.writeEnd(this.os);
      this.complete = true;
   }

   public OutputStream getOutputStream() {
      return this.os;
   }

   public void setComplete(boolean complete) {
      this.complete = complete;
   }

   public boolean isComplete() {
      return this.complete;
   }
}

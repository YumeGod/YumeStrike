package sleep.bridges.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BufferObject extends IOObject {
   protected ByteArrayOutputStream source;
   protected ByteArrayInputStream readme;

   public Object getSource() {
      return this.source;
   }

   public void close() {
      super.close();
      if (this.readme != null) {
         this.readme = null;
      }

      if (this.source != null) {
         this.readme = new ByteArrayInputStream(this.source.toByteArray());
         this.openRead(this.readme);
         this.source = null;
      }

   }

   public void allocate(int var1) {
      this.source = new ByteArrayOutputStream(var1);
      this.openWrite(this.source);
   }
}

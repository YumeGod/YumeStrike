package org.apache.james.mime4j.dom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class SingleBody implements Body {
   private Entity parent = null;
   static final int DEFAULT_ENCODING_BUFFER_SIZE = 1024;

   protected SingleBody() {
   }

   public Entity getParent() {
      return this.parent;
   }

   public void setParent(Entity parent) {
      this.parent = parent;
   }

   public abstract InputStream getInputStream() throws IOException;

   public void writeTo(OutputStream out) throws IOException {
      if (out == null) {
         throw new IllegalArgumentException();
      } else {
         InputStream in = this.getInputStream();
         copy(in, out);
         in.close();
      }
   }

   public SingleBody copy() {
      throw new UnsupportedOperationException();
   }

   public void dispose() {
   }

   private static void copy(InputStream in, OutputStream out) throws IOException {
      byte[] buffer = new byte[1024];

      int inputLength;
      while(-1 != (inputLength = in.read(buffer))) {
         out.write(buffer, 0, inputLength);
      }

   }
}

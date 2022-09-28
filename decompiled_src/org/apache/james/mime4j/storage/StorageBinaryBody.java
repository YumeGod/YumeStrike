package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.james.mime4j.codec.CodecUtil;
import org.apache.james.mime4j.dom.BinaryBody;

class StorageBinaryBody extends BinaryBody {
   private MultiReferenceStorage storage;

   public StorageBinaryBody(MultiReferenceStorage storage) {
      this.storage = storage;
   }

   public InputStream getInputStream() throws IOException {
      return this.storage.getInputStream();
   }

   public void writeTo(OutputStream out) throws IOException {
      if (out == null) {
         throw new IllegalArgumentException();
      } else {
         InputStream in = this.storage.getInputStream();
         CodecUtil.copy(in, out);
         in.close();
      }
   }

   public StorageBinaryBody copy() {
      this.storage.addReference();
      return new StorageBinaryBody(this.storage);
   }

   public void dispose() {
      if (this.storage != null) {
         this.storage.delete();
         this.storage = null;
      }

   }
}

package org.apache.james.mime4j.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.dom.BinaryBody;

class BasicBinaryBody extends BinaryBody {
   private final byte[] content;

   BasicBinaryBody(byte[] content) {
      this.content = content;
   }

   public InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream(this.content);
   }

   public BasicBinaryBody copy() {
      return new BasicBinaryBody(this.content);
   }
}

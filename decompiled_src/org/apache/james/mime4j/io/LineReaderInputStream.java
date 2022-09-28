package org.apache.james.mime4j.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public abstract class LineReaderInputStream extends FilterInputStream {
   protected LineReaderInputStream(InputStream in) {
      super(in);
   }

   public abstract int readLine(ByteArrayBuffer var1) throws MaxLineLimitException, IOException;

   public abstract boolean unread(ByteArrayBuffer var1);
}

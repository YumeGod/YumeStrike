package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class DemuxInputStream extends InputStream {
   private InheritableThreadLocal m_streams = new InheritableThreadLocal();

   public InputStream bindStream(InputStream input) {
      InputStream oldValue = this.getStream();
      this.m_streams.set(input);
      return oldValue;
   }

   public void close() throws IOException {
      InputStream input = this.getStream();
      if (null != input) {
         input.close();
      }

   }

   public int read() throws IOException {
      InputStream input = this.getStream();
      return null != input ? input.read() : -1;
   }

   private InputStream getStream() {
      return (InputStream)this.m_streams.get();
   }
}

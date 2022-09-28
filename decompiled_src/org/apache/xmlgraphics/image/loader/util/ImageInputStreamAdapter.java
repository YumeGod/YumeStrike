package org.apache.xmlgraphics.image.loader.util;

import java.io.IOException;
import java.io.InputStream;
import javax.imageio.stream.ImageInputStream;

public class ImageInputStreamAdapter extends InputStream {
   private ImageInputStream iin;
   private long lastMarkPosition;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public ImageInputStreamAdapter(ImageInputStream iin) {
      if (!$assertionsDisabled && iin == null) {
         throw new AssertionError("InputStream is null");
      } else {
         this.iin = iin;
      }
   }

   public int read(byte[] b, int off, int len) throws IOException {
      return this.iin.read(b, off, len);
   }

   public int read(byte[] b) throws IOException {
      return this.iin.read(b);
   }

   public int read() throws IOException {
      return this.iin.read();
   }

   public long skip(long n) throws IOException {
      return this.iin.skipBytes(n);
   }

   public void close() throws IOException {
      this.iin.close();
      this.iin = null;
   }

   public synchronized void mark(int readlimit) {
      try {
         this.lastMarkPosition = this.iin.getStreamPosition();
      } catch (IOException var3) {
         throw new RuntimeException("Unexpected IOException in ImageInputStream.getStreamPosition()", var3);
      }
   }

   public boolean markSupported() {
      return true;
   }

   public synchronized void reset() throws IOException {
      this.iin.seek(this.lastMarkPosition);
   }

   public int available() throws IOException {
      return 0;
   }

   static {
      $assertionsDisabled = !ImageInputStreamAdapter.class.desiredAssertionStatus();
   }
}

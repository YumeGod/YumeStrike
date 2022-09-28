package org.apache.xmlgraphics.image.loader.util;

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.apache.xmlgraphics.image.codec.util.SeekableStream;

public class SeekableStreamAdapter extends SeekableStream {
   private ImageInputStream iin;

   public SeekableStreamAdapter(ImageInputStream iin) {
      this.iin = iin;
   }

   public long getFilePointer() throws IOException {
      return this.iin.getStreamPosition();
   }

   public int read() throws IOException {
      return this.iin.read();
   }

   public int read(byte[] b, int off, int len) throws IOException {
      return this.iin.read(b, off, len);
   }

   public void seek(long pos) throws IOException {
      this.iin.seek(pos);
   }

   public boolean canSeekBackwards() {
      return true;
   }

   public int skipBytes(int n) throws IOException {
      return this.iin.skipBytes(n);
   }
}

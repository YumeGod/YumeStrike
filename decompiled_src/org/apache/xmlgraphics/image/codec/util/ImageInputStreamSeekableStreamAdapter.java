package org.apache.xmlgraphics.image.codec.util;

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;

public final class ImageInputStreamSeekableStreamAdapter extends SeekableStream {
   private ImageInputStream stream;

   public ImageInputStreamSeekableStreamAdapter(ImageInputStream stream) throws IOException {
      this.stream = stream;
   }

   public boolean canSeekBackwards() {
      return true;
   }

   public long getFilePointer() throws IOException {
      return this.stream.getStreamPosition();
   }

   public void seek(long pos) throws IOException {
      this.stream.seek(pos);
   }

   public int read() throws IOException {
      return this.stream.read();
   }

   public int read(byte[] b, int off, int len) throws IOException {
      return this.stream.read(b, off, len);
   }

   public void close() throws IOException {
      super.close();
      this.stream.close();
   }
}

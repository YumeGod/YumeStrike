package org.apache.xmlgraphics.image.codec.util;

import java.io.IOException;
import java.io.InputStream;

public class ForwardSeekableStream extends SeekableStream {
   private InputStream src;
   long pointer = 0L;
   long markPos = -1L;

   public ForwardSeekableStream(InputStream src) {
      this.src = src;
   }

   public final int read() throws IOException {
      int result = this.src.read();
      if (result != -1) {
         ++this.pointer;
      }

      return result;
   }

   public final int read(byte[] b, int off, int len) throws IOException {
      int result = this.src.read(b, off, len);
      if (result != -1) {
         this.pointer += (long)result;
      }

      return result;
   }

   public final long skip(long n) throws IOException {
      long skipped = this.src.skip(n);
      this.pointer += skipped;
      return skipped;
   }

   public final int available() throws IOException {
      return this.src.available();
   }

   public final void close() throws IOException {
      this.src.close();
   }

   public final synchronized void mark(int readLimit) {
      this.markPos = this.pointer;
      this.src.mark(readLimit);
   }

   public final synchronized void reset() throws IOException {
      if (this.markPos != -1L) {
         this.pointer = this.markPos;
      }

      this.src.reset();
   }

   public boolean markSupported() {
      return this.src.markSupported();
   }

   public final boolean canSeekBackwards() {
      return false;
   }

   public final long getFilePointer() {
      return this.pointer;
   }

   public final void seek(long pos) throws IOException {
      while(pos - this.pointer > 0L) {
         this.pointer += this.src.skip(pos - this.pointer);
      }

   }
}

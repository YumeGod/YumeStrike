package org.apache.batik.ext.awt.image.codec.util;

import java.io.IOException;
import java.io.InputStream;

public class ForwardSeekableStream extends SeekableStream {
   private InputStream src;
   long pointer = 0L;

   public ForwardSeekableStream(InputStream var1) {
      this.src = var1;
   }

   public final int read() throws IOException {
      int var1 = this.src.read();
      if (var1 != -1) {
         ++this.pointer;
      }

      return var1;
   }

   public final int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.src.read(var1, var2, var3);
      if (var4 != -1) {
         this.pointer += (long)var4;
      }

      return var4;
   }

   public final long skip(long var1) throws IOException {
      long var3 = this.src.skip(var1);
      this.pointer += var3;
      return var3;
   }

   public final int available() throws IOException {
      return this.src.available();
   }

   public final void close() throws IOException {
      this.src.close();
   }

   public final synchronized void mark(int var1) {
      this.markPos = this.pointer;
      this.src.mark(var1);
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

   public final void seek(long var1) throws IOException {
      while(var1 - this.pointer > 0L) {
         this.pointer += this.src.skip(var1 - this.pointer);
      }

   }
}

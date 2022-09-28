package com.xmlmind.fo.graphic.emf;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

final class EMFInputStream extends FilterInputStream {
   public EMFInputStream(InputStream var1) {
      super(var1);
   }

   public short readSHORT() throws IOException {
      int var1 = this.checkedRead();
      int var2 = this.checkedRead();
      return (short)((var2 & 255) << 8 | var1 & 255);
   }

   private int checkedRead() throws IOException {
      int var1 = this.read();
      if (var1 < 0) {
         throw new IOException("unexpected end of data");
      } else {
         return var1;
      }
   }

   public int readWORD() throws IOException {
      int var1 = this.checkedRead();
      int var2 = this.checkedRead();
      return (var2 & 255) << 8 | var1 & 255;
   }

   public int readINT() throws IOException {
      int var1 = this.checkedRead();
      int var2 = this.checkedRead();
      int var3 = this.checkedRead();
      int var4 = this.checkedRead();
      return (var4 & 255) << 24 | (var3 & 255) << 16 | (var2 & 255) << 8 | var1 & 255;
   }

   public long readDWORD() throws IOException {
      long var1 = (long)this.checkedRead();
      long var3 = (long)this.checkedRead();
      long var5 = (long)this.checkedRead();
      long var7 = (long)this.checkedRead();
      return (var7 & 255L) << 24 | (var5 & 255L) << 16 | (var3 & 255L) << 8 | var1 & 255L;
   }
}

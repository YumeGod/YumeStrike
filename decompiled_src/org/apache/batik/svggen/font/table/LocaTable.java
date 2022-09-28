package org.apache.batik.svggen.font.table;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LocaTable implements Table {
   private byte[] buf = null;
   private int[] offsets = null;
   private short factor = 0;

   protected LocaTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.buf = new byte[var1.getLength()];
      var2.read(this.buf);
   }

   public void init(int var1, boolean var2) {
      if (this.buf != null) {
         this.offsets = new int[var1 + 1];
         ByteArrayInputStream var3 = new ByteArrayInputStream(this.buf);
         int var4;
         if (var2) {
            this.factor = 2;

            for(var4 = 0; var4 <= var1; ++var4) {
               this.offsets[var4] = var3.read() << 8 | var3.read();
            }
         } else {
            this.factor = 1;

            for(var4 = 0; var4 <= var1; ++var4) {
               this.offsets[var4] = var3.read() << 24 | var3.read() << 16 | var3.read() << 8 | var3.read();
            }
         }

         this.buf = null;
      }
   }

   public int getOffset(int var1) {
      return this.offsets == null ? 0 : this.offsets[var1] * this.factor;
   }

   public int getType() {
      return 1819239265;
   }
}

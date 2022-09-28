package org.apache.batik.svggen.font.table;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HmtxTable implements Table {
   private byte[] buf = null;
   private int[] hMetrics = null;
   private short[] leftSideBearing = null;

   protected HmtxTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.buf = new byte[var1.getLength()];
      var2.read(this.buf);
   }

   public void init(int var1, int var2) {
      if (this.buf != null) {
         this.hMetrics = new int[var1];
         ByteArrayInputStream var3 = new ByteArrayInputStream(this.buf);

         int var4;
         for(var4 = 0; var4 < var1; ++var4) {
            this.hMetrics[var4] = var3.read() << 24 | var3.read() << 16 | var3.read() << 8 | var3.read();
         }

         if (var2 > 0) {
            this.leftSideBearing = new short[var2];

            for(var4 = 0; var4 < var2; ++var4) {
               this.leftSideBearing[var4] = (short)(var3.read() << 8 | var3.read());
            }
         }

         this.buf = null;
      }
   }

   public int getAdvanceWidth(int var1) {
      if (this.hMetrics == null) {
         return 0;
      } else {
         return var1 < this.hMetrics.length ? this.hMetrics[var1] >> 16 : this.hMetrics[this.hMetrics.length - 1] >> 16;
      }
   }

   public short getLeftSideBearing(int var1) {
      if (this.hMetrics == null) {
         return 0;
      } else {
         return var1 < this.hMetrics.length ? (short)(this.hMetrics[var1] & '\uffff') : this.leftSideBearing[var1 - this.hMetrics.length];
      }
   }

   public int getType() {
      return 1752003704;
   }
}

package org.apache.batik.svggen.font.table;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GlyfTable implements Table {
   private byte[] buf = null;
   private GlyfDescript[] descript;

   protected GlyfTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.buf = new byte[var1.getLength()];
      var2.read(this.buf);
   }

   public void init(int var1, LocaTable var2) {
      if (this.buf != null) {
         this.descript = new GlyfDescript[var1];
         ByteArrayInputStream var3 = new ByteArrayInputStream(this.buf);

         int var4;
         for(var4 = 0; var4 < var1; ++var4) {
            int var5 = var2.getOffset(var4 + 1) - var2.getOffset(var4);
            if (var5 > 0) {
               var3.reset();
               var3.skip((long)var2.getOffset(var4));
               short var6 = (short)(var3.read() << 8 | var3.read());
               if (var6 >= 0) {
                  this.descript[var4] = new GlyfSimpleDescript(this, var6, var3);
               } else {
                  this.descript[var4] = new GlyfCompositeDescript(this, var3);
               }
            }
         }

         this.buf = null;

         for(var4 = 0; var4 < var1; ++var4) {
            if (this.descript[var4] != null) {
               this.descript[var4].resolve();
            }
         }

      }
   }

   public GlyfDescript getDescription(int var1) {
      return this.descript[var1];
   }

   public int getType() {
      return 1735162214;
   }
}

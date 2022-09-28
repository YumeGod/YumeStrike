package pe;

import java.util.zip.Checksum;

class PEImageChecksum implements Checksum {
   private static final long MAX_UNSIGNED_INT = 4294967296L;
   private long checksum;
   private long position;
   private final long checksumOffset;
   private boolean checksumOffsetSkipped;

   public PEImageChecksum(long var1) {
      this.checksumOffset = var1;
   }

   public void update(int var1) {
      throw new UnsupportedOperationException("Checksum can only be updated with buffers");
   }

   public void update(byte[] var1, int var2, int var3) {
      long var4 = this.checksum;

      for(int var6 = var2; var6 < var2 + var3; var6 += 4) {
         if (!this.checksumOffsetSkipped && this.position + (long)var6 == this.checksumOffset) {
            this.checksumOffsetSkipped = true;
         } else {
            long var7 = (long)((var1[var6] & 255) + ((var1[var6 + 1] & 255) << 8) + ((var1[var6 + 2] & 255) << 16)) + (((long)var1[var6 + 3] & 255L) << 24);
            var4 += var7;
            if (var4 > 4294967296L) {
               var4 = (var4 & 4294967295L) + (var4 >> 32);
            }
         }
      }

      this.checksum = var4;
      this.position += (long)(var3 - var2);
   }

   public long getValue() {
      long var1 = this.checksum;
      var1 = (var1 >> 16) + (var1 & 65535L);
      var1 += var1 >> 16;
      return (var1 & 65535L) + this.position;
   }

   public void reset() {
      this.checksum = 0L;
      this.position = 0L;
      this.checksumOffsetSkipped = false;
   }
}

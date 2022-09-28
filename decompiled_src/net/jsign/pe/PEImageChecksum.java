package net.jsign.pe;

import java.util.zip.Checksum;

class PEImageChecksum implements Checksum {
   private static final long MAX_UNSIGNED_INT = 4294967296L;
   private long checksum;
   private long position;
   private final long checksumOffset;
   private boolean checksumOffsetSkipped;

   public PEImageChecksum(long checksumOffset) {
      this.checksumOffset = checksumOffset;
   }

   public void update(int b) {
      throw new UnsupportedOperationException("Checksum can only be updated with buffers");
   }

   public void update(byte[] buffer, int offset, int length) {
      long checksum = this.checksum;

      for(int i = offset; i < offset + length; i += 4) {
         if (!this.checksumOffsetSkipped && this.position + (long)i == this.checksumOffset) {
            this.checksumOffsetSkipped = true;
         } else {
            long dword = (long)((buffer[i] & 255) + ((buffer[i + 1] & 255) << 8) + ((buffer[i + 2] & 255) << 16)) + (((long)buffer[i + 3] & 255L) << 24);
            checksum += dword;
            if (checksum > 4294967296L) {
               checksum = (checksum & 4294967295L) + (checksum >> 32);
            }
         }
      }

      this.checksum = checksum;
      this.position += (long)(length - offset);
   }

   public long getValue() {
      long checksum = this.checksum;
      checksum = (checksum >> 16) + (checksum & 65535L);
      checksum += checksum >> 16;
      return (checksum & 65535L) + this.position;
   }

   public void reset() {
      this.checksum = 0L;
      this.position = 0L;
      this.checksumOffsetSkipped = false;
   }
}

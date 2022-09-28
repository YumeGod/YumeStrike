package net.jsign.pe;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DataDirectory {
   private PEFile peFile;
   private int index;

   DataDirectory(PEFile peFile, int index) {
      this.peFile = peFile;
      this.index = index;
   }

   public long getVirtualAddress() {
      return this.peFile.readDWord((long)this.peFile.getDataDirectoryOffset(), this.index * 8);
   }

   public int getSize() {
      return (int)this.peFile.readDWord((long)this.peFile.getDataDirectoryOffset(), this.index * 8 + 4);
   }

   public boolean exists() {
      return this.getVirtualAddress() != 0L && this.getSize() != 0;
   }

   public void write(long virtualAddress, int size) {
      ByteBuffer buffer = ByteBuffer.allocate(8);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      buffer.putInt((int)virtualAddress);
      buffer.putInt(size);
      this.peFile.write((long)(this.peFile.getDataDirectoryOffset() + this.index * 8), buffer.array());
   }
}

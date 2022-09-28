package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DirectoryEntry {
   private int tag;
   private int checksum;
   private int offset;
   private int length;
   private Table table = null;

   protected DirectoryEntry(RandomAccessFile var1) throws IOException {
      this.tag = var1.readInt();
      this.checksum = var1.readInt();
      this.offset = var1.readInt();
      this.length = var1.readInt();
   }

   public int getChecksum() {
      return this.checksum;
   }

   public int getLength() {
      return this.length;
   }

   public int getOffset() {
      return this.offset;
   }

   public int getTag() {
      return this.tag;
   }

   public String toString() {
      return "" + (char)(this.tag >> 24 & 255) + (char)(this.tag >> 16 & 255) + (char)(this.tag >> 8 & 255) + (char)(this.tag & 255) + ", offset: " + this.offset + ", length: " + this.length + ", checksum: 0x" + Integer.toHexString(this.checksum);
   }
}

package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class CmapFormat {
   protected int format;
   protected int length;
   protected int version;

   protected CmapFormat(RandomAccessFile var1) throws IOException {
      this.length = var1.readUnsignedShort();
      this.version = var1.readUnsignedShort();
   }

   protected static CmapFormat create(int var0, RandomAccessFile var1) throws IOException {
      switch (var0) {
         case 0:
            return new CmapFormat0(var1);
         case 1:
         case 3:
         case 5:
         default:
            return null;
         case 2:
            return new CmapFormat2(var1);
         case 4:
            return new CmapFormat4(var1);
         case 6:
            return new CmapFormat6(var1);
      }
   }

   public int getFormat() {
      return this.format;
   }

   public int getLength() {
      return this.length;
   }

   public int getVersion() {
      return this.version;
   }

   public abstract int mapCharCode(int var1);

   public abstract int getFirst();

   public abstract int getLast();

   public String toString() {
      return "format: " + this.format + ", length: " + this.length + ", version: " + this.version;
   }
}

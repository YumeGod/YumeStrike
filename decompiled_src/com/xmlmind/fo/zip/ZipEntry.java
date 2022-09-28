package com.xmlmind.fo.zip;

public class ZipEntry {
   public String name;
   public String[] paths;
   public byte[] data;
   public boolean stored;
   public int flags;
   public int compression;
   public int crc;
   public int compressedSize;
   public int uncompressedSize;
   public int offset;

   public ZipEntry(String var1) {
      this.name = var1;
   }

   public ZipEntry(String var1, String var2) {
      this(var1, var2, false);
   }

   public ZipEntry(String var1, String var2, boolean var3) {
      this.name = var1;
      this.paths = new String[]{var2};
      this.stored = var3;
   }

   public ZipEntry(String var1, String[] var2) {
      this(var1, var2, false);
   }

   public ZipEntry(String var1, String[] var2, boolean var3) {
      this.name = var1;
      this.paths = var2;
      this.stored = var3;
   }

   public ZipEntry(String var1, byte[] var2) {
      this(var1, var2, false);
   }

   public ZipEntry(String var1, byte[] var2, boolean var3) {
      this.name = var1;
      this.data = var2;
      this.stored = var3;
   }
}

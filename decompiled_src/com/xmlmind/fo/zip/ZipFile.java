package com.xmlmind.fo.zip;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

public class ZipFile {
   private static final int VERSION = 20;
   private Vector entries = new Vector();
   private Deflater deflater = new Deflater(-1, true);
   private OutputStream output;
   private int date;
   private int time;
   private int offset;
   private int size;

   public void add(String var1, String var2) {
      this.add(new ZipEntry(var1, var2));
   }

   public void add(String var1, String var2, boolean var3) {
      this.add(new ZipEntry(var1, var2, var3));
   }

   public void add(String var1, String[] var2) {
      this.add(new ZipEntry(var1, var2));
   }

   public void add(String var1, String[] var2, boolean var3) {
      this.add(new ZipEntry(var1, var2, var3));
   }

   public void add(String var1, byte[] var2) {
      this.add(new ZipEntry(var1, var2));
   }

   public void add(String var1, byte[] var2, boolean var3) {
      this.add(new ZipEntry(var1, var2, var3));
   }

   public void add(ZipEntry var1) {
      if (var1.name.endsWith("/")) {
         var1.stored = true;
      }

      this.entries.addElement(var1);
   }

   public void write(OutputStream var1) throws Exception {
      this.output = var1;
      Calendar var2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
      this.date = dosDate(var2);
      this.time = dosTime(var2);
      this.offset = 0;
      this.size = 0;
      int var3 = 0;

      int var4;
      ZipEntry var5;
      for(var4 = this.entries.size(); var3 < var4; ++var3) {
         var5 = (ZipEntry)this.entries.elementAt(var3);
         var5.flags = 8;
         var5.compression = var5.stored ? 0 : 8;
         var5.crc = 0;
         var5.compressedSize = 0;
         var5.uncompressedSize = 0;
         var5.offset = this.offset;
         if (var5.data != null && var5.stored) {
            var5.flags = 0;
            var5.crc = (int)crc(var5.data);
            var5.compressedSize = var5.data.length;
            var5.uncompressedSize = var5.data.length;
         } else if (var5.paths == null && var5.data == null) {
            var5.flags = 0;
         }

         this.writeLocalHeader(var5);
         if (var5.stored) {
            this.writeStored(var5);
         } else {
            this.writeDeflated(var5);
         }

         if (var5.flags != 0) {
            this.writeDataDescriptor(var5);
         }
      }

      var3 = 0;

      for(var4 = this.entries.size(); var3 < var4; ++var3) {
         var5 = (ZipEntry)this.entries.elementAt(var3);
         this.writeCentralHeader(var5);
      }

      this.writeEndOfDirectory();
   }

   private void writeLocalHeader(ZipEntry var1) throws Exception {
      this.writeDoubleWord(67324752);
      this.writeWord(20);
      this.writeWord(var1.flags);
      this.writeWord(var1.compression);
      this.writeWord(this.time);
      this.writeWord(this.date);
      this.writeDoubleWord(var1.crc);
      this.writeDoubleWord(var1.compressedSize);
      this.writeDoubleWord(var1.uncompressedSize);
      this.writeWord(var1.name.length());
      this.writeWord(0);
      this.writeString(var1.name);
      this.offset += 30 + var1.name.length();
   }

   private void writeStored(ZipEntry var1) throws Exception {
      if (var1.data != null) {
         this.output.write(var1.data);
         this.offset += var1.data.length;
      } else if (var1.paths != null) {
         byte[] var2 = new byte[1024];
         CRC32 var3 = new CRC32();
         int var4 = 0;

         for(int var5 = var1.paths.length; var4 < var5; ++var4) {
            String var6 = var1.paths[var4];
            BufferedInputStream var7 = new BufferedInputStream(openStream(var6));

            try {
               while(true) {
                  int var8 = var7.read(var2, 0, var2.length);
                  if (var8 < 0) {
                     break;
                  }

                  if (var8 > 0) {
                     var3.update(var2, 0, var8);
                     var1.uncompressedSize += var8;
                     this.output.write(var2, 0, var8);
                  }
               }
            } finally {
               var7.close();
            }
         }

         var1.crc = (int)var3.getValue();
         var1.compressedSize = var1.uncompressedSize;
         this.offset += var1.uncompressedSize;
      }

   }

   private void writeDeflated(ZipEntry var1) throws Exception {
      byte[] var3 = new byte[1024];
      this.deflater.reset();
      int var2;
      if (var1.data != null) {
         var1.crc = (int)crc(var1.data);
         var1.uncompressedSize = var1.data.length;
         this.deflater.setInput(var1.data);

         while((var2 = this.deflater.deflate(var3)) > 0) {
            var1.compressedSize += var2;
            this.output.write(var3, 0, var2);
         }
      } else if (var1.paths != null) {
         byte[] var4 = new byte[1024];
         CRC32 var5 = new CRC32();
         int var6 = 0;

         label117:
         for(int var7 = var1.paths.length; var6 < var7; ++var6) {
            String var8 = var1.paths[var6];
            BufferedInputStream var9 = new BufferedInputStream(openStream(var8));

            try {
               while(true) {
                  do {
                     var2 = var9.read(var4, 0, var4.length);
                     if (var2 < 0) {
                        continue label117;
                     }
                  } while(var2 <= 0);

                  var5.update(var4, 0, var2);
                  var1.uncompressedSize += var2;
                  this.deflater.setInput(var4, 0, var2);

                  while((var2 = this.deflater.deflate(var3)) > 0) {
                     var1.compressedSize += var2;
                     this.output.write(var3, 0, var2);
                  }
               }
            } finally {
               var9.close();
            }
         }

         var1.crc = (int)var5.getValue();
      }

      this.deflater.finish();

      while((var2 = this.deflater.deflate(var3)) > 0) {
         var1.compressedSize += var2;
         this.output.write(var3, 0, var2);
      }

      this.offset += var1.compressedSize;
   }

   private static InputStream openStream(String var0) throws IOException {
      try {
         URL var1 = new URL(var0);
         return var1.openStream();
      } catch (MalformedURLException var2) {
         return new FileInputStream(var0);
      }
   }

   private void writeDataDescriptor(ZipEntry var1) throws IOException {
      this.writeDoubleWord(134695760);
      this.writeDoubleWord(var1.crc);
      this.writeDoubleWord(var1.compressedSize);
      this.writeDoubleWord(var1.uncompressedSize);
      this.offset += 16;
   }

   private void writeCentralHeader(ZipEntry var1) throws IOException {
      this.writeDoubleWord(33639248);
      this.writeWord(20);
      this.writeWord(20);
      this.writeWord(var1.flags);
      this.writeWord(var1.compression);
      this.writeWord(this.time);
      this.writeWord(this.date);
      this.writeDoubleWord(var1.crc);
      this.writeDoubleWord(var1.compressedSize);
      this.writeDoubleWord(var1.uncompressedSize);
      this.writeWord(var1.name.length());
      this.writeWord(0);
      this.writeWord(0);
      this.writeWord(0);
      this.writeWord(0);
      this.writeDoubleWord(0);
      this.writeDoubleWord(var1.offset);
      this.writeString(var1.name);
      this.size += 46 + var1.name.length();
   }

   private void writeEndOfDirectory() throws IOException {
      this.writeDoubleWord(101010256);
      this.writeWord(0);
      this.writeWord(0);
      this.writeWord(this.entries.size());
      this.writeWord(this.entries.size());
      this.writeDoubleWord(this.size);
      this.writeDoubleWord(this.offset);
      this.writeWord(0);
   }

   private void writeWord(int var1) throws IOException {
      this.output.write(var1);
      this.output.write(var1 >>> 8);
   }

   private void writeDoubleWord(int var1) throws IOException {
      this.output.write(var1);
      this.output.write(var1 >>> 8);
      this.output.write(var1 >>> 16);
      this.output.write(var1 >>> 24);
   }

   private void writeString(String var1) throws IOException {
      this.output.write(var1.getBytes("ASCII"));
   }

   private static int dosDate(Calendar var0) {
      int var1 = var0.get(1) - 1980;
      int var2 = var0.get(2) - 0 + 1;
      int var3 = var0.get(5);
      return var1 << 9 | var2 << 5 | var3;
   }

   private static int dosTime(Calendar var0) {
      int var1 = var0.get(11);
      int var2 = var0.get(12);
      int var3 = var0.get(13);
      return var1 << 11 | var2 << 5 | var3;
   }

   private static long crc(byte[] var0) {
      CRC32 var1 = new CRC32();
      var1.update(var0);
      return var1.getValue();
   }
}

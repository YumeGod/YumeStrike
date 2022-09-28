package com.xmlmind.fo.graphic.emf;

import java.io.IOException;
import java.io.InputStream;

public final class WMFHeader {
   public long key;
   public int handle;
   public short left;
   public short top;
   public short right;
   public short bottom;
   public int inch;
   public long reserved;
   public int checksum;

   private WMFHeader() {
   }

   public static WMFHeader read(InputStream var0) throws IOException {
      EMFInputStream var1 = new EMFInputStream(var0);
      WMFHeader var2 = new WMFHeader();
      var2.key = var1.readDWORD();
      var2.handle = var1.readWORD();
      var2.left = var1.readSHORT();
      var2.top = var1.readSHORT();
      var2.right = var1.readSHORT();
      var2.bottom = var1.readSHORT();
      var2.inch = var1.readWORD();
      var2.reserved = var1.readDWORD();
      var2.checksum = var1.readWORD();
      if (var2.key != 2596720087L) {
         throw new IOException("invalid 'key' field: 0x" + Long.toString(var2.key, 16));
      } else if (var2.handle != 0) {
         throw new IOException("invalid 'handle' field: 0x" + Integer.toString(var2.handle, 16));
      } else if (var2.left < var2.right && var2.top < var2.bottom) {
         if (var2.inch <= 0) {
            throw new IOException("invalid 'inch' field: " + var2.inch);
         } else if (var2.reserved != 0L) {
            throw new IOException("invalid 'reserved' field: 0x" + Long.toString(var2.reserved, 16));
         } else {
            int var3 = var2.checksum();
            if (var2.checksum != var3) {
               throw new IOException("invalid 'checksum' field: " + var2.checksum + "; expected " + var3);
            } else {
               return var2;
            }
         }
      } else {
         throw new IOException("invalid bounding box fields: " + var2.left + " " + var2.top + " " + var2.right + " " + var2.bottom);
      }
   }

   private int checksum() {
      long var1 = 0L;
      var1 ^= this.key & 65535L;
      var1 ^= (this.key & 4294901760L) >> 16;
      var1 ^= (long)(this.handle & '\uffff');
      var1 ^= (long)this.left;
      var1 ^= (long)this.top;
      var1 ^= (long)this.right;
      var1 ^= (long)this.bottom;
      var1 ^= (long)(this.inch & '\uffff');
      var1 ^= this.reserved & 65535L;
      var1 ^= (this.reserved & 4294901760L) >> 16;
      return (int)var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("WMFHeader[left=");
      var1.append(this.left);
      var1.append(", top=");
      var1.append(this.top);
      var1.append(", right=");
      var1.append(this.right);
      var1.append(", bottom=");
      var1.append(this.bottom);
      var1.append(", inch=");
      var1.append(this.inch);
      var1.append(']');
      return var1.toString();
   }
}

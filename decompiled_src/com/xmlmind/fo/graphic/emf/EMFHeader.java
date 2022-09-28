package com.xmlmind.fo.graphic.emf;

import java.io.IOException;
import java.io.InputStream;

public final class EMFHeader {
   public long type;
   public long size;
   public int boundsLeft;
   public int boundsTop;
   public int boundsRight;
   public int boundsBottom;
   public int frameLeft;
   public int frameTop;
   public int frameRight;
   public int frameBottom;
   public long recordSignature;
   public long version;

   private EMFHeader() {
   }

   public static EMFHeader read(InputStream var0) throws IOException {
      EMFInputStream var1 = new EMFInputStream(var0);
      EMFHeader var2 = new EMFHeader();
      var2.type = var1.readDWORD();
      var2.size = var1.readDWORD();
      var2.boundsLeft = var1.readINT();
      var2.boundsTop = var1.readINT();
      var2.boundsRight = var1.readINT();
      var2.boundsBottom = var1.readINT();
      var2.frameLeft = var1.readINT();
      var2.frameTop = var1.readINT();
      var2.frameRight = var1.readINT();
      var2.frameBottom = var1.readINT();
      var2.recordSignature = var1.readDWORD();
      var2.version = var1.readDWORD();
      if (var2.type != 1L) {
         throw new IOException("invalid 'type' field: 0x" + Long.toString(var2.type, 16));
      } else if (var2.size != 88L && var2.size != 100L && var2.size != 108L) {
         throw new IOException("invalid 'size' field: " + var2.size);
      } else if (var2.boundsLeft < var2.boundsRight && var2.boundsTop < var2.boundsBottom) {
         if (var2.frameLeft < var2.frameRight && var2.frameTop < var2.frameBottom) {
            if (var2.recordSignature != 1179469088L) {
               throw new IOException("invalid 'recordSignature' field: 0x" + Long.toString(var2.recordSignature, 16));
            } else if (var2.version != 65536L) {
               throw new IOException("invalid 'version' field: 0x" + Long.toString(var2.version, 16));
            } else {
               return var2;
            }
         } else {
            throw new IOException("invalid 'frame' fields: " + var2.frameLeft + " " + var2.frameTop + " " + var2.frameRight + " " + var2.frameBottom);
         }
      } else {
         throw new IOException("invalid 'bounds' fields: " + var2.boundsLeft + " " + var2.boundsTop + " " + var2.boundsRight + " " + var2.boundsBottom);
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("EMFHeader[bounds=");
      var1.append(this.boundsLeft);
      var1.append(' ');
      var1.append(this.boundsTop);
      var1.append(' ');
      var1.append(this.boundsRight);
      var1.append(' ');
      var1.append(this.boundsBottom);
      var1.append(", frame=");
      var1.append(this.frameLeft);
      var1.append(' ');
      var1.append(this.frameTop);
      var1.append(' ');
      var1.append(this.frameRight);
      var1.append(' ');
      var1.append(this.frameBottom);
      var1.append(']');
      return var1.toString();
   }
}

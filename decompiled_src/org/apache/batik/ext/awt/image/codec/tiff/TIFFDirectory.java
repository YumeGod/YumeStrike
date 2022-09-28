package org.apache.batik.ext.awt.image.codec.tiff;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.ext.awt.image.codec.util.SeekableStream;

public class TIFFDirectory implements Serializable {
   boolean isBigEndian;
   int numEntries;
   TIFFField[] fields;
   Map fieldIndex = new HashMap();
   long IFDOffset = 8L;
   long nextIFDOffset = 0L;
   private static final int[] sizeOfType = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};

   TIFFDirectory() {
   }

   private static boolean isValidEndianTag(int var0) {
      return var0 == 18761 || var0 == 19789;
   }

   public TIFFDirectory(SeekableStream var1, int var2) throws IOException {
      long var3 = var1.getFilePointer();
      var1.seek(0L);
      int var7 = var1.readUnsignedShort();
      if (!isValidEndianTag(var7)) {
         throw new IllegalArgumentException("TIFFDirectory1");
      } else {
         this.isBigEndian = var7 == 19789;
         int var8 = this.readUnsignedShort(var1);
         if (var8 != 42) {
            throw new IllegalArgumentException("TIFFDirectory2");
         } else {
            long var5 = this.readUnsignedInt(var1);

            for(int var9 = 0; var9 < var2; ++var9) {
               if (var5 == 0L) {
                  throw new IllegalArgumentException("TIFFDirectory3");
               }

               var1.seek(var5);
               long var10 = (long)this.readUnsignedShort(var1);
               var1.skip(12L * var10);
               var5 = this.readUnsignedInt(var1);
            }

            var1.seek(var5);
            this.initialize(var1);
            var1.seek(var3);
         }
      }
   }

   public TIFFDirectory(SeekableStream var1, long var2, int var4) throws IOException {
      long var5 = var1.getFilePointer();
      var1.seek(0L);
      int var7 = var1.readUnsignedShort();
      if (!isValidEndianTag(var7)) {
         throw new IllegalArgumentException("TIFFDirectory1");
      } else {
         this.isBigEndian = var7 == 19789;
         var1.seek(var2);

         for(int var8 = 0; var8 < var4; ++var8) {
            long var9 = (long)this.readUnsignedShort(var1);
            var1.seek(var2 + 12L * var9);
            var2 = this.readUnsignedInt(var1);
            var1.seek(var2);
         }

         this.initialize(var1);
         var1.seek(var5);
      }
   }

   private void initialize(SeekableStream var1) throws IOException {
      this.IFDOffset = var1.getFilePointer();
      this.numEntries = this.readUnsignedShort(var1);
      this.fields = new TIFFField[this.numEntries];

      for(int var4 = 0; var4 < this.numEntries; ++var4) {
         int var6 = this.readUnsignedShort(var1);
         int var7 = this.readUnsignedShort(var1);
         int var8 = (int)this.readUnsignedInt(var1);
         boolean var9 = false;
         long var2 = var1.getFilePointer() + 4L;

         try {
            if (var8 * sizeOfType[var7] > 4) {
               int var21 = (int)this.readUnsignedInt(var1);
               var1.seek((long)var21);
            }
         } catch (ArrayIndexOutOfBoundsException var20) {
            System.err.println(var6 + " " + "TIFFDirectory4");
            var1.seek(var2);
            continue;
         }

         this.fieldIndex.put(new Integer(var6), new Integer(var4));
         Object var10 = null;
         int var5;
         switch (var7) {
            case 1:
            case 2:
            case 6:
            case 7:
               byte[] var11 = new byte[var8];
               var1.readFully(var11, 0, var8);
               if (var7 != 2) {
                  var10 = var11;
                  break;
               }

               int var22 = 0;
               int var23 = 0;

               ArrayList var24;
               for(var24 = new ArrayList(); var22 < var8; var23 = var22) {
                  while(var22 < var8 && var11[var22++] != 0) {
                  }

                  var24.add(new String(var11, var23, var22 - var23));
               }

               var8 = var24.size();
               String[] var25 = new String[var8];
               var24.toArray(var25);
               var10 = var25;
               break;
            case 3:
               char[] var12 = new char[var8];

               for(var5 = 0; var5 < var8; ++var5) {
                  var12[var5] = (char)this.readUnsignedShort(var1);
               }

               var10 = var12;
               break;
            case 4:
               long[] var13 = new long[var8];

               for(var5 = 0; var5 < var8; ++var5) {
                  var13[var5] = this.readUnsignedInt(var1);
               }

               var10 = var13;
               break;
            case 5:
               long[][] var14 = new long[var8][2];

               for(var5 = 0; var5 < var8; ++var5) {
                  var14[var5][0] = this.readUnsignedInt(var1);
                  var14[var5][1] = this.readUnsignedInt(var1);
               }

               var10 = var14;
               break;
            case 8:
               short[] var15 = new short[var8];

               for(var5 = 0; var5 < var8; ++var5) {
                  var15[var5] = this.readShort(var1);
               }

               var10 = var15;
               break;
            case 9:
               int[] var16 = new int[var8];

               for(var5 = 0; var5 < var8; ++var5) {
                  var16[var5] = this.readInt(var1);
               }

               var10 = var16;
               break;
            case 10:
               int[][] var17 = new int[var8][2];

               for(var5 = 0; var5 < var8; ++var5) {
                  var17[var5][0] = this.readInt(var1);
                  var17[var5][1] = this.readInt(var1);
               }

               var10 = var17;
               break;
            case 11:
               float[] var18 = new float[var8];

               for(var5 = 0; var5 < var8; ++var5) {
                  var18[var5] = this.readFloat(var1);
               }

               var10 = var18;
               break;
            case 12:
               double[] var19 = new double[var8];

               for(var5 = 0; var5 < var8; ++var5) {
                  var19[var5] = this.readDouble(var1);
               }

               var10 = var19;
               break;
            default:
               System.err.println("TIFFDirectory0");
         }

         this.fields[var4] = new TIFFField(var6, var7, var8, var10);
         var1.seek(var2);
      }

      this.nextIFDOffset = this.readUnsignedInt(var1);
   }

   public int getNumEntries() {
      return this.numEntries;
   }

   public TIFFField getField(int var1) {
      Integer var2 = (Integer)this.fieldIndex.get(new Integer(var1));
      return var2 == null ? null : this.fields[var2];
   }

   public boolean isTagPresent(int var1) {
      return this.fieldIndex.containsKey(new Integer(var1));
   }

   public int[] getTags() {
      int[] var1 = new int[this.fieldIndex.size()];
      Iterator var2 = this.fieldIndex.keySet().iterator();

      for(int var3 = 0; var2.hasNext(); var1[var3++] = (Integer)var2.next()) {
      }

      return var1;
   }

   public TIFFField[] getFields() {
      return this.fields;
   }

   public byte getFieldAsByte(int var1, int var2) {
      Integer var3 = (Integer)this.fieldIndex.get(new Integer(var1));
      byte[] var4 = this.fields[var3].getAsBytes();
      return var4[var2];
   }

   public byte getFieldAsByte(int var1) {
      return this.getFieldAsByte(var1, 0);
   }

   public long getFieldAsLong(int var1, int var2) {
      Integer var3 = (Integer)this.fieldIndex.get(new Integer(var1));
      return this.fields[var3].getAsLong(var2);
   }

   public long getFieldAsLong(int var1) {
      return this.getFieldAsLong(var1, 0);
   }

   public float getFieldAsFloat(int var1, int var2) {
      Integer var3 = (Integer)this.fieldIndex.get(new Integer(var1));
      return this.fields[var3].getAsFloat(var2);
   }

   public float getFieldAsFloat(int var1) {
      return this.getFieldAsFloat(var1, 0);
   }

   public double getFieldAsDouble(int var1, int var2) {
      Integer var3 = (Integer)this.fieldIndex.get(new Integer(var1));
      return this.fields[var3].getAsDouble(var2);
   }

   public double getFieldAsDouble(int var1) {
      return this.getFieldAsDouble(var1, 0);
   }

   private short readShort(SeekableStream var1) throws IOException {
      return this.isBigEndian ? var1.readShort() : var1.readShortLE();
   }

   private int readUnsignedShort(SeekableStream var1) throws IOException {
      return this.isBigEndian ? var1.readUnsignedShort() : var1.readUnsignedShortLE();
   }

   private int readInt(SeekableStream var1) throws IOException {
      return this.isBigEndian ? var1.readInt() : var1.readIntLE();
   }

   private long readUnsignedInt(SeekableStream var1) throws IOException {
      return this.isBigEndian ? var1.readUnsignedInt() : var1.readUnsignedIntLE();
   }

   private long readLong(SeekableStream var1) throws IOException {
      return this.isBigEndian ? var1.readLong() : var1.readLongLE();
   }

   private float readFloat(SeekableStream var1) throws IOException {
      return this.isBigEndian ? var1.readFloat() : var1.readFloatLE();
   }

   private double readDouble(SeekableStream var1) throws IOException {
      return this.isBigEndian ? var1.readDouble() : var1.readDoubleLE();
   }

   private static int readUnsignedShort(SeekableStream var0, boolean var1) throws IOException {
      return var1 ? var0.readUnsignedShort() : var0.readUnsignedShortLE();
   }

   private static long readUnsignedInt(SeekableStream var0, boolean var1) throws IOException {
      return var1 ? var0.readUnsignedInt() : var0.readUnsignedIntLE();
   }

   public static int getNumDirectories(SeekableStream var0) throws IOException {
      long var1 = var0.getFilePointer();
      var0.seek(0L);
      int var3 = var0.readUnsignedShort();
      if (!isValidEndianTag(var3)) {
         throw new IllegalArgumentException("TIFFDirectory1");
      } else {
         boolean var4 = var3 == 19789;
         int var5 = readUnsignedShort(var0, var4);
         if (var5 != 42) {
            throw new IllegalArgumentException("TIFFDirectory2");
         } else {
            var0.seek(4L);
            long var6 = readUnsignedInt(var0, var4);

            int var8;
            for(var8 = 0; var6 != 0L; var6 = readUnsignedInt(var0, var4)) {
               ++var8;
               var0.seek(var6);
               long var9 = (long)readUnsignedShort(var0, var4);
               var0.skip(12L * var9);
            }

            var0.seek(var1);
            return var8;
         }
      }
   }

   public boolean isBigEndian() {
      return this.isBigEndian;
   }

   public long getIFDOffset() {
      return this.IFDOffset;
   }

   public long getNextIFDOffset() {
      return this.nextIFDOffset;
   }
}

package org.apache.batik.ext.awt.image.codec.tiff;

import java.io.Serializable;

public class TIFFField implements Comparable, Serializable {
   public static final int TIFF_BYTE = 1;
   public static final int TIFF_ASCII = 2;
   public static final int TIFF_SHORT = 3;
   public static final int TIFF_LONG = 4;
   public static final int TIFF_RATIONAL = 5;
   public static final int TIFF_SBYTE = 6;
   public static final int TIFF_UNDEFINED = 7;
   public static final int TIFF_SSHORT = 8;
   public static final int TIFF_SLONG = 9;
   public static final int TIFF_SRATIONAL = 10;
   public static final int TIFF_FLOAT = 11;
   public static final int TIFF_DOUBLE = 12;
   int tag;
   int type;
   int count;
   Object data;

   TIFFField() {
   }

   public TIFFField(int var1, int var2, int var3, Object var4) {
      this.tag = var1;
      this.type = var2;
      this.count = var3;
      this.data = var4;
   }

   public int getTag() {
      return this.tag;
   }

   public int getType() {
      return this.type;
   }

   public int getCount() {
      return this.count;
   }

   public byte[] getAsBytes() {
      return (byte[])this.data;
   }

   public char[] getAsChars() {
      return (char[])this.data;
   }

   public short[] getAsShorts() {
      return (short[])this.data;
   }

   public int[] getAsInts() {
      return (int[])this.data;
   }

   public long[] getAsLongs() {
      return (long[])this.data;
   }

   public float[] getAsFloats() {
      return (float[])this.data;
   }

   public double[] getAsDoubles() {
      return (double[])this.data;
   }

   public int[][] getAsSRationals() {
      return (int[][])this.data;
   }

   public long[][] getAsRationals() {
      return (long[][])this.data;
   }

   public int getAsInt(int var1) {
      switch (this.type) {
         case 1:
         case 7:
            return ((byte[])this.data)[var1] & 255;
         case 2:
         case 4:
         case 5:
         default:
            throw new ClassCastException();
         case 3:
            return ((char[])this.data)[var1] & '\uffff';
         case 6:
            return ((byte[])this.data)[var1];
         case 8:
            return ((short[])this.data)[var1];
         case 9:
            return ((int[])this.data)[var1];
      }
   }

   public long getAsLong(int var1) {
      switch (this.type) {
         case 1:
         case 7:
            return (long)(((byte[])this.data)[var1] & 255);
         case 2:
         case 5:
         default:
            throw new ClassCastException();
         case 3:
            return (long)(((char[])this.data)[var1] & '\uffff');
         case 4:
            return ((long[])this.data)[var1];
         case 6:
            return (long)((byte[])this.data)[var1];
         case 8:
            return (long)((short[])this.data)[var1];
         case 9:
            return (long)((int[])this.data)[var1];
      }
   }

   public float getAsFloat(int var1) {
      switch (this.type) {
         case 1:
            return (float)(((byte[])this.data)[var1] & 255);
         case 2:
         case 7:
         default:
            throw new ClassCastException();
         case 3:
            return (float)(((char[])this.data)[var1] & '\uffff');
         case 4:
            return (float)((long[])this.data)[var1];
         case 5:
            long[] var3 = this.getAsRational(var1);
            return (float)((double)var3[0] / (double)var3[1]);
         case 6:
            return (float)((byte[])this.data)[var1];
         case 8:
            return (float)((short[])this.data)[var1];
         case 9:
            return (float)((int[])this.data)[var1];
         case 10:
            int[] var2 = this.getAsSRational(var1);
            return (float)((double)var2[0] / (double)var2[1]);
         case 11:
            return ((float[])this.data)[var1];
         case 12:
            return (float)((double[])this.data)[var1];
      }
   }

   public double getAsDouble(int var1) {
      switch (this.type) {
         case 1:
            return (double)(((byte[])this.data)[var1] & 255);
         case 2:
         case 7:
         default:
            throw new ClassCastException();
         case 3:
            return (double)(((char[])this.data)[var1] & '\uffff');
         case 4:
            return (double)((long[])this.data)[var1];
         case 5:
            long[] var3 = this.getAsRational(var1);
            return (double)var3[0] / (double)var3[1];
         case 6:
            return (double)((byte[])this.data)[var1];
         case 8:
            return (double)((short[])this.data)[var1];
         case 9:
            return (double)((int[])this.data)[var1];
         case 10:
            int[] var2 = this.getAsSRational(var1);
            return (double)var2[0] / (double)var2[1];
         case 11:
            return (double)((float[])this.data)[var1];
         case 12:
            return ((double[])this.data)[var1];
      }
   }

   public String getAsString(int var1) {
      return ((String[])this.data)[var1];
   }

   public int[] getAsSRational(int var1) {
      return ((int[][])this.data)[var1];
   }

   public long[] getAsRational(int var1) {
      return ((long[][])this.data)[var1];
   }

   public int compareTo(Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         int var2 = ((TIFFField)var1).getTag();
         if (this.tag < var2) {
            return -1;
         } else {
            return this.tag > var2 ? 1 : 0;
         }
      }
   }
}

package org.apache.xmlgraphics.image.codec.tiff;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.xmlgraphics.image.codec.util.SeekableStream;

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

   private static boolean isValidEndianTag(int endian) {
      return endian == 18761 || endian == 19789;
   }

   public TIFFDirectory(SeekableStream stream, int directory) throws IOException {
      long global_save_offset = stream.getFilePointer();
      stream.seek(0L);
      int endian = stream.readUnsignedShort();
      if (!isValidEndianTag(endian)) {
         throw new IllegalArgumentException("TIFFDirectory1");
      } else {
         this.isBigEndian = endian == 19789;
         int magic = this.readUnsignedShort(stream);
         if (magic != 42) {
            throw new IllegalArgumentException("TIFFDirectory2");
         } else {
            long ifd_offset = this.readUnsignedInt(stream);

            for(int i = 0; i < directory; ++i) {
               if (ifd_offset == 0L) {
                  throw new IllegalArgumentException("TIFFDirectory3");
               }

               stream.seek(ifd_offset);
               long entries = (long)this.readUnsignedShort(stream);
               stream.skip(12L * entries);
               ifd_offset = this.readUnsignedInt(stream);
            }

            if (ifd_offset == 0L) {
               throw new IllegalArgumentException("TIFFDirectory3");
            } else {
               stream.seek(ifd_offset);
               this.initialize(stream);
               stream.seek(global_save_offset);
            }
         }
      }
   }

   public TIFFDirectory(SeekableStream stream, long ifd_offset, int directory) throws IOException {
      long global_save_offset = stream.getFilePointer();
      stream.seek(0L);
      int endian = stream.readUnsignedShort();
      if (!isValidEndianTag(endian)) {
         throw new IllegalArgumentException("TIFFDirectory1");
      } else {
         this.isBigEndian = endian == 19789;
         stream.seek(ifd_offset);

         for(int dirNum = 0; dirNum < directory; ++dirNum) {
            long numEntries = (long)this.readUnsignedShort(stream);
            stream.seek(ifd_offset + 12L * numEntries);
            ifd_offset = this.readUnsignedInt(stream);
            stream.seek(ifd_offset);
         }

         this.initialize(stream);
         stream.seek(global_save_offset);
      }
   }

   private void initialize(SeekableStream stream) throws IOException {
      this.IFDOffset = stream.getFilePointer();
      this.numEntries = this.readUnsignedShort(stream);
      this.fields = new TIFFField[this.numEntries];

      for(int i = 0; i < this.numEntries; ++i) {
         int tag = this.readUnsignedShort(stream);
         int type = this.readUnsignedShort(stream);
         int count = (int)this.readUnsignedInt(stream);
         int value = false;
         long nextTagOffset = stream.getFilePointer() + 4L;

         try {
            if (count * sizeOfType[type] > 4) {
               int value = (int)this.readUnsignedInt(stream);
               stream.seek((long)value);
            }
         } catch (ArrayIndexOutOfBoundsException var20) {
            System.err.println(tag + " " + "TIFFDirectory4");
            stream.seek(nextTagOffset);
            continue;
         }

         this.fieldIndex.put(new Integer(tag), new Integer(i));
         Object obj = null;
         int j;
         switch (type) {
            case 1:
            case 2:
            case 6:
            case 7:
               byte[] bvalues = new byte[count];
               stream.readFully(bvalues, 0, count);
               if (type != 2) {
                  obj = bvalues;
                  break;
               }

               int index = 0;
               int prevIndex = 0;

               ArrayList v;
               for(v = new ArrayList(); index < count; prevIndex = index) {
                  while(index < count && bvalues[index++] != 0) {
                  }

                  v.add(new String(bvalues, prevIndex, index - prevIndex));
               }

               count = v.size();
               String[] strings = new String[count];
               v.toArray(strings);
               obj = strings;
               break;
            case 3:
               char[] cvalues = new char[count];

               for(j = 0; j < count; ++j) {
                  cvalues[j] = (char)this.readUnsignedShort(stream);
               }

               obj = cvalues;
               break;
            case 4:
               long[] lvalues = new long[count];

               for(j = 0; j < count; ++j) {
                  lvalues[j] = this.readUnsignedInt(stream);
               }

               obj = lvalues;
               break;
            case 5:
               long[][] llvalues = new long[count][2];

               for(j = 0; j < count; ++j) {
                  llvalues[j][0] = this.readUnsignedInt(stream);
                  llvalues[j][1] = this.readUnsignedInt(stream);
               }

               obj = llvalues;
               break;
            case 8:
               short[] svalues = new short[count];

               for(j = 0; j < count; ++j) {
                  svalues[j] = this.readShort(stream);
               }

               obj = svalues;
               break;
            case 9:
               int[] ivalues = new int[count];

               for(j = 0; j < count; ++j) {
                  ivalues[j] = this.readInt(stream);
               }

               obj = ivalues;
               break;
            case 10:
               int[][] iivalues = new int[count][2];

               for(j = 0; j < count; ++j) {
                  iivalues[j][0] = this.readInt(stream);
                  iivalues[j][1] = this.readInt(stream);
               }

               obj = iivalues;
               break;
            case 11:
               float[] fvalues = new float[count];

               for(j = 0; j < count; ++j) {
                  fvalues[j] = this.readFloat(stream);
               }

               obj = fvalues;
               break;
            case 12:
               double[] dvalues = new double[count];

               for(j = 0; j < count; ++j) {
                  dvalues[j] = this.readDouble(stream);
               }

               obj = dvalues;
               break;
            default:
               System.err.println("TIFFDirectory0");
         }

         this.fields[i] = new TIFFField(tag, type, count, obj);
         stream.seek(nextTagOffset);
      }

      this.nextIFDOffset = this.readUnsignedInt(stream);
   }

   public int getNumEntries() {
      return this.numEntries;
   }

   public TIFFField getField(int tag) {
      Integer i = (Integer)this.fieldIndex.get(new Integer(tag));
      return i == null ? null : this.fields[i];
   }

   public boolean isTagPresent(int tag) {
      return this.fieldIndex.containsKey(new Integer(tag));
   }

   public int[] getTags() {
      int[] tags = new int[this.fieldIndex.size()];
      Iterator iter = this.fieldIndex.keySet().iterator();

      for(int i = 0; iter.hasNext(); tags[i++] = (Integer)iter.next()) {
      }

      return tags;
   }

   public TIFFField[] getFields() {
      return this.fields;
   }

   public byte getFieldAsByte(int tag, int index) {
      Integer i = (Integer)this.fieldIndex.get(new Integer(tag));
      byte[] b = this.fields[i].getAsBytes();
      return b[index];
   }

   public byte getFieldAsByte(int tag) {
      return this.getFieldAsByte(tag, 0);
   }

   public long getFieldAsLong(int tag, int index) {
      Integer i = (Integer)this.fieldIndex.get(new Integer(tag));
      return this.fields[i].getAsLong(index);
   }

   public long getFieldAsLong(int tag) {
      return this.getFieldAsLong(tag, 0);
   }

   public float getFieldAsFloat(int tag, int index) {
      Integer i = (Integer)this.fieldIndex.get(new Integer(tag));
      return this.fields[i].getAsFloat(index);
   }

   public float getFieldAsFloat(int tag) {
      return this.getFieldAsFloat(tag, 0);
   }

   public double getFieldAsDouble(int tag, int index) {
      Integer i = (Integer)this.fieldIndex.get(new Integer(tag));
      return this.fields[i].getAsDouble(index);
   }

   public double getFieldAsDouble(int tag) {
      return this.getFieldAsDouble(tag, 0);
   }

   private short readShort(SeekableStream stream) throws IOException {
      return this.isBigEndian ? stream.readShort() : stream.readShortLE();
   }

   private int readUnsignedShort(SeekableStream stream) throws IOException {
      return this.isBigEndian ? stream.readUnsignedShort() : stream.readUnsignedShortLE();
   }

   private int readInt(SeekableStream stream) throws IOException {
      return this.isBigEndian ? stream.readInt() : stream.readIntLE();
   }

   private long readUnsignedInt(SeekableStream stream) throws IOException {
      return this.isBigEndian ? stream.readUnsignedInt() : stream.readUnsignedIntLE();
   }

   private long readLong(SeekableStream stream) throws IOException {
      return this.isBigEndian ? stream.readLong() : stream.readLongLE();
   }

   private float readFloat(SeekableStream stream) throws IOException {
      return this.isBigEndian ? stream.readFloat() : stream.readFloatLE();
   }

   private double readDouble(SeekableStream stream) throws IOException {
      return this.isBigEndian ? stream.readDouble() : stream.readDoubleLE();
   }

   private static int readUnsignedShort(SeekableStream stream, boolean isBigEndian) throws IOException {
      return isBigEndian ? stream.readUnsignedShort() : stream.readUnsignedShortLE();
   }

   private static long readUnsignedInt(SeekableStream stream, boolean isBigEndian) throws IOException {
      return isBigEndian ? stream.readUnsignedInt() : stream.readUnsignedIntLE();
   }

   public static int getNumDirectories(SeekableStream stream) throws IOException {
      long pointer = stream.getFilePointer();
      stream.seek(0L);
      int endian = stream.readUnsignedShort();
      if (!isValidEndianTag(endian)) {
         throw new IllegalArgumentException("TIFFDirectory1");
      } else {
         boolean isBigEndian = endian == 19789;
         int magic = readUnsignedShort(stream, isBigEndian);
         if (magic != 42) {
            throw new IllegalArgumentException("TIFFDirectory2");
         } else {
            stream.seek(4L);
            long offset = readUnsignedInt(stream, isBigEndian);

            int numDirectories;
            for(numDirectories = 0; offset != 0L; offset = readUnsignedInt(stream, isBigEndian)) {
               ++numDirectories;
               stream.seek(offset);
               long entries = (long)readUnsignedShort(stream, isBigEndian);
               stream.skip(12L * entries);
            }

            stream.seek(pointer);
            return numDirectories;
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

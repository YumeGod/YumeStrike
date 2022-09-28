package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CmapFormat4 extends CmapFormat {
   public int language;
   private int segCountX2;
   private int searchRange;
   private int entrySelector;
   private int rangeShift;
   private int[] endCode;
   private int[] startCode;
   private int[] idDelta;
   private int[] idRangeOffset;
   private int[] glyphIdArray;
   private int segCount;
   private int first;
   private int last;

   protected CmapFormat4(RandomAccessFile var1) throws IOException {
      super(var1);
      this.format = 4;
      this.segCountX2 = var1.readUnsignedShort();
      this.segCount = this.segCountX2 / 2;
      this.endCode = new int[this.segCount];
      this.startCode = new int[this.segCount];
      this.idDelta = new int[this.segCount];
      this.idRangeOffset = new int[this.segCount];
      this.searchRange = var1.readUnsignedShort();
      this.entrySelector = var1.readUnsignedShort();
      this.rangeShift = var1.readUnsignedShort();
      this.last = -1;

      int var2;
      for(var2 = 0; var2 < this.segCount; ++var2) {
         this.endCode[var2] = var1.readUnsignedShort();
         if (this.endCode[var2] > this.last) {
            this.last = this.endCode[var2];
         }
      }

      var1.readUnsignedShort();

      for(var2 = 0; var2 < this.segCount; ++var2) {
         this.startCode[var2] = var1.readUnsignedShort();
         if (var2 == 0 || this.startCode[var2] < this.first) {
            this.first = this.startCode[var2];
         }
      }

      for(var2 = 0; var2 < this.segCount; ++var2) {
         this.idDelta[var2] = var1.readUnsignedShort();
      }

      for(var2 = 0; var2 < this.segCount; ++var2) {
         this.idRangeOffset[var2] = var1.readUnsignedShort();
      }

      var2 = (this.length - 16 - this.segCount * 8) / 2;
      this.glyphIdArray = new int[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.glyphIdArray[var3] = var1.readUnsignedShort();
      }

   }

   public int getFirst() {
      return this.first;
   }

   public int getLast() {
      return this.last;
   }

   public int mapCharCode(int var1) {
      try {
         if (var1 < 0 || var1 >= 65534) {
            return 0;
         }

         for(int var2 = 0; var2 < this.segCount; ++var2) {
            if (this.endCode[var2] >= var1) {
               if (this.startCode[var2] <= var1) {
                  if (this.idRangeOffset[var2] > 0) {
                     return this.glyphIdArray[this.idRangeOffset[var2] / 2 + (var1 - this.startCode[var2]) - (this.segCount - var2)];
                  }

                  return (this.idDelta[var2] + var1) % 65536;
               }
               break;
            }
         }
      } catch (ArrayIndexOutOfBoundsException var3) {
         System.err.println("error: Array out of bounds - " + var3.getMessage());
      }

      return 0;
   }

   public String toString() {
      return (new StringBuffer(80)).append(super.toString()).append(", segCountX2: ").append(this.segCountX2).append(", searchRange: ").append(this.searchRange).append(", entrySelector: ").append(this.entrySelector).append(", rangeShift: ").append(this.rangeShift).append(", endCode: ").append(intToStr(this.endCode)).append(", startCode: ").append(intToStr(this.startCode)).append(", idDelta: ").append(intToStr(this.idDelta)).append(", idRangeOffset: ").append(intToStr(this.idRangeOffset)).toString();
   }

   private static String intToStr(int[] var0) {
      int var1 = var0.length;
      StringBuffer var2 = new StringBuffer(var1 * 8);
      var2.append('[');

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(var0[var3]);
         if (var3 < var1 - 1) {
            var2.append(',');
         }
      }

      var2.append(']');
      return var2.toString();
   }
}

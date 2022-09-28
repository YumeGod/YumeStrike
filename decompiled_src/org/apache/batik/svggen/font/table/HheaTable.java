package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class HheaTable implements Table {
   private int version;
   private short ascender;
   private short descender;
   private short lineGap;
   private short advanceWidthMax;
   private short minLeftSideBearing;
   private short minRightSideBearing;
   private short xMaxExtent;
   private short caretSlopeRise;
   private short caretSlopeRun;
   private short metricDataFormat;
   private int numberOfHMetrics;

   protected HheaTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.version = var2.readInt();
      this.ascender = var2.readShort();
      this.descender = var2.readShort();
      this.lineGap = var2.readShort();
      this.advanceWidthMax = var2.readShort();
      this.minLeftSideBearing = var2.readShort();
      this.minRightSideBearing = var2.readShort();
      this.xMaxExtent = var2.readShort();
      this.caretSlopeRise = var2.readShort();
      this.caretSlopeRun = var2.readShort();

      for(int var3 = 0; var3 < 5; ++var3) {
         var2.readShort();
      }

      this.metricDataFormat = var2.readShort();
      this.numberOfHMetrics = var2.readUnsignedShort();
   }

   public short getAdvanceWidthMax() {
      return this.advanceWidthMax;
   }

   public short getAscender() {
      return this.ascender;
   }

   public short getCaretSlopeRise() {
      return this.caretSlopeRise;
   }

   public short getCaretSlopeRun() {
      return this.caretSlopeRun;
   }

   public short getDescender() {
      return this.descender;
   }

   public short getLineGap() {
      return this.lineGap;
   }

   public short getMetricDataFormat() {
      return this.metricDataFormat;
   }

   public short getMinLeftSideBearing() {
      return this.minLeftSideBearing;
   }

   public short getMinRightSideBearing() {
      return this.minRightSideBearing;
   }

   public int getNumberOfHMetrics() {
      return this.numberOfHMetrics;
   }

   public int getType() {
      return 1751672161;
   }

   public short getXMaxExtent() {
      return this.xMaxExtent;
   }
}

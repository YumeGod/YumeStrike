package org.apache.fop.fonts.truetype;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TTFSubSetFile extends TTFFile {
   private byte[] output = null;
   private int realSize = 0;
   private int currentPos = 0;
   private int cvtDirOffset = 0;
   private int fpgmDirOffset = 0;
   private int glyfDirOffset = 0;
   private int headDirOffset = 0;
   private int hheaDirOffset = 0;
   private int hmtxDirOffset = 0;
   private int locaDirOffset = 0;
   private int maxpDirOffset = 0;
   private int prepDirOffset = 0;
   private int checkSumAdjustmentOffset = 0;
   private int locaOffset = 0;

   private void init(int size) {
      this.output = new byte[size];
      this.realSize = 0;
      this.currentPos = 0;
   }

   private int determineTableCount() {
      int numTables = 4;
      if (this.isCFF()) {
         throw new UnsupportedOperationException("OpenType fonts with CFF glyphs are not supported");
      } else {
         numTables += 2;
         if (this.hasCvt()) {
            ++numTables;
         }

         if (this.hasFpgm()) {
            ++numTables;
         }

         if (this.hasPrep()) {
            ++numTables;
         }

         return numTables;
      }
   }

   private void createDirectory() {
      int numTables = this.determineTableCount();
      this.writeByte((byte)0);
      this.writeByte((byte)1);
      this.writeByte((byte)0);
      this.writeByte((byte)0);
      this.realSize += 4;
      this.writeUShort(numTables);
      this.realSize += 2;
      int maxPow = this.maxPow2(numTables);
      int searchRange = maxPow * 16;
      this.writeUShort(searchRange);
      this.realSize += 2;
      this.writeUShort(maxPow);
      this.realSize += 2;
      this.writeUShort(numTables * 16 - searchRange);
      this.realSize += 2;
      if (this.hasCvt()) {
         this.writeString("cvt ");
         this.cvtDirOffset = this.currentPos;
         this.currentPos += 12;
         this.realSize += 16;
      }

      if (this.hasFpgm()) {
         this.writeString("fpgm");
         this.fpgmDirOffset = this.currentPos;
         this.currentPos += 12;
         this.realSize += 16;
      }

      this.writeString("glyf");
      this.glyfDirOffset = this.currentPos;
      this.currentPos += 12;
      this.realSize += 16;
      this.writeString("head");
      this.headDirOffset = this.currentPos;
      this.currentPos += 12;
      this.realSize += 16;
      this.writeString("hhea");
      this.hheaDirOffset = this.currentPos;
      this.currentPos += 12;
      this.realSize += 16;
      this.writeString("hmtx");
      this.hmtxDirOffset = this.currentPos;
      this.currentPos += 12;
      this.realSize += 16;
      this.writeString("loca");
      this.locaDirOffset = this.currentPos;
      this.currentPos += 12;
      this.realSize += 16;
      this.writeString("maxp");
      this.maxpDirOffset = this.currentPos;
      this.currentPos += 12;
      this.realSize += 16;
      if (this.hasPrep()) {
         this.writeString("prep");
         this.prepDirOffset = this.currentPos;
         this.currentPos += 12;
         this.realSize += 16;
      }

   }

   private boolean createCvt(FontFileReader in) throws IOException {
      TTFDirTabEntry entry = (TTFDirTabEntry)this.dirTabs.get("cvt ");
      if (entry != null) {
         this.pad4();
         this.seekTab(in, "cvt ", 0L);
         System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()), 0, this.output, this.currentPos, (int)entry.getLength());
         int checksum = this.getCheckSum(this.currentPos, (int)entry.getLength());
         this.writeULong(this.cvtDirOffset, checksum);
         this.writeULong(this.cvtDirOffset + 4, this.currentPos);
         this.writeULong(this.cvtDirOffset + 8, (int)entry.getLength());
         this.currentPos += (int)entry.getLength();
         this.realSize += (int)entry.getLength();
         return true;
      } else {
         return false;
      }
   }

   private boolean hasCvt() {
      return this.dirTabs.containsKey("cvt ");
   }

   private boolean hasFpgm() {
      return this.dirTabs.containsKey("fpgm");
   }

   private boolean hasPrep() {
      return this.dirTabs.containsKey("prep");
   }

   private boolean createFpgm(FontFileReader in) throws IOException {
      TTFDirTabEntry entry = (TTFDirTabEntry)this.dirTabs.get("fpgm");
      if (entry != null) {
         this.pad4();
         this.seekTab(in, "fpgm", 0L);
         System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()), 0, this.output, this.currentPos, (int)entry.getLength());
         int checksum = this.getCheckSum(this.currentPos, (int)entry.getLength());
         this.writeULong(this.fpgmDirOffset, checksum);
         this.writeULong(this.fpgmDirOffset + 4, this.currentPos);
         this.writeULong(this.fpgmDirOffset + 8, (int)entry.getLength());
         this.currentPos += (int)entry.getLength();
         this.realSize += (int)entry.getLength();
         return true;
      } else {
         return false;
      }
   }

   private void createLoca(int size) throws IOException {
      this.pad4();
      this.locaOffset = this.currentPos;
      this.writeULong(this.locaDirOffset + 4, this.currentPos);
      this.writeULong(this.locaDirOffset + 8, size * 4 + 4);
      this.currentPos += size * 4 + 4;
      this.realSize += size * 4 + 4;
   }

   private void createMaxp(FontFileReader in, int size) throws IOException {
      TTFDirTabEntry entry = (TTFDirTabEntry)this.dirTabs.get("maxp");
      if (entry != null) {
         this.pad4();
         this.seekTab(in, "maxp", 0L);
         System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()), 0, this.output, this.currentPos, (int)entry.getLength());
         this.writeUShort(this.currentPos + 4, size);
         int checksum = this.getCheckSum(this.currentPos, (int)entry.getLength());
         this.writeULong(this.maxpDirOffset, checksum);
         this.writeULong(this.maxpDirOffset + 4, this.currentPos);
         this.writeULong(this.maxpDirOffset + 8, (int)entry.getLength());
         this.currentPos += (int)entry.getLength();
         this.realSize += (int)entry.getLength();
      } else {
         throw new IOException("Can't find maxp table");
      }
   }

   private boolean createPrep(FontFileReader in) throws IOException {
      TTFDirTabEntry entry = (TTFDirTabEntry)this.dirTabs.get("prep");
      if (entry != null) {
         this.pad4();
         this.seekTab(in, "prep", 0L);
         System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()), 0, this.output, this.currentPos, (int)entry.getLength());
         int checksum = this.getCheckSum(this.currentPos, (int)entry.getLength());
         this.writeULong(this.prepDirOffset, checksum);
         this.writeULong(this.prepDirOffset + 4, this.currentPos);
         this.writeULong(this.prepDirOffset + 8, (int)entry.getLength());
         this.currentPos += (int)entry.getLength();
         this.realSize += (int)entry.getLength();
         return true;
      } else {
         return false;
      }
   }

   private void createHhea(FontFileReader in, int size) throws IOException {
      TTFDirTabEntry entry = (TTFDirTabEntry)this.dirTabs.get("hhea");
      if (entry != null) {
         this.pad4();
         this.seekTab(in, "hhea", 0L);
         System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()), 0, this.output, this.currentPos, (int)entry.getLength());
         this.writeUShort((int)entry.getLength() + this.currentPos - 2, size);
         int checksum = this.getCheckSum(this.currentPos, (int)entry.getLength());
         this.writeULong(this.hheaDirOffset, checksum);
         this.writeULong(this.hheaDirOffset + 4, this.currentPos);
         this.writeULong(this.hheaDirOffset + 8, (int)entry.getLength());
         this.currentPos += (int)entry.getLength();
         this.realSize += (int)entry.getLength();
      } else {
         throw new IOException("Can't find hhea table");
      }
   }

   private void createHead(FontFileReader in) throws IOException {
      TTFDirTabEntry entry = (TTFDirTabEntry)this.dirTabs.get("head");
      if (entry != null) {
         this.pad4();
         this.seekTab(in, "head", 0L);
         System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()), 0, this.output, this.currentPos, (int)entry.getLength());
         this.checkSumAdjustmentOffset = this.currentPos + 8;
         this.output[this.currentPos + 8] = 0;
         this.output[this.currentPos + 9] = 0;
         this.output[this.currentPos + 10] = 0;
         this.output[this.currentPos + 11] = 0;
         this.output[this.currentPos + 50] = 0;
         this.output[this.currentPos + 51] = 1;
         int checksum = this.getCheckSum(this.currentPos, (int)entry.getLength());
         this.writeULong(this.headDirOffset, checksum);
         this.writeULong(this.headDirOffset + 4, this.currentPos);
         this.writeULong(this.headDirOffset + 8, (int)entry.getLength());
         this.currentPos += (int)entry.getLength();
         this.realSize += (int)entry.getLength();
      } else {
         throw new IOException("Can't find head table");
      }
   }

   private void createGlyf(FontFileReader in, Map glyphs) throws IOException {
      TTFDirTabEntry entry = (TTFDirTabEntry)this.dirTabs.get("glyf");
      int size = false;
      int start = false;
      int endOffset = 0;
      if (entry == null) {
         throw new IOException("Can't find glyf table");
      } else {
         this.pad4();
         int start = this.currentPos;
         int[] origIndexes = new int[glyphs.size()];

         Integer origIndex;
         Integer subsetIndex;
         for(Iterator e = glyphs.keySet().iterator(); e.hasNext(); origIndexes[subsetIndex] = origIndex) {
            origIndex = (Integer)e.next();
            subsetIndex = (Integer)glyphs.get(origIndex);
         }

         int i;
         for(i = 0; i < origIndexes.length; ++i) {
            int glyphLength = false;
            int nextOffset = false;
            int origGlyphIndex = origIndexes[i];
            int nextOffset;
            if (origGlyphIndex >= this.mtxTab.length - 1) {
               nextOffset = (int)this.lastLoca;
            } else {
               nextOffset = (int)this.mtxTab[origGlyphIndex + 1].getOffset();
            }

            int glyphLength = nextOffset - (int)this.mtxTab[origGlyphIndex].getOffset();
            System.arraycopy(in.getBytes((int)entry.getOffset() + (int)this.mtxTab[origGlyphIndex].getOffset(), glyphLength), 0, this.output, this.currentPos, glyphLength);
            this.writeULong(this.locaOffset + i * 4, this.currentPos - start);
            if (this.currentPos - start + glyphLength > endOffset) {
               endOffset = this.currentPos - start + glyphLength;
            }

            this.currentPos += glyphLength;
            this.realSize += glyphLength;
         }

         int size = this.currentPos - start;
         i = this.getCheckSum(start, size);
         this.writeULong(this.glyfDirOffset, i);
         this.writeULong(this.glyfDirOffset + 4, start);
         this.writeULong(this.glyfDirOffset + 8, size);
         this.currentPos += 12;
         this.realSize += 12;
         this.writeULong(this.locaOffset + glyphs.size() * 4, endOffset);
         i = this.getCheckSum(this.locaOffset, glyphs.size() * 4 + 4);
         this.writeULong(this.locaDirOffset, i);
      }
   }

   private void createHmtx(FontFileReader in, Map glyphs) throws IOException {
      TTFDirTabEntry entry = (TTFDirTabEntry)this.dirTabs.get("hmtx");
      int longHorMetricSize = glyphs.size() * 2;
      int leftSideBearingSize = glyphs.size() * 2;
      int hmtxSize = longHorMetricSize + leftSideBearingSize;
      if (entry == null) {
         throw new IOException("Can't find hmtx table");
      } else {
         this.pad4();
         Iterator e = glyphs.keySet().iterator();

         while(e.hasNext()) {
            Integer origIndex = (Integer)e.next();
            Integer subsetIndex = (Integer)glyphs.get(origIndex);
            this.writeUShort(this.currentPos + subsetIndex * 4, this.mtxTab[origIndex].getWx());
            this.writeUShort(this.currentPos + subsetIndex * 4 + 2, this.mtxTab[origIndex].getLsb());
         }

         int checksum = this.getCheckSum(this.currentPos, hmtxSize);
         this.writeULong(this.hmtxDirOffset, checksum);
         this.writeULong(this.hmtxDirOffset + 4, this.currentPos);
         this.writeULong(this.hmtxDirOffset + 8, hmtxSize);
         this.currentPos += hmtxSize;
         this.realSize += hmtxSize;
      }
   }

   private List getIncludedGlyphs(FontFileReader in, int glyphOffset, Integer glyphIdx) throws IOException {
      List ret = new ArrayList();
      ret.add(glyphIdx);
      int offset = glyphOffset + (int)this.mtxTab[glyphIdx].getOffset() + 10;
      Integer compositeIdx = null;
      int flags = false;
      boolean moreComposites = true;

      while(moreComposites) {
         int flags = in.readTTFUShort((long)offset);
         compositeIdx = new Integer(in.readTTFUShort((long)(offset + 2)));
         ret.add(compositeIdx);
         offset += 4;
         if ((flags & 1) > 0) {
            offset += 4;
         } else {
            offset += 2;
         }

         if ((flags & 8) > 0) {
            offset += 2;
         } else if ((flags & 64) > 0) {
            offset += 4;
         } else if ((flags & 128) > 0) {
            offset += 8;
         }

         if ((flags & 32) > 0) {
            moreComposites = true;
         } else {
            moreComposites = false;
         }
      }

      return ret;
   }

   private void remapComposite(FontFileReader in, Map glyphs, int glyphOffset, Integer glyphIdx) throws IOException {
      int offset = glyphOffset + (int)this.mtxTab[glyphIdx].getOffset() + 10;
      Integer compositeIdx = null;
      int flags = false;
      boolean moreComposites = true;

      while(moreComposites) {
         int flags = in.readTTFUShort((long)offset);
         compositeIdx = new Integer(in.readTTFUShort((long)(offset + 2)));
         Integer newIdx = (Integer)glyphs.get(compositeIdx);
         if (newIdx == null) {
            moreComposites = false;
         } else {
            in.writeTTFUShort(offset + 2, newIdx);
            offset += 4;
            if ((flags & 1) > 0) {
               offset += 4;
            } else {
               offset += 2;
            }

            if ((flags & 8) > 0) {
               offset += 2;
            } else if ((flags & 64) > 0) {
               offset += 4;
            } else if ((flags & 128) > 0) {
               offset += 8;
            }

            if ((flags & 32) > 0) {
               moreComposites = true;
            } else {
               moreComposites = false;
            }
         }
      }

   }

   private void scanGlyphs(FontFileReader in, Map glyphs) throws IOException {
      TTFDirTabEntry entry = (TTFDirTabEntry)this.dirTabs.get("glyf");
      Map newComposites = null;
      Map allComposites = new HashMap();
      int newIndex = glyphs.size();
      if (entry == null) {
         throw new IOException("Can't find glyf table");
      } else {
         Iterator e;
         label59:
         while(newComposites == null || newComposites.size() > 0) {
            newComposites = new HashMap();
            e = glyphs.keySet().iterator();

            while(true) {
               Integer origIndex;
               do {
                  if (!e.hasNext()) {
                     Iterator m = newComposites.keySet().iterator();

                     while(m.hasNext()) {
                        Integer im = (Integer)m.next();
                        glyphs.put(im, newComposites.get(im));
                     }
                     continue label59;
                  }

                  origIndex = (Integer)e.next();
               } while(in.readTTFShort(entry.getOffset() + this.mtxTab[origIndex].getOffset()) >= 0);

               allComposites.put(origIndex, glyphs.get(origIndex));
               List composites = this.getIncludedGlyphs(in, (int)entry.getOffset(), origIndex);
               Iterator cps = composites.iterator();

               while(cps.hasNext()) {
                  Integer cIdx = (Integer)cps.next();
                  if (glyphs.get(cIdx) == null && newComposites.get(cIdx) == null) {
                     newComposites.put(cIdx, new Integer(newIndex));
                     ++newIndex;
                  }
               }
            }
         }

         e = allComposites.keySet().iterator();

         while(e.hasNext()) {
            this.remapComposite(in, glyphs, (int)entry.getOffset(), (Integer)e.next());
         }

      }
   }

   public byte[] readFont(FontFileReader in, String name, Map glyphs) throws IOException {
      if (!this.checkTTC(in, name)) {
         throw new IOException("Failed to read font");
      } else {
         Map subsetGlyphs = new HashMap(glyphs);
         this.output = new byte[in.getFileSize()];
         this.readDirTabs(in);
         this.readFontHeader(in);
         this.getNumGlyphs(in);
         this.readHorizontalHeader(in);
         this.readHorizontalMetrics(in);
         this.readIndexToLocation(in);
         this.scanGlyphs(in, subsetGlyphs);
         this.createDirectory();
         this.createHead(in);
         this.createHhea(in, subsetGlyphs.size());
         this.createHmtx(in, subsetGlyphs);
         this.createMaxp(in, subsetGlyphs.size());
         boolean optionalTableFound = this.createCvt(in);
         if (!optionalTableFound) {
            this.log.debug("TrueType: ctv table not present. Skipped.");
         }

         optionalTableFound = this.createFpgm(in);
         if (!optionalTableFound) {
            this.log.debug("TrueType: fpgm table not present. Skipped.");
         }

         optionalTableFound = this.createPrep(in);
         if (!optionalTableFound) {
            this.log.debug("TrueType: prep table not present. Skipped.");
         }

         this.createLoca(subsetGlyphs.size());
         this.createGlyf(in, subsetGlyphs);
         this.pad4();
         this.createCheckSumAdjustment();
         byte[] ret = new byte[this.realSize];
         System.arraycopy(this.output, 0, ret, 0, this.realSize);
         return ret;
      }
   }

   private int writeString(String str) {
      int length = 0;

      try {
         byte[] buf = str.getBytes("ISO-8859-1");
         System.arraycopy(buf, 0, this.output, this.currentPos, buf.length);
         length = buf.length;
         this.currentPos += length;
      } catch (UnsupportedEncodingException var4) {
      }

      return length;
   }

   private void writeByte(byte b) {
      this.output[this.currentPos++] = b;
   }

   private void writeUShort(int s) {
      byte b1 = (byte)(s >> 8 & 255);
      byte b2 = (byte)(s & 255);
      this.writeByte(b1);
      this.writeByte(b2);
   }

   private void writeUShort(int pos, int s) {
      byte b1 = (byte)(s >> 8 & 255);
      byte b2 = (byte)(s & 255);
      this.output[pos] = b1;
      this.output[pos + 1] = b2;
   }

   private void writeULong(int s) {
      byte b1 = (byte)(s >> 24 & 255);
      byte b2 = (byte)(s >> 16 & 255);
      byte b3 = (byte)(s >> 8 & 255);
      byte b4 = (byte)(s & 255);
      this.writeByte(b1);
      this.writeByte(b2);
      this.writeByte(b3);
      this.writeByte(b4);
   }

   private void writeULong(int pos, int s) {
      byte b1 = (byte)(s >> 24 & 255);
      byte b2 = (byte)(s >> 16 & 255);
      byte b3 = (byte)(s >> 8 & 255);
      byte b4 = (byte)(s & 255);
      this.output[pos] = b1;
      this.output[pos + 1] = b2;
      this.output[pos + 2] = b3;
      this.output[pos + 3] = b4;
   }

   private short readShort(int pos) {
      int ret = this.readUShort(pos);
      return (short)ret;
   }

   private int readUShort(int pos) {
      int ret = this.output[pos];
      if (ret < 0) {
         ret += 256;
      }

      ret <<= 8;
      if (this.output[pos + 1] < 0) {
         ret |= this.output[pos + 1] + 256;
      } else {
         ret |= this.output[pos + 1];
      }

      return ret;
   }

   private void pad4() {
      int padSize = this.currentPos % 4;

      for(int i = 0; i < padSize; ++i) {
         this.output[this.currentPos++] = 0;
         ++this.realSize;
      }

   }

   private int maxPow2(int max) {
      int i;
      for(i = 0; Math.pow(2.0, (double)i) < (double)max; ++i) {
      }

      return i - 1;
   }

   private int log2(int num) {
      return (int)(Math.log((double)num) / Math.log(2.0));
   }

   private int getCheckSum(int start, int size) {
      return (int)this.getLongCheckSum(start, size);
   }

   private long getLongCheckSum(int start, int size) {
      int remainder = size % 4;
      if (remainder != 0) {
         size += remainder;
      }

      long sum = 0L;

      for(int i = 0; i < size; i += 4) {
         int l = this.output[start + i] << 24;
         l += this.output[start + i + 1] << 16;
         l += this.output[start + i + 2] << 16;
         l += this.output[start + i + 3] << 16;
         sum += (long)l;
         if (sum > -1L) {
            sum -= -1L;
         }
      }

      return sum;
   }

   private void createCheckSumAdjustment() {
      long sum = this.getLongCheckSum(0, this.realSize);
      int checksum = (int)(-1313820742L - sum);
      this.writeULong(this.checkSumAdjustmentOffset, checksum);
   }
}

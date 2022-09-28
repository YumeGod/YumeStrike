package org.apache.fop.fonts.truetype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.FontUtil;
import org.apache.xmlgraphics.fonts.Glyphs;

public class TTFFile {
   static final byte NTABS = 24;
   static final int NMACGLYPHS = 258;
   static final int MAX_CHAR_CODE = 255;
   static final int ENC_BUF_SIZE = 1024;
   public static final boolean TRACE_ENABLED = false;
   private String encoding = "WinAnsiEncoding";
   private short firstChar = 0;
   private boolean isEmbeddable = true;
   private boolean hasSerifs = true;
   protected Map dirTabs;
   private Map kerningTab;
   private Map ansiKerningTab;
   private List cmaps;
   private List unicodeMapping;
   private int upem;
   private int nhmtx;
   private int postFormat;
   private int locaFormat;
   protected long lastLoca = 0L;
   private int numberOfGlyphs;
   private int nmGlyphs;
   protected TTFMtxEntry[] mtxTab;
   private int[] mtxEncoded = null;
   private String postScriptName = "";
   private String fullName = "";
   private String notice = "";
   private Set familyNames = new HashSet();
   private String subFamilyName = "";
   private long italicAngle = 0L;
   private long isFixedPitch = 0L;
   private int fontBBox1 = 0;
   private int fontBBox2 = 0;
   private int fontBBox3 = 0;
   private int fontBBox4 = 0;
   private int capHeight = 0;
   private int os2CapHeight = 0;
   private int underlinePosition = 0;
   private int underlineThickness = 0;
   private int xHeight = 0;
   private int os2xHeight = 0;
   private int ascender = 0;
   private int descender = 0;
   private int hheaAscender = 0;
   private int hheaDescender = 0;
   private int os2Ascender = 0;
   private int os2Descender = 0;
   private int usWeightClass = 0;
   private short lastChar = 0;
   private int[] ansiWidth;
   private Map ansiIndex;
   private Map glyphToUnicodeMap = new HashMap();
   private Map unicodeToGlyphMap = new HashMap();
   private TTFDirTabEntry currentDirTab;
   private boolean isCFF;
   protected Log log;

   public TTFFile() {
      this.log = LogFactory.getLog(TTFFile.class);
   }

   boolean seekTab(FontFileReader in, String name, long offset) throws IOException {
      TTFDirTabEntry dt = (TTFDirTabEntry)this.dirTabs.get(name);
      if (dt == null) {
         this.log.error("Dirtab " + name + " not found.");
         return false;
      } else {
         in.seekSet(dt.getOffset() + offset);
         this.currentDirTab = dt;
         return true;
      }
   }

   public int convertTTFUnit2PDFUnit(int n) {
      int ret;
      if (n < 0) {
         long rest1 = (long)(n % this.upem);
         long storrest = 1000L * rest1;
         long ledd2 = storrest != 0L ? rest1 / storrest : 0L;
         ret = -(-1000 * n / this.upem - (int)ledd2);
      } else {
         ret = n / this.upem * 1000 + n % this.upem * 1000 / this.upem;
      }

      return ret;
   }

   private boolean readCMAP(FontFileReader in) throws IOException {
      this.unicodeMapping = new ArrayList();
      this.seekTab(in, "cmap", 2L);
      int numCMap = in.readTTFUShort();
      long cmapUniOffset = 0L;
      long symbolMapOffset = 0L;
      if (this.log.isDebugEnabled()) {
         this.log.debug(numCMap + " cmap tables");
      }

      for(int i = 0; i < numCMap; ++i) {
         int cmapPID = in.readTTFUShort();
         int cmapEID = in.readTTFUShort();
         long cmapOffset = in.readTTFULong();
         if (this.log.isDebugEnabled()) {
            this.log.debug("Platform ID: " + cmapPID + " Encoding: " + cmapEID);
         }

         if (cmapPID == 3 && cmapEID == 1) {
            cmapUniOffset = cmapOffset;
         }

         if (cmapPID == 3 && cmapEID == 0) {
            symbolMapOffset = cmapOffset;
         }
      }

      if (cmapUniOffset > 0L) {
         return this.readUnicodeCmap(in, cmapUniOffset, 1);
      } else if (symbolMapOffset > 0L) {
         return this.readUnicodeCmap(in, symbolMapOffset, 0);
      } else {
         this.log.fatal("Unsupported TrueType font: No Unicode or Symbol cmap table not present. Aborting");
         return false;
      }
   }

   private boolean readUnicodeCmap(FontFileReader in, long cmapUniOffset, int encodingID) throws IOException {
      int mtxPtr = 0;
      this.seekTab(in, "cmap", cmapUniOffset);
      int cmapFormat = in.readTTFUShort();
      in.readTTFUShort();
      if (this.log.isDebugEnabled()) {
         this.log.debug("CMAP format: " + cmapFormat);
      }

      if (cmapFormat != 4) {
         this.log.error("Cmap format not supported: " + cmapFormat);
         return false;
      } else {
         in.skip(2L);
         int cmapSegCountX2 = in.readTTFUShort();
         int cmapSearchRange = in.readTTFUShort();
         int cmapEntrySelector = in.readTTFUShort();
         int cmapRangeShift = in.readTTFUShort();
         if (this.log.isDebugEnabled()) {
            this.log.debug("segCountX2   : " + cmapSegCountX2);
            this.log.debug("searchRange  : " + cmapSearchRange);
            this.log.debug("entrySelector: " + cmapEntrySelector);
            this.log.debug("rangeShift   : " + cmapRangeShift);
         }

         int[] cmapEndCounts = new int[cmapSegCountX2 / 2];
         int[] cmapStartCounts = new int[cmapSegCountX2 / 2];
         int[] cmapDeltas = new int[cmapSegCountX2 / 2];
         int[] cmapRangeOffsets = new int[cmapSegCountX2 / 2];

         int glyphIdArrayOffset;
         for(glyphIdArrayOffset = 0; glyphIdArrayOffset < cmapSegCountX2 / 2; ++glyphIdArrayOffset) {
            cmapEndCounts[glyphIdArrayOffset] = in.readTTFUShort();
         }

         in.skip(2L);

         for(glyphIdArrayOffset = 0; glyphIdArrayOffset < cmapSegCountX2 / 2; ++glyphIdArrayOffset) {
            cmapStartCounts[glyphIdArrayOffset] = in.readTTFUShort();
         }

         for(glyphIdArrayOffset = 0; glyphIdArrayOffset < cmapSegCountX2 / 2; ++glyphIdArrayOffset) {
            cmapDeltas[glyphIdArrayOffset] = in.readTTFShort();
         }

         for(glyphIdArrayOffset = 0; glyphIdArrayOffset < cmapSegCountX2 / 2; ++glyphIdArrayOffset) {
            cmapRangeOffsets[glyphIdArrayOffset] = in.readTTFUShort();
         }

         glyphIdArrayOffset = in.getCurrentPos();
         BitSet eightBitGlyphs = new BitSet(256);

         for(int i = 0; i < cmapStartCounts.length; ++i) {
            if (this.log.isTraceEnabled()) {
               this.log.trace(i + ": " + cmapStartCounts[i] + " - " + cmapEndCounts[i]);
            }

            if (this.log.isDebugEnabled() && this.isInPrivateUseArea(cmapStartCounts[i], cmapEndCounts[i])) {
               this.log.debug("Font contains glyphs in the Unicode private use area: " + Integer.toHexString(cmapStartCounts[i]) + " - " + Integer.toHexString(cmapEndCounts[i]));
            }

            for(int j = cmapStartCounts[i]; j <= cmapEndCounts[i]; ++j) {
               if (j < 256 && j > this.lastChar) {
                  this.lastChar = (short)j;
               }

               if (j < 256) {
                  eightBitGlyphs.set(j);
               }

               if (mtxPtr < this.mtxTab.length) {
                  int glyphIdx;
                  if (cmapRangeOffsets[i] != 0 && j != 65535) {
                     int glyphOffset = glyphIdArrayOffset + (cmapRangeOffsets[i] / 2 + (j - cmapStartCounts[i]) + i - cmapSegCountX2 / 2) * 2;
                     in.seekSet((long)glyphOffset);
                     glyphIdx = in.readTTFUShort() + cmapDeltas[i] & '\uffff';
                     this.unicodeMapping.add(new UnicodeMapping(glyphIdx, j));
                     this.mtxTab[glyphIdx].getUnicodeIndex().add(new Integer(j));
                     if (encodingID == 0 && j >= 61472 && j <= 61695) {
                        int mapped = j - '\uf000';
                        if (!eightBitGlyphs.get(mapped)) {
                           this.unicodeMapping.add(new UnicodeMapping(glyphIdx, mapped));
                           this.mtxTab[glyphIdx].getUnicodeIndex().add(new Integer(mapped));
                        }
                     }

                     List v = (List)this.ansiIndex.get(new Integer(j));
                     if (v != null) {
                        Iterator e = v.listIterator();

                        while(e.hasNext()) {
                           Integer aIdx = (Integer)e.next();
                           this.ansiWidth[aIdx] = this.mtxTab[glyphIdx].getWx();
                           if (this.log.isTraceEnabled()) {
                              this.log.trace("Added width " + this.mtxTab[glyphIdx].getWx() + " uni: " + j + " ansi: " + aIdx);
                           }
                        }
                     }

                     if (this.log.isTraceEnabled()) {
                        this.log.trace("Idx: " + glyphIdx + " Delta: " + cmapDeltas[i] + " Unicode: " + j + " name: " + this.mtxTab[glyphIdx].getName());
                     }
                  } else {
                     glyphIdx = j + cmapDeltas[i] & '\uffff';
                     if (glyphIdx < this.mtxTab.length) {
                        this.mtxTab[glyphIdx].getUnicodeIndex().add(new Integer(j));
                     } else {
                        this.log.debug("Glyph " + glyphIdx + " out of range: " + this.mtxTab.length);
                     }

                     this.unicodeMapping.add(new UnicodeMapping(glyphIdx, j));
                     if (glyphIdx < this.mtxTab.length) {
                        this.mtxTab[glyphIdx].getUnicodeIndex().add(new Integer(j));
                     } else {
                        this.log.debug("Glyph " + glyphIdx + " out of range: " + this.mtxTab.length);
                     }

                     List v = (List)this.ansiIndex.get(new Integer(j));
                     Integer aIdx;
                     if (v != null) {
                        for(Iterator e = v.listIterator(); e.hasNext(); this.ansiWidth[aIdx] = this.mtxTab[glyphIdx].getWx()) {
                           aIdx = (Integer)e.next();
                        }
                     }
                  }

                  if (glyphIdx < this.mtxTab.length && this.mtxTab[glyphIdx].getUnicodeIndex().size() < 2) {
                     ++mtxPtr;
                  }
               }
            }
         }

         return true;
      }
   }

   private boolean isInPrivateUseArea(int start, int end) {
      return this.isInPrivateUseArea(start) || this.isInPrivateUseArea(end);
   }

   private boolean isInPrivateUseArea(int unicode) {
      return unicode >= 57344 && unicode <= 63743;
   }

   private void printMaxMin() {
      int min = 255;
      int max = 0;

      for(int i = 0; i < this.mtxTab.length; ++i) {
         if (this.mtxTab[i].getIndex() < min) {
            min = this.mtxTab[i].getIndex();
         }

         if (this.mtxTab[i].getIndex() > max) {
            max = this.mtxTab[i].getIndex();
         }
      }

      this.log.info("Min: " + min);
      this.log.info("Max: " + max);
   }

   public void readFont(FontFileReader in) throws IOException {
      this.readFont(in, (String)null);
   }

   private void initAnsiWidths() {
      this.ansiWidth = new int[256];

      int i;
      for(i = 0; i < 256; ++i) {
         this.ansiWidth[i] = this.mtxTab[0].getWx();
      }

      this.ansiIndex = new HashMap();

      for(i = 32; i < Glyphs.WINANSI_ENCODING.length; ++i) {
         Integer ansi = new Integer(i);
         Integer uni = new Integer(Glyphs.WINANSI_ENCODING[i]);
         List v = (List)this.ansiIndex.get(uni);
         if (v == null) {
            v = new ArrayList();
            this.ansiIndex.put(uni, v);
         }

         ((List)v).add(ansi);
      }

   }

   public boolean readFont(FontFileReader in, String name) throws IOException {
      if (!this.checkTTC(in, name)) {
         if (name == null) {
            throw new IllegalArgumentException("For TrueType collection you must specify which font to select (-ttcname)");
         } else {
            throw new IOException("Name does not exist in the TrueType collection: " + name);
         }
      } else {
         this.readDirTabs(in);
         this.readFontHeader(in);
         this.getNumGlyphs(in);
         if (this.log.isDebugEnabled()) {
            this.log.debug("Number of glyphs in font: " + this.numberOfGlyphs);
         }

         this.readHorizontalHeader(in);
         this.readHorizontalMetrics(in);
         this.initAnsiWidths();
         this.readPostScript(in);
         this.readOS2(in);
         this.determineAscDesc();
         if (!this.isCFF) {
            this.readIndexToLocation(in);
            this.readGlyf(in);
         }

         this.readName(in);
         this.readPCLT(in);
         boolean valid = this.readCMAP(in);
         if (!valid) {
            return false;
         } else {
            this.createCMaps();
            this.readKerning(in);
            this.guessVerticalMetricsFromGlyphBBox();
            return true;
         }
      }
   }

   private void createCMaps() {
      this.cmaps = new ArrayList();
      TTFCmapEntry tce = new TTFCmapEntry();
      Iterator e = this.unicodeMapping.listIterator();
      UnicodeMapping um = (UnicodeMapping)e.next();
      UnicodeMapping lastMapping = um;
      tce.setUnicodeStart(um.getUnicodeIndex());
      tce.setGlyphStartIndex(um.getGlyphIndex());

      for(; e.hasNext(); lastMapping = um) {
         um = (UnicodeMapping)e.next();
         if (lastMapping.getUnicodeIndex() + 1 != um.getUnicodeIndex() || lastMapping.getGlyphIndex() + 1 != um.getGlyphIndex()) {
            tce.setUnicodeEnd(lastMapping.getUnicodeIndex());
            this.cmaps.add(tce);
            tce = new TTFCmapEntry();
            tce.setUnicodeStart(um.getUnicodeIndex());
            tce.setGlyphStartIndex(um.getGlyphIndex());
         }
      }

      tce.setUnicodeEnd(um.getUnicodeIndex());
      this.cmaps.add(tce);
   }

   public String getPostScriptName() {
      return this.postScriptName.length() == 0 ? FontUtil.stripWhiteSpace(this.getFullName()) : this.postScriptName;
   }

   public Set getFamilyNames() {
      return this.familyNames;
   }

   public String getSubFamilyName() {
      return this.subFamilyName;
   }

   public String getFullName() {
      return this.fullName;
   }

   public String getCharSetName() {
      return this.encoding;
   }

   public int getCapHeight() {
      return this.convertTTFUnit2PDFUnit(this.capHeight);
   }

   public int getXHeight() {
      return this.convertTTFUnit2PDFUnit(this.xHeight);
   }

   public int getFlags() {
      int flags = 32;
      if (this.italicAngle != 0L) {
         flags |= 64;
      }

      if (this.isFixedPitch != 0L) {
         flags |= 2;
      }

      if (this.hasSerifs) {
         flags |= 1;
      }

      return flags;
   }

   public int getWeightClass() {
      return this.usWeightClass;
   }

   public String getStemV() {
      return "0";
   }

   public String getItalicAngle() {
      String ia = Short.toString((short)((int)(this.italicAngle / 65536L)));
      return ia;
   }

   public int[] getFontBBox() {
      int[] fbb = new int[]{this.convertTTFUnit2PDFUnit(this.fontBBox1), this.convertTTFUnit2PDFUnit(this.fontBBox2), this.convertTTFUnit2PDFUnit(this.fontBBox3), this.convertTTFUnit2PDFUnit(this.fontBBox4)};
      return fbb;
   }

   public int getLowerCaseAscent() {
      return this.convertTTFUnit2PDFUnit(this.ascender);
   }

   public int getLowerCaseDescent() {
      return this.convertTTFUnit2PDFUnit(this.descender);
   }

   public short getLastChar() {
      return this.lastChar;
   }

   public short getFirstChar() {
      return this.firstChar;
   }

   public int[] getWidths() {
      int[] wx = new int[this.mtxTab.length];

      for(int i = 0; i < wx.length; ++i) {
         wx[i] = this.convertTTFUnit2PDFUnit(this.mtxTab[i].getWx());
      }

      return wx;
   }

   public int getCharWidth(int idx) {
      return this.convertTTFUnit2PDFUnit(this.ansiWidth[idx]);
   }

   public Map getKerning() {
      return this.kerningTab;
   }

   public Map getAnsiKerning() {
      return this.ansiKerningTab;
   }

   public boolean isEmbeddable() {
      return this.isEmbeddable;
   }

   public boolean isCFF() {
      return this.isCFF;
   }

   protected void readDirTabs(FontFileReader in) throws IOException {
      int sfntVersion = in.readTTFLong();
      switch (sfntVersion) {
         case 65536:
            this.log.debug("sfnt version: OpenType 1.0");
            break;
         case 1330926671:
            this.isCFF = true;
            this.log.debug("sfnt version: OpenType with CFF data");
            break;
         case 1953658213:
            this.log.debug("sfnt version: Apple TrueType");
            break;
         case 1954115633:
            this.log.debug("sfnt version: Apple Type 1 housed in sfnt wrapper");
            break;
         default:
            this.log.debug("Unknown sfnt version: " + Integer.toHexString(sfntVersion));
      }

      int ntabs = in.readTTFUShort();
      in.skip(6L);
      this.dirTabs = new HashMap();
      TTFDirTabEntry[] pd = new TTFDirTabEntry[ntabs];
      this.log.debug("Reading " + ntabs + " dir tables");

      for(int i = 0; i < ntabs; ++i) {
         pd[i] = new TTFDirTabEntry();
         this.dirTabs.put(pd[i].read(in), pd[i]);
      }

      this.log.debug("dir tables: " + this.dirTabs.keySet());
   }

   protected void readFontHeader(FontFileReader in) throws IOException {
      this.seekTab(in, "head", 16L);
      int flags = in.readTTFUShort();
      if (this.log.isDebugEnabled()) {
         this.log.debug("flags: " + flags + " - " + Integer.toString(flags, 2));
      }

      this.upem = in.readTTFUShort();
      if (this.log.isDebugEnabled()) {
         this.log.debug("unit per em: " + this.upem);
      }

      in.skip(16L);
      this.fontBBox1 = in.readTTFShort();
      this.fontBBox2 = in.readTTFShort();
      this.fontBBox3 = in.readTTFShort();
      this.fontBBox4 = in.readTTFShort();
      if (this.log.isDebugEnabled()) {
         this.log.debug("font bbox: xMin=" + this.fontBBox1 + " yMin=" + this.fontBBox2 + " xMax=" + this.fontBBox3 + " yMax=" + this.fontBBox4);
      }

      in.skip(6L);
      this.locaFormat = in.readTTFShort();
   }

   protected void getNumGlyphs(FontFileReader in) throws IOException {
      this.seekTab(in, "maxp", 4L);
      this.numberOfGlyphs = in.readTTFUShort();
   }

   protected void readHorizontalHeader(FontFileReader in) throws IOException {
      this.seekTab(in, "hhea", 4L);
      this.hheaAscender = in.readTTFShort();
      this.hheaDescender = in.readTTFShort();
      in.skip(26L);
      this.nhmtx = in.readTTFUShort();
      if (this.log.isDebugEnabled()) {
         this.log.debug("hhea.Ascender: " + this.formatUnitsForDebug(this.hheaAscender));
         this.log.debug("hhea.Descender: " + this.formatUnitsForDebug(this.hheaDescender));
         this.log.debug("Number of horizontal metrics: " + this.nhmtx);
      }

   }

   protected void readHorizontalMetrics(FontFileReader in) throws IOException {
      this.seekTab(in, "hmtx", 0L);
      int mtxSize = Math.max(this.numberOfGlyphs, this.nhmtx);
      this.mtxTab = new TTFMtxEntry[mtxSize];

      int lastWidth;
      for(lastWidth = 0; lastWidth < mtxSize; ++lastWidth) {
         this.mtxTab[lastWidth] = new TTFMtxEntry();
      }

      for(lastWidth = 0; lastWidth < this.nhmtx; ++lastWidth) {
         this.mtxTab[lastWidth].setWx(in.readTTFUShort());
         this.mtxTab[lastWidth].setLsb(in.readTTFUShort());
      }

      if (this.nhmtx < mtxSize) {
         lastWidth = this.mtxTab[this.nhmtx - 1].getWx();

         for(int i = this.nhmtx; i < mtxSize; ++i) {
            this.mtxTab[i].setWx(lastWidth);
            this.mtxTab[i].setLsb(in.readTTFUShort());
         }
      }

   }

   private final void readPostScript(FontFileReader in) throws IOException {
      this.seekTab(in, "post", 0L);
      this.postFormat = in.readTTFLong();
      this.italicAngle = in.readTTFULong();
      this.underlinePosition = in.readTTFShort();
      this.underlineThickness = in.readTTFShort();
      this.isFixedPitch = in.readTTFULong();
      in.skip(16L);
      this.log.debug("PostScript format: 0x" + Integer.toHexString(this.postFormat));
      int numGlyphStrings;
      switch (this.postFormat) {
         case 65536:
            this.log.debug("PostScript format 1");

            for(numGlyphStrings = 0; numGlyphStrings < Glyphs.MAC_GLYPH_NAMES.length; ++numGlyphStrings) {
               this.mtxTab[numGlyphStrings].setName(Glyphs.MAC_GLYPH_NAMES[numGlyphStrings]);
            }

            return;
         case 131072:
            this.log.debug("PostScript format 2");
            numGlyphStrings = 0;
            int l = in.readTTFUShort();

            for(int i = 0; i < l; ++i) {
               this.mtxTab[i].setIndex(in.readTTFUShort());
               if (this.mtxTab[i].getIndex() > 257) {
                  ++numGlyphStrings;
               }

               if (this.log.isTraceEnabled()) {
                  this.log.trace("PostScript index: " + this.mtxTab[i].getIndexAsString());
               }
            }

            String[] psGlyphsBuffer = new String[numGlyphStrings];
            if (this.log.isDebugEnabled()) {
               this.log.debug("Reading " + numGlyphStrings + " glyphnames, that are not in the standard Macintosh" + " set. Total number of glyphs=" + l);
            }

            int i;
            for(i = 0; i < psGlyphsBuffer.length; ++i) {
               psGlyphsBuffer[i] = in.readTTFString(in.readTTFUByte());
            }

            for(i = 0; i < l; ++i) {
               if (this.mtxTab[i].getIndex() < 258) {
                  this.mtxTab[i].setName(Glyphs.MAC_GLYPH_NAMES[this.mtxTab[i].getIndex()]);
               } else if (!this.mtxTab[i].isIndexReserved()) {
                  int k = this.mtxTab[i].getIndex() - 258;
                  if (this.log.isTraceEnabled()) {
                     this.log.trace(k + " i=" + i + " mtx=" + this.mtxTab.length + " ps=" + psGlyphsBuffer.length);
                  }

                  this.mtxTab[i].setName(psGlyphsBuffer[k]);
               }
            }

            return;
         case 196608:
            this.log.debug("PostScript format 3");
            break;
         default:
            this.log.error("Unknown PostScript format: " + this.postFormat);
      }

   }

   private void readOS2(FontFileReader in) throws IOException {
      TTFDirTabEntry os2Entry = (TTFDirTabEntry)this.dirTabs.get("OS/2");
      if (os2Entry != null) {
         this.seekTab(in, "OS/2", 0L);
         int version = in.readTTFUShort();
         if (this.log.isDebugEnabled()) {
            this.log.debug("OS/2 table: version=" + version + ", offset=" + os2Entry.getOffset() + ", len=" + os2Entry.getLength());
         }

         in.skip(2L);
         this.usWeightClass = in.readTTFUShort();
         in.skip(2L);
         int fsType = in.readTTFUShort();
         if (fsType == 2) {
            this.isEmbeddable = false;
         } else {
            this.isEmbeddable = true;
         }

         in.skip(22L);
         in.skip(10L);
         in.skip(16L);
         in.skip(4L);
         in.skip(6L);
         this.os2Ascender = in.readTTFShort();
         this.os2Descender = in.readTTFShort();
         if (this.log.isDebugEnabled()) {
            this.log.debug("sTypoAscender: " + this.os2Ascender + " -> internal " + this.convertTTFUnit2PDFUnit(this.os2Ascender));
            this.log.debug("sTypoDescender: " + this.os2Descender + " -> internal " + this.convertTTFUnit2PDFUnit(this.os2Descender));
         }

         int v = in.readTTFShort();
         if (this.log.isDebugEnabled()) {
            this.log.debug("sTypoLineGap: " + v);
         }

         v = in.readTTFUShort();
         if (this.log.isDebugEnabled()) {
            this.log.debug("usWinAscent: " + this.formatUnitsForDebug(v));
         }

         v = in.readTTFUShort();
         if (this.log.isDebugEnabled()) {
            this.log.debug("usWinDescent: " + this.formatUnitsForDebug(v));
         }

         if (os2Entry.getLength() >= 90L) {
            in.skip(8L);
            this.os2xHeight = in.readTTFShort();
            this.os2CapHeight = in.readTTFShort();
            if (this.log.isDebugEnabled()) {
               this.log.debug("sxHeight: " + this.os2xHeight);
               this.log.debug("sCapHeight: " + this.os2CapHeight);
            }
         }
      } else {
         this.isEmbeddable = true;
      }

   }

   protected final void readIndexToLocation(FontFileReader in) throws IOException {
      if (!this.seekTab(in, "loca", 0L)) {
         throw new IOException("'loca' table not found, happens when the font file doesn't contain TrueType outlines (trying to read an OpenType CFF font maybe?)");
      } else {
         for(int i = 0; i < this.numberOfGlyphs; ++i) {
            this.mtxTab[i].setOffset(this.locaFormat == 1 ? in.readTTFULong() : (long)(in.readTTFUShort() << 1));
         }

         this.lastLoca = this.locaFormat == 1 ? in.readTTFULong() : (long)(in.readTTFUShort() << 1);
      }
   }

   private final void readGlyf(FontFileReader in) throws IOException {
      TTFDirTabEntry dirTab = (TTFDirTabEntry)this.dirTabs.get("glyf");
      if (dirTab == null) {
         throw new IOException("glyf table not found, cannot continue");
      } else {
         for(int i = 0; i < this.numberOfGlyphs - 1; ++i) {
            if (this.mtxTab[i].getOffset() != this.mtxTab[i + 1].getOffset()) {
               in.seekSet(dirTab.getOffset() + this.mtxTab[i].getOffset());
               in.skip(2L);
               int[] bbox = new int[]{in.readTTFShort(), in.readTTFShort(), in.readTTFShort(), in.readTTFShort()};
               this.mtxTab[i].setBoundingBox(bbox);
            } else {
               this.mtxTab[i].setBoundingBox(this.mtxTab[0].getBoundingBox());
            }
         }

         long n = ((TTFDirTabEntry)this.dirTabs.get("glyf")).getOffset();

         for(int i = 0; i < this.numberOfGlyphs; ++i) {
            if (i + 1 < this.mtxTab.length && this.mtxTab[i].getOffset() == this.mtxTab[i + 1].getOffset()) {
               int bbox0 = this.mtxTab[0].getBoundingBox()[0];
               int[] bbox = new int[]{bbox0, bbox0, bbox0, bbox0};
               this.mtxTab[i].setBoundingBox(bbox);
            } else {
               in.seekSet(n + this.mtxTab[i].getOffset());
               in.skip(2L);
               int[] bbox = new int[]{in.readTTFShort(), in.readTTFShort(), in.readTTFShort(), in.readTTFShort()};
               this.mtxTab[i].setBoundingBox(bbox);
            }

            if (this.log.isTraceEnabled()) {
               this.log.trace(this.mtxTab[i].toString(this));
            }
         }

      }
   }

   private final void readName(FontFileReader in) throws IOException {
      this.seekTab(in, "name", 2L);
      int i = in.getCurrentPos();
      int n = in.readTTFUShort();
      int j = in.readTTFUShort() + i - 2;

      for(i += 4; n-- > 0; i += 12) {
         in.seekSet((long)i);
         int platformID = in.readTTFUShort();
         int encodingID = in.readTTFUShort();
         int languageID = in.readTTFUShort();
         int k = in.readTTFUShort();
         int l = in.readTTFUShort();
         if ((platformID == 1 || platformID == 3) && (encodingID == 0 || encodingID == 1)) {
            in.seekSet((long)(j + in.readTTFUShort()));
            String txt;
            if (platformID == 3) {
               txt = in.readTTFString(l, encodingID);
            } else {
               txt = in.readTTFString(l);
            }

            if (this.log.isDebugEnabled()) {
               this.log.debug(platformID + " " + encodingID + " " + languageID + " " + k + " " + txt);
            }

            switch (k) {
               case 0:
                  if (this.notice.length() == 0) {
                     this.notice = txt;
                  }
                  break;
               case 1:
               case 16:
                  this.familyNames.add(txt);
                  break;
               case 2:
                  if (this.subFamilyName.length() == 0) {
                     this.subFamilyName = txt;
                  }
               case 3:
               case 5:
               case 7:
               case 8:
               case 9:
               case 10:
               case 11:
               case 12:
               case 13:
               case 14:
               case 15:
               default:
                  break;
               case 4:
                  if (this.fullName.length() == 0 || platformID == 3 && languageID == 1033) {
                     this.fullName = txt;
                  }
                  break;
               case 6:
                  if (this.postScriptName.length() == 0) {
                     this.postScriptName = txt;
                  }
            }
         }
      }

   }

   private final boolean readPCLT(FontFileReader in) throws IOException {
      TTFDirTabEntry dirTab = (TTFDirTabEntry)this.dirTabs.get("PCLT");
      if (dirTab != null) {
         in.seekSet(dirTab.getOffset() + 4L + 4L + 2L);
         this.xHeight = in.readTTFUShort();
         this.log.debug("xHeight from PCLT: " + this.formatUnitsForDebug(this.xHeight));
         in.skip(4L);
         this.capHeight = in.readTTFUShort();
         this.log.debug("capHeight from PCLT: " + this.formatUnitsForDebug(this.capHeight));
         in.skip(34L);
         int serifStyle = in.readTTFUByte();
         serifStyle >>= 6;
         serifStyle &= 3;
         if (serifStyle == 1) {
            this.hasSerifs = false;
         } else {
            this.hasSerifs = true;
         }

         return true;
      } else {
         return false;
      }
   }

   private void determineAscDesc() {
      int hheaBoxHeight = this.hheaAscender - this.hheaDescender;
      int os2BoxHeight = this.os2Ascender - this.os2Descender;
      if (this.os2Ascender > 0 && os2BoxHeight <= this.upem) {
         this.ascender = this.os2Ascender;
         this.descender = this.os2Descender;
      } else if (this.hheaAscender > 0 && hheaBoxHeight <= this.upem) {
         this.ascender = this.hheaAscender;
         this.descender = this.hheaDescender;
      } else if (this.os2Ascender > 0) {
         this.ascender = this.os2Ascender;
         this.descender = this.os2Descender;
      } else {
         this.ascender = this.hheaAscender;
         this.descender = this.hheaDescender;
      }

      if (this.log.isDebugEnabled()) {
         this.log.debug("Font box height: " + (this.ascender - this.descender));
         if (this.ascender - this.descender > this.upem) {
            this.log.debug("Ascender and descender together are larger than the em box.");
         }
      }

   }

   private void guessVerticalMetricsFromGlyphBBox() {
      int localCapHeight = 0;
      int localXHeight = 0;
      int localAscender = 0;
      int localDescender = 0;

      for(int i = 0; i < this.mtxTab.length; ++i) {
         if ("H".equals(this.mtxTab[i].getName())) {
            localCapHeight = this.mtxTab[i].getBoundingBox()[3];
         } else if ("x".equals(this.mtxTab[i].getName())) {
            localXHeight = this.mtxTab[i].getBoundingBox()[3];
         } else if ("d".equals(this.mtxTab[i].getName())) {
            localAscender = this.mtxTab[i].getBoundingBox()[3];
         } else if ("p".equals(this.mtxTab[i].getName())) {
            localDescender = this.mtxTab[i].getBoundingBox()[1];
         } else {
            List unicodeIndex = this.mtxTab[i].getUnicodeIndex();
            if (unicodeIndex.size() > 0) {
               char ch = (char)(Integer)unicodeIndex.get(0);
               if (ch == 'H') {
                  localCapHeight = this.mtxTab[i].getBoundingBox()[3];
               } else if (ch == 'x') {
                  localXHeight = this.mtxTab[i].getBoundingBox()[3];
               } else if (ch == 'd') {
                  localAscender = this.mtxTab[i].getBoundingBox()[3];
               } else if (ch == 'p') {
                  localDescender = this.mtxTab[i].getBoundingBox()[1];
               }
            }
         }
      }

      if (this.log.isDebugEnabled()) {
         this.log.debug("Ascender from glyph 'd': " + this.formatUnitsForDebug(localAscender));
         this.log.debug("Descender from glyph 'p': " + this.formatUnitsForDebug(localDescender));
      }

      if (this.ascender - this.descender > this.upem) {
         this.log.debug("Replacing specified ascender/descender with derived values to get values which fit in the em box.");
         this.ascender = localAscender;
         this.descender = localDescender;
      }

      if (this.log.isDebugEnabled()) {
         this.log.debug("xHeight from glyph 'x': " + this.formatUnitsForDebug(localXHeight));
         this.log.debug("CapHeight from glyph 'H': " + this.formatUnitsForDebug(localCapHeight));
      }

      if (this.capHeight == 0) {
         this.capHeight = localCapHeight;
         if (this.capHeight == 0) {
            this.capHeight = this.os2CapHeight;
         }

         if (this.capHeight == 0) {
            this.log.warn("capHeight value could not be determined. The font may not work as expected.");
         }
      }

      if (this.xHeight == 0) {
         this.xHeight = localXHeight;
         if (this.xHeight == 0) {
            this.xHeight = this.os2xHeight;
         }

         if (this.xHeight == 0) {
            this.log.warn("xHeight value could not be determined. The font may not work as expected.");
         }
      }

   }

   private final void readKerning(FontFileReader in) throws IOException {
      this.kerningTab = new HashMap();
      this.ansiKerningTab = new HashMap();
      TTFDirTabEntry dirTab = (TTFDirTabEntry)this.dirTabs.get("kern");
      if (dirTab != null) {
         this.seekTab(in, "kern", 2L);

         Integer u2;
         for(int n = in.readTTFUShort(); n > 0; --n) {
            in.skip(4L);
            int k = in.readTTFUShort();
            if ((k & 1) == 0 || (k & 2) != 0 || (k & 4) != 0) {
               return;
            }

            if (k >> 8 == 0) {
               k = in.readTTFUShort();
               in.skip(6L);

               while(k-- > 0) {
                  int i = in.readTTFUShort();
                  int j = in.readTTFUShort();
                  int kpx = in.readTTFShort();
                  if (kpx != 0) {
                     Integer iObj = this.glyphToUnicode(i);
                     u2 = this.glyphToUnicode(j);
                     if (iObj == null) {
                        this.log.debug("Ignoring kerning pair because no Unicode index was found for the first glyph " + i);
                     } else if (u2 == null) {
                        this.log.debug("Ignoring kerning pair because Unicode index was found for the second glyph " + i);
                     } else {
                        Map adjTab = (Map)this.kerningTab.get(iObj);
                        if (adjTab == null) {
                           adjTab = new HashMap();
                        }

                        ((Map)adjTab).put(u2, new Integer(this.convertTTFUnit2PDFUnit(kpx)));
                        this.kerningTab.put(iObj, adjTab);
                     }
                  }
               }
            }
         }

         Iterator ae = this.kerningTab.keySet().iterator();

         while(true) {
            Integer cidKey1;
            HashMap akpx;
            Integer unicodeKey;
            do {
               if (!ae.hasNext()) {
                  return;
               }

               Integer unicodeKey1 = (Integer)ae.next();
               cidKey1 = this.unicodeToGlyph(unicodeKey1);
               akpx = new HashMap();
               Map ckpx = (Map)this.kerningTab.get(unicodeKey1);
               Iterator aee = ckpx.keySet().iterator();

               while(aee.hasNext()) {
                  u2 = (Integer)aee.next();
                  unicodeKey = this.unicodeToGlyph(u2);
                  Integer kern = (Integer)ckpx.get(u2);
                  Iterator uniMap = this.mtxTab[unicodeKey].getUnicodeIndex().listIterator();

                  while(uniMap.hasNext()) {
                     Integer unicodeKey = (Integer)uniMap.next();
                     Integer[] ansiKeys = this.unicodeToWinAnsi(unicodeKey);

                     for(int u = 0; u < ansiKeys.length; ++u) {
                        akpx.put(ansiKeys[u], kern);
                     }
                  }
               }
            } while(akpx.size() <= 0);

            Iterator uniMap = this.mtxTab[cidKey1].getUnicodeIndex().listIterator();

            while(uniMap.hasNext()) {
               unicodeKey = (Integer)uniMap.next();
               Integer[] ansiKeys = this.unicodeToWinAnsi(unicodeKey);

               for(int u = 0; u < ansiKeys.length; ++u) {
                  this.ansiKerningTab.put(ansiKeys[u], akpx);
               }
            }
         }
      }
   }

   public List getCMaps() {
      return this.cmaps;
   }

   protected final boolean checkTTC(FontFileReader in, String name) throws IOException {
      String tag = in.readTTFString(4);
      if (!"ttcf".equals(tag)) {
         in.seekSet(0L);
         return true;
      } else {
         in.skip(4L);
         int numDirectories = (int)in.readTTFULong();
         long[] dirOffsets = new long[numDirectories];

         for(int i = 0; i < numDirectories; ++i) {
            dirOffsets[i] = in.readTTFULong();
         }

         this.log.info("This is a TrueType collection file with " + numDirectories + " fonts");
         this.log.info("Containing the following fonts: ");
         boolean found = false;
         long dirTabOffset = 0L;

         for(int i = 0; i < numDirectories; ++i) {
            in.seekSet(dirOffsets[i]);
            this.readDirTabs(in);
            this.readName(in);
            if (this.fullName.equals(name)) {
               found = true;
               dirTabOffset = dirOffsets[i];
               this.log.info(this.fullName + " <-- selected");
            } else {
               this.log.info(this.fullName);
            }

            this.notice = "";
            this.fullName = "";
            this.familyNames.clear();
            this.postScriptName = "";
            this.subFamilyName = "";
         }

         in.seekSet(dirTabOffset);
         return found;
      }
   }

   public final List getTTCnames(FontFileReader in) throws IOException {
      List fontNames = new ArrayList();
      String tag = in.readTTFString(4);
      if (!"ttcf".equals(tag)) {
         this.log.error("Not a TTC!");
         return null;
      } else {
         in.skip(4L);
         int numDirectories = (int)in.readTTFULong();
         long[] dirOffsets = new long[numDirectories];

         int i;
         for(i = 0; i < numDirectories; ++i) {
            dirOffsets[i] = in.readTTFULong();
         }

         if (this.log.isDebugEnabled()) {
            this.log.debug("This is a TrueType collection file with " + numDirectories + " fonts");
            this.log.debug("Containing the following fonts: ");
         }

         for(i = 0; i < numDirectories; ++i) {
            in.seekSet(dirOffsets[i]);
            this.readDirTabs(in);
            this.readName(in);
            this.log.debug(this.fullName);
            fontNames.add(this.fullName);
            this.notice = "";
            this.fullName = "";
            this.familyNames.clear();
            this.postScriptName = "";
            this.subFamilyName = "";
         }

         in.seekSet(0L);
         return fontNames;
      }
   }

   private Integer[] unicodeToWinAnsi(int unicode) {
      List ret = new ArrayList();

      for(int i = 32; i < Glyphs.WINANSI_ENCODING.length; ++i) {
         if (unicode == Glyphs.WINANSI_ENCODING[i]) {
            ret.add(new Integer(i));
         }
      }

      return (Integer[])ret.toArray(new Integer[0]);
   }

   public void printStuff() {
      System.out.println("Font name:   " + this.postScriptName);
      System.out.println("Full name:   " + this.fullName);
      System.out.println("Family name: " + this.familyNames);
      System.out.println("Subfamily name: " + this.subFamilyName);
      System.out.println("Notice:      " + this.notice);
      System.out.println("xHeight:     " + this.convertTTFUnit2PDFUnit(this.xHeight));
      System.out.println("capheight:   " + this.convertTTFUnit2PDFUnit(this.capHeight));
      int italic = (int)(this.italicAngle >> 16);
      System.out.println("Italic:      " + italic);
      System.out.print("ItalicAngle: " + (short)((int)(this.italicAngle / 65536L)));
      if (this.italicAngle % 65536L > 0L) {
         System.out.print("." + (short)((int)(this.italicAngle % 65536L * 1000L)) / 65536);
      }

      System.out.println();
      System.out.println("Ascender:    " + this.convertTTFUnit2PDFUnit(this.ascender));
      System.out.println("Descender:   " + this.convertTTFUnit2PDFUnit(this.descender));
      System.out.println("FontBBox:    [" + this.convertTTFUnit2PDFUnit(this.fontBBox1) + " " + this.convertTTFUnit2PDFUnit(this.fontBBox2) + " " + this.convertTTFUnit2PDFUnit(this.fontBBox3) + " " + this.convertTTFUnit2PDFUnit(this.fontBBox4) + "]");
   }

   private String formatUnitsForDebug(int units) {
      return units + " -> " + this.convertTTFUnit2PDFUnit(units) + " internal units";
   }

   private Integer glyphToUnicode(int glyphIndex) throws IOException {
      return (Integer)this.glyphToUnicodeMap.get(new Integer(glyphIndex));
   }

   private Integer unicodeToGlyph(int unicodeIndex) throws IOException {
      Integer result = (Integer)this.unicodeToGlyphMap.get(new Integer(unicodeIndex));
      if (result == null) {
         throw new IOException("Glyph index not found for unicode value " + unicodeIndex);
      } else {
         return result;
      }
   }

   public static void main(String[] args) {
      try {
         TTFFile ttfFile = new TTFFile();
         FontFileReader reader = new FontFileReader(args[0]);
         String name = null;
         if (args.length >= 2) {
            name = args[1];
         }

         ttfFile.readFont(reader, name);
         ttfFile.printStuff();
      } catch (IOException var4) {
         System.err.println("Problem reading font: " + var4.toString());
         var4.printStackTrace(System.err);
      }

   }

   class UnicodeMapping {
      private int unicodeIndex;
      private int glyphIndex;

      UnicodeMapping(int glyphIndex, int unicodeIndex) {
         this.unicodeIndex = unicodeIndex;
         this.glyphIndex = glyphIndex;
         TTFFile.this.glyphToUnicodeMap.put(new Integer(glyphIndex), new Integer(unicodeIndex));
         TTFFile.this.unicodeToGlyphMap.put(new Integer(unicodeIndex), new Integer(glyphIndex));
      }

      public int getGlyphIndex() {
         return this.glyphIndex;
      }

      public int getUnicodeIndex() {
         return this.unicodeIndex;
      }
   }
}

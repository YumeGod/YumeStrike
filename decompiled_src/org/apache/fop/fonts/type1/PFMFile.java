package org.apache.fop.fonts.type1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.fonts.Glyphs;

public class PFMFile {
   private String windowsName;
   private String postscriptName;
   private short dfItalic;
   private int dfWeight;
   private short dfCharSet;
   private short dfPitchAndFamily;
   private int dfAvgWidth;
   private int dfMaxWidth;
   private int dfMinWidth;
   private short dfFirstChar;
   private short dfLastChar;
   private int etmCapHeight;
   private int etmXHeight;
   private int etmLowerCaseAscent;
   private int etmLowerCaseDescent;
   private int[] extentTable;
   private Map kerningTab = new HashMap();
   protected Log log;

   public PFMFile() {
      this.log = LogFactory.getLog(PFMFile.class);
   }

   public void load(InputStream inStream) throws IOException {
      byte[] pfmBytes = IOUtils.toByteArray(inStream);
      InputStream bufin = new ByteArrayInputStream(pfmBytes);
      PFMInputStream in = new PFMInputStream(bufin);
      bufin.mark(512);
      short sh1 = in.readByte();
      short sh2 = in.readByte();
      if (sh1 == 128 && sh2 == 1) {
         throw new IOException("Cannot parse PFM file. You probably specified the PFB file of a Type 1 font as parameter instead of the PFM.");
      } else {
         bufin.reset();
         byte[] b = new byte[16];
         bufin.read(b);
         if ((new String(b, "US-ASCII")).equalsIgnoreCase("StartFontMetrics")) {
            throw new IOException("Cannot parse PFM file. You probably specified the AFM file of a Type 1 font as parameter instead of the PFM.");
         } else {
            bufin.reset();
            int version = in.readShort();
            if (version != 256) {
               this.log.warn("PFM version expected to be '256' but got '" + version + "'." + " Please make sure you specify the PFM as parameter" + " and not the PFB or the AFM.");
            }

            bufin.reset();
            this.loadHeader(in);
            this.loadExtension(in);
         }
      }
   }

   private void loadHeader(PFMInputStream inStream) throws IOException {
      inStream.skip(80L);
      this.dfItalic = inStream.readByte();
      inStream.skip(2L);
      this.dfWeight = inStream.readShort();
      this.dfCharSet = inStream.readByte();
      inStream.skip(4L);
      this.dfPitchAndFamily = inStream.readByte();
      this.dfAvgWidth = inStream.readShort();
      this.dfMaxWidth = inStream.readShort();
      this.dfFirstChar = inStream.readByte();
      this.dfLastChar = inStream.readByte();
      inStream.skip(8L);
      long faceOffset = inStream.readInt();
      inStream.reset();
      inStream.skip(faceOffset);
      this.windowsName = inStream.readString();
      inStream.reset();
      inStream.skip(117L);
   }

   private void loadExtension(PFMInputStream inStream) throws IOException {
      int size = inStream.readShort();
      if (size != 30) {
         this.log.warn("Size of extension block was expected to be 30 bytes, but was " + size + " bytes.");
      }

      long extMetricsOffset = inStream.readInt();
      long extentTableOffset = inStream.readInt();
      inStream.skip(4L);
      long kernPairOffset = inStream.readInt();
      inStream.skip(4L);
      long driverInfoOffset = inStream.readInt();
      if (kernPairOffset > 0L) {
         inStream.reset();
         inStream.skip(kernPairOffset);
         this.loadKernPairs(inStream);
      }

      inStream.reset();
      inStream.skip(driverInfoOffset);
      this.postscriptName = inStream.readString();
      if (extMetricsOffset != 0L) {
         inStream.reset();
         inStream.skip(extMetricsOffset);
         this.loadExtMetrics(inStream);
      }

      if (extentTableOffset != 0L) {
         inStream.reset();
         inStream.skip(extentTableOffset);
         this.loadExtentTable(inStream);
      }

   }

   private void loadKernPairs(PFMInputStream inStream) throws IOException {
      int i = inStream.readShort();
      if (this.log.isTraceEnabled()) {
         this.log.trace(i + " kerning pairs");
      }

      while(i > 0) {
         int g1 = inStream.readByte();
         --i;
         int g2 = inStream.readByte();
         int adj = inStream.readShort();
         if (adj > 32768) {
            adj = -(65536 - adj);
         }

         if (this.log.isTraceEnabled()) {
            this.log.trace("Char no: (" + g1 + ", " + g2 + ") kern: " + adj);
            String glyph1 = Glyphs.TEX8R_GLYPH_NAMES[g1];
            String glyph2 = Glyphs.TEX8R_GLYPH_NAMES[g2];
            this.log.trace("glyphs: " + glyph1 + ", " + glyph2);
         }

         Map adjTab = (Map)this.kerningTab.get(new Integer(g1));
         if (adjTab == null) {
            adjTab = new HashMap();
         }

         ((Map)adjTab).put(new Integer(g2), new Integer(adj));
         this.kerningTab.put(new Integer(g1), adjTab);
      }

   }

   private void loadExtMetrics(PFMInputStream inStream) throws IOException {
      int size = inStream.readShort();
      if (size != 52) {
         this.log.warn("Size of extension block was expected to be 52 bytes, but was " + size + " bytes.");
      }

      inStream.skip(12L);
      this.etmCapHeight = inStream.readShort();
      this.etmXHeight = inStream.readShort();
      this.etmLowerCaseAscent = inStream.readShort();
      this.etmLowerCaseDescent = -inStream.readShort();
   }

   private void loadExtentTable(PFMInputStream inStream) throws IOException {
      this.extentTable = new int[this.dfLastChar - this.dfFirstChar + 1];
      this.dfMinWidth = this.dfMaxWidth;

      for(short i = this.dfFirstChar; i <= this.dfLastChar; ++i) {
         this.extentTable[i - this.dfFirstChar] = inStream.readShort();
         if (this.extentTable[i - this.dfFirstChar] < this.dfMinWidth) {
            this.dfMinWidth = this.extentTable[i - this.dfFirstChar];
         }
      }

   }

   public String getWindowsName() {
      return this.windowsName;
   }

   public Map getKerning() {
      return this.kerningTab;
   }

   public String getPostscriptName() {
      return this.postscriptName;
   }

   public short getCharSet() {
      return this.dfCharSet;
   }

   public String getCharSetName() {
      switch (this.dfCharSet) {
         case 0:
            return "WinAnsi";
         case 2:
            if ("Symbol".equals(this.getPostscriptName())) {
               return "Symbol";
            }
            break;
         case 128:
            return "Shift-JIS (Japanese)";
         default:
            this.log.warn("Unknown charset detected (" + this.dfCharSet + ", 0x" + Integer.toHexString(this.dfCharSet) + "). Trying fallback to WinAnsi.");
      }

      return "WinAnsi";
   }

   public short getFirstChar() {
      return this.dfFirstChar;
   }

   public short getLastChar() {
      return this.dfLastChar;
   }

   public int getCapHeight() {
      return this.etmCapHeight;
   }

   public int getXHeight() {
      return this.etmXHeight;
   }

   public int getLowerCaseAscent() {
      return this.etmLowerCaseAscent;
   }

   public int getLowerCaseDescent() {
      return this.etmLowerCaseDescent;
   }

   public boolean getIsProportional() {
      return (this.dfPitchAndFamily & 1) == 1;
   }

   public int[] getFontBBox() {
      int[] bbox = new int[4];
      if (!this.getIsProportional() && this.dfAvgWidth == this.dfMaxWidth) {
         bbox[0] = -20;
      } else {
         bbox[0] = -100;
      }

      bbox[1] = this.getLowerCaseDescent() - 5;
      bbox[2] = this.dfMaxWidth + 10;
      bbox[3] = this.getLowerCaseAscent() + 5;
      return bbox;
   }

   public boolean isNonSymbolic() {
      return this.dfCharSet != 2;
   }

   public int getFlags() {
      int flags = 0;
      if (!this.getIsProportional()) {
         flags |= 1;
      }

      if (this.isNonSymbolic()) {
         flags |= 32;
      } else {
         flags |= 4;
      }

      if ((this.dfPitchAndFamily & 16) != 0) {
         flags |= 2;
      }

      if ((this.dfPitchAndFamily & 64) != 0) {
         flags |= 8;
      }

      if (this.dfItalic != 0) {
         flags |= 64;
      }

      return flags;
   }

   public int getStemV() {
      return this.dfItalic != 0 ? (int)Math.round((double)this.dfMinWidth * 0.25) : (int)Math.round((double)this.dfMinWidth * 0.6);
   }

   public int getItalicAngle() {
      return this.dfItalic != 0 ? -16 : 0;
   }

   public int getCharWidth(short which) {
      return this.extentTable != null ? this.extentTable[which - this.dfFirstChar] : this.dfAvgWidth;
   }
}

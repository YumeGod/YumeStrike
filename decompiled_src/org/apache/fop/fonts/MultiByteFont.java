package org.apache.fop.fonts;

import java.text.DecimalFormat;
import java.util.Map;

public class MultiByteFont extends CIDFont {
   private static int uniqueCounter = -1;
   private String ttcName = null;
   private String encoding = "Identity-H";
   private int defaultWidth = 0;
   private CIDFontType cidType;
   private String namePrefix;
   private CIDSubset subset;
   private BFEntry[] bfentries;

   public MultiByteFont() {
      this.cidType = CIDFontType.CIDTYPE2;
      this.namePrefix = null;
      this.subset = new CIDSubset();
      this.bfentries = null;
      this.subset.setupFirstThreeGlyphs();
      synchronized(this.getClass()) {
         ++uniqueCounter;
         if (uniqueCounter > 99999 || uniqueCounter < 0) {
            uniqueCounter = 0;
         }
      }

      DecimalFormat counterFormat = new DecimalFormat("00000");
      String cntString = counterFormat.format((long)uniqueCounter);
      StringBuffer sb = new StringBuffer("E");
      int i = 0;

      for(int c = cntString.length(); i < c; ++i) {
         sb.append((char)(cntString.charAt(i) + 17));
      }

      sb.append("+");
      this.namePrefix = sb.toString();
      this.setFontType(FontType.TYPE0);
   }

   public int getDefaultWidth() {
      return this.defaultWidth;
   }

   public String getRegistry() {
      return "Adobe";
   }

   public String getOrdering() {
      return "UCS";
   }

   public int getSupplement() {
      return 0;
   }

   public CIDFontType getCIDType() {
      return this.cidType;
   }

   public void setCIDType(CIDFontType cidType) {
      this.cidType = cidType;
   }

   private String getPrefixedFontName() {
      return this.namePrefix + FontUtil.stripWhiteSpace(super.getFontName());
   }

   public String getEmbedFontName() {
      return this.isEmbeddable() ? this.getPrefixedFontName() : super.getFontName();
   }

   public boolean isEmbeddable() {
      return this.getEmbedFileName() != null || this.getEmbedResourceName() != null;
   }

   public CIDSubset getCIDSubset() {
      return this.subset;
   }

   public String getEncodingName() {
      return this.encoding;
   }

   public int getWidth(int i, int size) {
      if (this.isEmbeddable()) {
         int glyphIndex = this.subset.getGlyphIndexForSubsetIndex(i);
         return size * this.width[glyphIndex];
      } else {
         return size * this.width[i];
      }
   }

   public int[] getWidths() {
      int[] arr = new int[this.width.length];
      System.arraycopy(this.width, 0, arr, 0, this.width.length);
      return arr;
   }

   private int findGlyphIndex(char c) {
      int idx = c;
      int retIdx = 0;

      for(int i = 0; i < this.bfentries.length && retIdx == 0; ++i) {
         if (this.bfentries[i].getUnicodeStart() <= idx && this.bfentries[i].getUnicodeEnd() >= idx) {
            retIdx = this.bfentries[i].getGlyphStartIndex() + idx - this.bfentries[i].getUnicodeStart();
         }
      }

      return retIdx;
   }

   public char mapChar(char c) {
      this.notifyMapOperation();
      int glyphIndex = this.findGlyphIndex(c);
      if (glyphIndex == 0) {
         this.warnMissingGlyph(c);
         glyphIndex = this.findGlyphIndex('#');
      }

      if (this.isEmbeddable()) {
         glyphIndex = this.subset.mapSubsetChar(glyphIndex, c);
      }

      return (char)glyphIndex;
   }

   public boolean hasChar(char c) {
      return this.findGlyphIndex(c) != 0;
   }

   public void setBFEntries(BFEntry[] entries) {
      this.bfentries = entries;
   }

   public void setDefaultWidth(int defaultWidth) {
      this.defaultWidth = defaultWidth;
   }

   public String getTTCName() {
      return this.ttcName;
   }

   public void setTTCName(String ttcName) {
      this.ttcName = ttcName;
   }

   public void setWidthArray(int[] wds) {
      this.width = wds;
   }

   public Map getUsedGlyphs() {
      return this.subset.getSubsetGlyphs();
   }

   public char[] getCharsUsed() {
      return !this.isEmbeddable() ? null : this.subset.getSubsetChars();
   }
}

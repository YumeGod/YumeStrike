package org.apache.fop.fonts;

import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CIDSubset {
   private Map usedGlyphs = new HashMap();
   private Map usedGlyphsIndex = new HashMap();
   private int usedGlyphsCount = 0;
   private Map usedCharsIndex = new HashMap();

   public void setupFirstThreeGlyphs() {
      this.usedGlyphs.put(new Integer(0), new Integer(0));
      this.usedGlyphsIndex.put(new Integer(0), new Integer(0));
      ++this.usedGlyphsCount;
      this.usedGlyphs.put(new Integer(1), new Integer(1));
      this.usedGlyphsIndex.put(new Integer(1), new Integer(1));
      ++this.usedGlyphsCount;
      this.usedGlyphs.put(new Integer(2), new Integer(2));
      this.usedGlyphsIndex.put(new Integer(2), new Integer(2));
      ++this.usedGlyphsCount;
   }

   public int getGlyphIndexForSubsetIndex(int subsetIndex) {
      Integer glyphIndex = (Integer)this.usedGlyphsIndex.get(new Integer(subsetIndex));
      return glyphIndex != null ? glyphIndex : -1;
   }

   public char getUnicodeForSubsetIndex(int subsetIndex) {
      Character mapValue = (Character)this.usedCharsIndex.get(new Integer(subsetIndex));
      return mapValue != null ? mapValue : '\uffff';
   }

   public int mapSubsetChar(int glyphIndex, char unicode) {
      Integer subsetCharSelector = (Integer)this.usedGlyphs.get(new Integer(glyphIndex));
      if (subsetCharSelector == null) {
         int selector = this.usedGlyphsCount;
         this.usedGlyphs.put(new Integer(glyphIndex), new Integer(selector));
         this.usedGlyphsIndex.put(new Integer(selector), new Integer(glyphIndex));
         this.usedCharsIndex.put(new Integer(selector), new Character(unicode));
         ++this.usedGlyphsCount;
         return selector;
      } else {
         return subsetCharSelector;
      }
   }

   public Map getSubsetGlyphs() {
      return Collections.unmodifiableMap(this.usedGlyphs);
   }

   public char[] getSubsetChars() {
      char[] charArray = new char[this.usedGlyphsCount];

      for(int i = 0; i < this.usedGlyphsCount; ++i) {
         charArray[i] = this.getUnicodeForSubsetIndex(i);
      }

      return charArray;
   }

   public int getSubsetSize() {
      return this.usedGlyphsCount;
   }

   public BitSet getGlyphIndexBitSet() {
      BitSet bitset = new BitSet();
      Iterator iter = this.usedGlyphsIndex.keySet().iterator();

      while(iter.hasNext()) {
         Integer cid = (Integer)iter.next();
         bitset.set(cid);
      }

      return bitset;
   }
}

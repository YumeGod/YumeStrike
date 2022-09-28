package org.apache.fop.fonts.truetype;

import java.util.ArrayList;
import java.util.List;

class TTFMtxEntry {
   private int wx;
   private int lsb;
   private String name = "";
   private int index;
   private List unicodeIndex = new ArrayList();
   private int[] boundingBox = new int[4];
   private long offset;
   private byte found = 0;

   public String toString(TTFFile t) {
      return "Glyph " + this.name + " index: " + this.getIndexAsString() + " bbox [" + t.convertTTFUnit2PDFUnit(this.boundingBox[0]) + " " + t.convertTTFUnit2PDFUnit(this.boundingBox[1]) + " " + t.convertTTFUnit2PDFUnit(this.boundingBox[2]) + " " + t.convertTTFUnit2PDFUnit(this.boundingBox[3]) + "] wx: " + t.convertTTFUnit2PDFUnit(this.wx);
   }

   public int[] getBoundingBox() {
      return this.boundingBox;
   }

   public void setBoundingBox(int[] boundingBox) {
      this.boundingBox = boundingBox;
   }

   public byte getFound() {
      return this.found;
   }

   public int getIndex() {
      return this.index;
   }

   public boolean isIndexReserved() {
      return this.getIndex() >= 32768 && this.getIndex() <= 65535;
   }

   public String getIndexAsString() {
      return this.isIndexReserved() ? Integer.toString(this.getIndex()) + " (reserved)" : Integer.toString(this.getIndex());
   }

   public int getLsb() {
      return this.lsb;
   }

   public String getName() {
      return this.name;
   }

   public long getOffset() {
      return this.offset;
   }

   public List getUnicodeIndex() {
      return this.unicodeIndex;
   }

   public int getWx() {
      return this.wx;
   }

   public void setFound(byte found) {
      this.found = found;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public void setLsb(int lsb) {
      this.lsb = lsb;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setOffset(long offset) {
      this.offset = offset;
   }

   public void setWx(int wx) {
      this.wx = wx;
   }
}

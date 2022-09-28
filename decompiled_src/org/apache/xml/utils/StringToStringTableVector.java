package org.apache.xml.utils;

public class StringToStringTableVector {
   private int m_blocksize;
   private StringToStringTable[] m_map;
   private int m_firstFree = 0;
   private int m_mapSize;

   public StringToStringTableVector() {
      this.m_blocksize = 8;
      this.m_mapSize = this.m_blocksize;
      this.m_map = new StringToStringTable[this.m_blocksize];
   }

   public StringToStringTableVector(int blocksize) {
      this.m_blocksize = blocksize;
      this.m_mapSize = blocksize;
      this.m_map = new StringToStringTable[blocksize];
   }

   public final int getLength() {
      return this.m_firstFree;
   }

   public final int size() {
      return this.m_firstFree;
   }

   public final void addElement(StringToStringTable value) {
      if (this.m_firstFree + 1 >= this.m_mapSize) {
         this.m_mapSize += this.m_blocksize;
         StringToStringTable[] newMap = new StringToStringTable[this.m_mapSize];
         System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
         this.m_map = newMap;
      }

      this.m_map[this.m_firstFree] = value;
      ++this.m_firstFree;
   }

   public final String get(String key) {
      for(int i = this.m_firstFree - 1; i >= 0; --i) {
         String nsuri = this.m_map[i].get(key);
         if (nsuri != null) {
            return nsuri;
         }
      }

      return null;
   }

   public final boolean containsKey(String key) {
      for(int i = this.m_firstFree - 1; i >= 0; --i) {
         if (this.m_map[i].get(key) != null) {
            return true;
         }
      }

      return false;
   }

   public final void removeLastElem() {
      if (this.m_firstFree > 0) {
         this.m_map[this.m_firstFree] = null;
         --this.m_firstFree;
      }

   }

   public final StringToStringTable elementAt(int i) {
      return this.m_map[i];
   }

   public final boolean contains(StringToStringTable s) {
      for(int i = 0; i < this.m_firstFree; ++i) {
         if (this.m_map[i].equals(s)) {
            return true;
         }
      }

      return false;
   }
}

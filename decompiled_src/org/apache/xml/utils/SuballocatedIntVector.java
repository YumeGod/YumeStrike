package org.apache.xml.utils;

public class SuballocatedIntVector {
   protected int m_blocksize;
   protected int m_SHIFT;
   protected int m_MASK;
   protected static final int NUMBLOCKS_DEFAULT = 32;
   protected int m_numblocks;
   protected int[][] m_map;
   protected int m_firstFree;
   protected int[] m_map0;
   protected int[] m_buildCache;
   protected int m_buildCacheStartIndex;

   public SuballocatedIntVector() {
      this(2048);
   }

   public SuballocatedIntVector(int blocksize, int numblocks) {
      this.m_numblocks = 32;
      this.m_firstFree = 0;

      for(this.m_SHIFT = 0; 0 != (blocksize >>>= 1); ++this.m_SHIFT) {
      }

      this.m_blocksize = 1 << this.m_SHIFT;
      this.m_MASK = this.m_blocksize - 1;
      this.m_numblocks = numblocks;
      this.m_map0 = new int[this.m_blocksize];
      this.m_map = new int[numblocks][];
      this.m_map[0] = this.m_map0;
      this.m_buildCache = this.m_map0;
      this.m_buildCacheStartIndex = 0;
   }

   public SuballocatedIntVector(int blocksize) {
      this(blocksize, 32);
   }

   public int size() {
      return this.m_firstFree;
   }

   public void setSize(int sz) {
      if (this.m_firstFree > sz) {
         this.m_firstFree = sz;
      }

   }

   public void addElement(int value) {
      int indexRelativeToCache = this.m_firstFree - this.m_buildCacheStartIndex;
      if (indexRelativeToCache >= 0 && indexRelativeToCache < this.m_blocksize) {
         this.m_buildCache[indexRelativeToCache] = value;
         ++this.m_firstFree;
      } else {
         int index = this.m_firstFree >>> this.m_SHIFT;
         int offset = this.m_firstFree & this.m_MASK;
         if (index >= this.m_map.length) {
            int newsize = index + this.m_numblocks;
            int[][] newMap = new int[newsize][];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
            this.m_map = newMap;
         }

         int[] block = this.m_map[index];
         if (null == block) {
            block = this.m_map[index] = new int[this.m_blocksize];
         }

         block[offset] = value;
         this.m_buildCache = block;
         this.m_buildCacheStartIndex = this.m_firstFree - offset;
         ++this.m_firstFree;
      }

   }

   private void addElements(int value, int numberOfElements) {
      if (this.m_firstFree + numberOfElements < this.m_blocksize) {
         for(int i = 0; i < numberOfElements; ++i) {
            this.m_map0[this.m_firstFree++] = value;
         }
      } else {
         int index = this.m_firstFree >>> this.m_SHIFT;
         int offset = this.m_firstFree & this.m_MASK;

         for(this.m_firstFree += numberOfElements; numberOfElements > 0; offset = 0) {
            if (index >= this.m_map.length) {
               int newsize = index + this.m_numblocks;
               int[][] newMap = new int[newsize][];
               System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
               this.m_map = newMap;
            }

            int[] block = this.m_map[index];
            if (null == block) {
               block = this.m_map[index] = new int[this.m_blocksize];
            }

            int copied = this.m_blocksize - offset < numberOfElements ? this.m_blocksize - offset : numberOfElements;

            for(numberOfElements -= copied; copied-- > 0; block[offset++] = value) {
            }

            ++index;
         }
      }

   }

   private void addElements(int numberOfElements) {
      int newlen = this.m_firstFree + numberOfElements;
      if (newlen > this.m_blocksize) {
         int index = this.m_firstFree >>> this.m_SHIFT;
         int newindex = this.m_firstFree + numberOfElements >>> this.m_SHIFT;

         for(int i = index + 1; i <= newindex; ++i) {
            this.m_map[i] = new int[this.m_blocksize];
         }
      }

      this.m_firstFree = newlen;
   }

   private void insertElementAt(int value, int at) {
      if (at == this.m_firstFree) {
         this.addElement(value);
      } else {
         int index;
         int newsize;
         int offset;
         if (at > this.m_firstFree) {
            index = at >>> this.m_SHIFT;
            if (index >= this.m_map.length) {
               newsize = index + this.m_numblocks;
               int[][] newMap = new int[newsize][];
               System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
               this.m_map = newMap;
            }

            int[] block = this.m_map[index];
            if (null == block) {
               block = this.m_map[index] = new int[this.m_blocksize];
            }

            offset = at & this.m_MASK;
            block[offset] = value;
            this.m_firstFree = offset + 1;
         } else {
            index = at >>> this.m_SHIFT;
            newsize = this.m_firstFree >>> this.m_SHIFT;
            ++this.m_firstFree;

            for(offset = at & this.m_MASK; index <= newsize; ++index) {
               int copylen = this.m_blocksize - offset - 1;
               int[] block = this.m_map[index];
               int push;
               if (null == block) {
                  push = 0;
                  block = this.m_map[index] = new int[this.m_blocksize];
               } else {
                  push = block[this.m_blocksize - 1];
                  System.arraycopy(block, offset, block, offset + 1, copylen);
               }

               block[offset] = value;
               value = push;
               offset = 0;
            }
         }
      }

   }

   public void removeAllElements() {
      this.m_firstFree = 0;
      this.m_buildCache = this.m_map0;
      this.m_buildCacheStartIndex = 0;
   }

   private boolean removeElement(int s) {
      int at = this.indexOf(s, 0);
      if (at < 0) {
         return false;
      } else {
         this.removeElementAt(at);
         return true;
      }
   }

   private void removeElementAt(int at) {
      if (at < this.m_firstFree) {
         int index = at >>> this.m_SHIFT;
         int maxindex = this.m_firstFree >>> this.m_SHIFT;

         for(int offset = at & this.m_MASK; index <= maxindex; ++index) {
            int copylen = this.m_blocksize - offset - 1;
            int[] block = this.m_map[index];
            if (null == block) {
               block = this.m_map[index] = new int[this.m_blocksize];
            } else {
               System.arraycopy(block, offset + 1, block, offset, copylen);
            }

            if (index < maxindex) {
               int[] next = this.m_map[index + 1];
               if (next != null) {
                  block[this.m_blocksize - 1] = next != null ? next[0] : 0;
               }
            } else {
               block[this.m_blocksize - 1] = 0;
            }

            offset = 0;
         }
      }

      --this.m_firstFree;
   }

   public void setElementAt(int value, int at) {
      if (at < this.m_blocksize) {
         this.m_map0[at] = value;
      } else {
         int index = at >>> this.m_SHIFT;
         int offset = at & this.m_MASK;
         if (index >= this.m_map.length) {
            int newsize = index + this.m_numblocks;
            int[][] newMap = new int[newsize][];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
            this.m_map = newMap;
         }

         int[] block = this.m_map[index];
         if (null == block) {
            block = this.m_map[index] = new int[this.m_blocksize];
         }

         block[offset] = value;
      }

      if (at >= this.m_firstFree) {
         this.m_firstFree = at + 1;
      }

   }

   public int elementAt(int i) {
      return i < this.m_blocksize ? this.m_map0[i] : this.m_map[i >>> this.m_SHIFT][i & this.m_MASK];
   }

   private boolean contains(int s) {
      return this.indexOf(s, 0) >= 0;
   }

   public int indexOf(int elem, int index) {
      if (index >= this.m_firstFree) {
         return -1;
      } else {
         int bindex = index >>> this.m_SHIFT;
         int boffset = index & this.m_MASK;

         int maxindex;
         int[] block;
         int offset;
         for(maxindex = this.m_firstFree >>> this.m_SHIFT; bindex < maxindex; ++bindex) {
            block = this.m_map[bindex];
            if (block != null) {
               for(offset = boffset; offset < this.m_blocksize; ++offset) {
                  if (block[offset] == elem) {
                     return offset + bindex * this.m_blocksize;
                  }
               }
            }

            boffset = 0;
         }

         offset = this.m_firstFree & this.m_MASK;
         block = this.m_map[maxindex];

         for(int offset = boffset; offset < offset; ++offset) {
            if (block[offset] == elem) {
               return offset + maxindex * this.m_blocksize;
            }
         }

         return -1;
      }
   }

   public int indexOf(int elem) {
      return this.indexOf(elem, 0);
   }

   private int lastIndexOf(int elem) {
      int boffset = this.m_firstFree & this.m_MASK;

      for(int index = this.m_firstFree >>> this.m_SHIFT; index >= 0; --index) {
         int[] block = this.m_map[index];
         if (block != null) {
            for(int offset = boffset; offset >= 0; --offset) {
               if (block[offset] == elem) {
                  return offset + index * this.m_blocksize;
               }
            }
         }

         boffset = 0;
      }

      return -1;
   }

   public final int[] getMap0() {
      return this.m_map0;
   }

   public final int[][] getMap() {
      return this.m_map;
   }
}

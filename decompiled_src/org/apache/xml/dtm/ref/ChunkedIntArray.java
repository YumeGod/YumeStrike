package org.apache.xml.dtm.ref;

import org.apache.xml.res.XMLMessages;

final class ChunkedIntArray {
   final int slotsize = 4;
   static final int lowbits = 10;
   static final int chunkalloc = 1024;
   static final int lowmask = 1023;
   ChunksVector chunks = new ChunksVector();
   final int[] fastArray = new int[1024];
   int lastUsed = 0;

   ChunkedIntArray(int slotsize) {
      if (4 < slotsize) {
         throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage("ER_CHUNKEDINTARRAY_NOT_SUPPORTED", new Object[]{Integer.toString(slotsize)}));
      } else {
         if (4 > slotsize) {
            System.out.println("*****WARNING: ChunkedIntArray(" + slotsize + ") wasting " + (4 - slotsize) + " words per slot");
         }

         this.chunks.addElement(this.fastArray);
      }
   }

   int appendSlot(int w0, int w1, int w2, int w3) {
      int slotsize = true;
      int newoffset = (this.lastUsed + 1) * 4;
      int chunkpos = newoffset >> 10;
      int slotpos = newoffset & 1023;
      if (chunkpos > this.chunks.size() - 1) {
         this.chunks.addElement(new int[1024]);
      }

      int[] chunk = this.chunks.elementAt(chunkpos);
      chunk[slotpos] = w0;
      chunk[slotpos + 1] = w1;
      chunk[slotpos + 2] = w2;
      chunk[slotpos + 3] = w3;
      return ++this.lastUsed;
   }

   int readEntry(int position, int offset) throws ArrayIndexOutOfBoundsException {
      if (offset >= 4) {
         throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage("ER_OFFSET_BIGGER_THAN_SLOT", (Object[])null));
      } else {
         position *= 4;
         int chunkpos = position >> 10;
         int slotpos = position & 1023;
         int[] chunk = this.chunks.elementAt(chunkpos);
         return chunk[slotpos + offset];
      }
   }

   int specialFind(int startPos, int position) {
      int ancestor = startPos;

      while(ancestor > 0) {
         ancestor *= 4;
         int chunkpos = ancestor >> 10;
         int slotpos = ancestor & 1023;
         int[] chunk = this.chunks.elementAt(chunkpos);
         ancestor = chunk[slotpos + 1];
         if (ancestor == position) {
            break;
         }
      }

      return ancestor <= 0 ? position : -1;
   }

   int slotsUsed() {
      return this.lastUsed;
   }

   void discardLast() {
      --this.lastUsed;
   }

   void writeEntry(int position, int offset, int value) throws ArrayIndexOutOfBoundsException {
      if (offset >= 4) {
         throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage("ER_OFFSET_BIGGER_THAN_SLOT", (Object[])null));
      } else {
         position *= 4;
         int chunkpos = position >> 10;
         int slotpos = position & 1023;
         int[] chunk = this.chunks.elementAt(chunkpos);
         chunk[slotpos + offset] = value;
      }
   }

   void writeSlot(int position, int w0, int w1, int w2, int w3) {
      position *= 4;
      int chunkpos = position >> 10;
      int slotpos = position & 1023;
      if (chunkpos > this.chunks.size() - 1) {
         this.chunks.addElement(new int[1024]);
      }

      int[] chunk = this.chunks.elementAt(chunkpos);
      chunk[slotpos] = w0;
      chunk[slotpos + 1] = w1;
      chunk[slotpos + 2] = w2;
      chunk[slotpos + 3] = w3;
   }

   void readSlot(int position, int[] buffer) {
      position *= 4;
      int chunkpos = position >> 10;
      int slotpos = position & 1023;
      if (chunkpos > this.chunks.size() - 1) {
         this.chunks.addElement(new int[1024]);
      }

      int[] chunk = this.chunks.elementAt(chunkpos);
      System.arraycopy(chunk, slotpos, buffer, 0, 4);
   }

   class ChunksVector {
      final int BLOCKSIZE = 64;
      int[][] m_map = new int[64][];
      int m_mapSize = 64;
      int pos = 0;

      final int size() {
         return this.pos;
      }

      void addElement(int[] value) {
         if (this.pos >= this.m_mapSize) {
            int orgMapSize;
            for(orgMapSize = this.m_mapSize; this.pos >= this.m_mapSize; this.m_mapSize += 64) {
            }

            int[][] newMap = new int[this.m_mapSize][];
            System.arraycopy(this.m_map, 0, newMap, 0, orgMapSize);
            this.m_map = newMap;
         }

         this.m_map[this.pos] = value;
         ++this.pos;
      }

      final int[] elementAt(int pos) {
         return this.m_map[pos];
      }
   }
}

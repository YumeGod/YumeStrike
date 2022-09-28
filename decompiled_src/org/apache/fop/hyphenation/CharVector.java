package org.apache.fop.hyphenation;

import java.io.Serializable;

public class CharVector implements Cloneable, Serializable {
   private static final int DEFAULT_BLOCK_SIZE = 2048;
   private int blockSize;
   private char[] array;
   private int n;

   public CharVector() {
      this(2048);
   }

   public CharVector(int capacity) {
      if (capacity > 0) {
         this.blockSize = capacity;
      } else {
         this.blockSize = 2048;
      }

      this.array = new char[this.blockSize];
      this.n = 0;
   }

   public CharVector(char[] a) {
      this.blockSize = 2048;
      this.array = a;
      this.n = a.length;
   }

   public CharVector(char[] a, int capacity) {
      if (capacity > 0) {
         this.blockSize = capacity;
      } else {
         this.blockSize = 2048;
      }

      this.array = a;
      this.n = a.length;
   }

   public void clear() {
      this.n = 0;
   }

   public Object clone() {
      CharVector cv = new CharVector((char[])this.array.clone(), this.blockSize);
      cv.n = this.n;
      return cv;
   }

   public char[] getArray() {
      return this.array;
   }

   public int length() {
      return this.n;
   }

   public int capacity() {
      return this.array.length;
   }

   public void put(int index, char val) {
      this.array[index] = val;
   }

   public char get(int index) {
      return this.array[index];
   }

   public int alloc(int size) {
      int index = this.n;
      int len = this.array.length;
      if (this.n + size >= len) {
         char[] aux = new char[len + this.blockSize];
         System.arraycopy(this.array, 0, aux, 0, len);
         this.array = aux;
      }

      this.n += size;
      return index;
   }

   public void trimToSize() {
      if (this.n < this.array.length) {
         char[] aux = new char[this.n];
         System.arraycopy(this.array, 0, aux, 0, this.n);
         this.array = aux;
      }

   }
}

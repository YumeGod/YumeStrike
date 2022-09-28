package org.apache.xml.utils;

import java.util.EmptyStackException;

public class IntStack extends IntVector {
   public IntStack() {
   }

   public IntStack(int blocksize) {
      super(blocksize);
   }

   public IntStack(IntStack v) {
      super(v);
   }

   public int push(int i) {
      if (super.m_firstFree + 1 >= super.m_mapSize) {
         super.m_mapSize += super.m_blocksize;
         int[] newMap = new int[super.m_mapSize];
         System.arraycopy(super.m_map, 0, newMap, 0, super.m_firstFree + 1);
         super.m_map = newMap;
      }

      super.m_map[super.m_firstFree] = i;
      ++super.m_firstFree;
      return i;
   }

   public final int pop() {
      return super.m_map[--super.m_firstFree];
   }

   public final void quickPop(int n) {
      super.m_firstFree -= n;
   }

   public final int peek() {
      try {
         return super.m_map[super.m_firstFree - 1];
      } catch (ArrayIndexOutOfBoundsException var2) {
         throw new EmptyStackException();
      }
   }

   public int peek(int n) {
      try {
         return super.m_map[super.m_firstFree - (1 + n)];
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new EmptyStackException();
      }
   }

   public void setTop(int val) {
      try {
         super.m_map[super.m_firstFree - 1] = val;
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new EmptyStackException();
      }
   }

   public boolean empty() {
      return super.m_firstFree == 0;
   }

   public int search(int o) {
      int i = this.lastIndexOf(o);
      return i >= 0 ? this.size() - i : -1;
   }

   public Object clone() throws CloneNotSupportedException {
      return (IntStack)super.clone();
   }
}

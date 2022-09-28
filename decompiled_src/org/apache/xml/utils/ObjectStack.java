package org.apache.xml.utils;

import java.util.EmptyStackException;

public class ObjectStack extends ObjectVector {
   public ObjectStack() {
   }

   public ObjectStack(int blocksize) {
      super(blocksize);
   }

   public ObjectStack(ObjectStack v) {
      super(v);
   }

   public Object push(Object i) {
      if (super.m_firstFree + 1 >= super.m_mapSize) {
         super.m_mapSize += super.m_blocksize;
         Object[] newMap = new Object[super.m_mapSize];
         System.arraycopy(super.m_map, 0, newMap, 0, super.m_firstFree + 1);
         super.m_map = newMap;
      }

      super.m_map[super.m_firstFree] = i;
      ++super.m_firstFree;
      return i;
   }

   public Object pop() {
      Object val = super.m_map[--super.m_firstFree];
      super.m_map[super.m_firstFree] = null;
      return val;
   }

   public void quickPop(int n) {
      super.m_firstFree -= n;
   }

   public Object peek() {
      try {
         return super.m_map[super.m_firstFree - 1];
      } catch (ArrayIndexOutOfBoundsException var2) {
         throw new EmptyStackException();
      }
   }

   public Object peek(int n) {
      try {
         return super.m_map[super.m_firstFree - (1 + n)];
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new EmptyStackException();
      }
   }

   public void setTop(Object val) {
      try {
         super.m_map[super.m_firstFree - 1] = val;
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new EmptyStackException();
      }
   }

   public boolean empty() {
      return super.m_firstFree == 0;
   }

   public int search(Object o) {
      int i = this.lastIndexOf(o);
      return i >= 0 ? this.size() - i : -1;
   }

   public Object clone() throws CloneNotSupportedException {
      return (ObjectStack)super.clone();
   }
}

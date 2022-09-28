package org.apache.xalan.lib.sql;

import java.util.Vector;

public class ObjectArray {
   private int m_minArraySize = 10;
   private Vector m_Arrays = new Vector(200);
   private _ObjectArray m_currentArray;
   private int m_nextSlot;

   public ObjectArray() {
      this.init(10);
   }

   public ObjectArray(int minArraySize) {
      this.init(minArraySize);
   }

   private void init(int size) {
      this.m_minArraySize = size;
      this.m_currentArray = new _ObjectArray(this.m_minArraySize);
   }

   public Object getAt(int idx) {
      int arrayIndx = idx / this.m_minArraySize;
      int arrayOffset = idx - arrayIndx * this.m_minArraySize;
      if (arrayIndx < this.m_Arrays.size()) {
         _ObjectArray a = (_ObjectArray)this.m_Arrays.elementAt(arrayIndx);
         return a.objects[arrayOffset];
      } else {
         return this.m_currentArray.objects[arrayOffset];
      }
   }

   public void setAt(int idx, Object obj) {
      int arrayIndx = idx / this.m_minArraySize;
      int arrayOffset = idx - arrayIndx * this.m_minArraySize;
      if (arrayIndx < this.m_Arrays.size()) {
         _ObjectArray a = (_ObjectArray)this.m_Arrays.elementAt(arrayIndx);
         a.objects[arrayOffset] = obj;
      } else {
         this.m_currentArray.objects[arrayOffset] = obj;
      }

   }

   public int append(Object o) {
      if (this.m_nextSlot >= this.m_minArraySize) {
         this.m_Arrays.addElement(this.m_currentArray);
         this.m_nextSlot = 0;
         this.m_currentArray = new _ObjectArray(this.m_minArraySize);
      }

      this.m_currentArray.objects[this.m_nextSlot] = o;
      int pos = this.m_Arrays.size() * this.m_minArraySize + this.m_nextSlot;
      ++this.m_nextSlot;
      return pos;
   }

   public static void main(String[] args) {
      String[] word = new String[]{"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Twenty-One", "Twenty-Two", "Twenty-Three", "Twenty-Four", "Twenty-Five", "Twenty-Six", "Twenty-Seven", "Twenty-Eight", "Twenty-Nine", "Thirty", "Thirty-One", "Thirty-Two", "Thirty-Three", "Thirty-Four", "Thirty-Five", "Thirty-Six", "Thirty-Seven", "Thirty-Eight", "Thirty-Nine"};
      ObjectArray m_ObjectArray = new ObjectArray();

      for(int x = 0; x < word.length; ++x) {
         System.out.print(" - " + m_ObjectArray.append(word[x]));
      }

      System.out.println("\n");

      for(int x = 0; x < word.length; ++x) {
         String s = (String)m_ObjectArray.getAt(x);
         System.out.println(s);
      }

      System.out.println((String)m_ObjectArray.getAt(5));
      System.out.println((String)m_ObjectArray.getAt(10));
      System.out.println((String)m_ObjectArray.getAt(20));
      System.out.println((String)m_ObjectArray.getAt(2));
      System.out.println((String)m_ObjectArray.getAt(15));
      System.out.println((String)m_ObjectArray.getAt(30));
      System.out.println((String)m_ObjectArray.getAt(6));
      System.out.println((String)m_ObjectArray.getAt(8));
      System.out.println((String)m_ObjectArray.getAt(40));
   }

   class _ObjectArray {
      public Object[] objects;

      public _ObjectArray(int size) {
         this.objects = new Object[size];
      }
   }
}

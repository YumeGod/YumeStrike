package org.apache.fop.fo;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class CharIterator implements Iterator, Cloneable {
   public abstract boolean hasNext();

   public abstract char nextChar() throws NoSuchElementException;

   public Object next() throws NoSuchElementException {
      return new Character(this.nextChar());
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public void replaceChar(char c) {
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }
}

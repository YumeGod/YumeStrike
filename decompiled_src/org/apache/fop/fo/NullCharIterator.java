package org.apache.fop.fo;

import java.util.NoSuchElementException;

public class NullCharIterator extends CharIterator {
   private static CharIterator instance;

   public static CharIterator getInstance() {
      if (instance == null) {
         instance = new NullCharIterator();
      }

      return instance;
   }

   public boolean hasNext() {
      return false;
   }

   public char nextChar() throws NoSuchElementException {
      throw new NoSuchElementException();
   }
}

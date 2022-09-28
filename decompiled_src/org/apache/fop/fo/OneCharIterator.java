package org.apache.fop.fo;

import java.util.NoSuchElementException;

public class OneCharIterator extends CharIterator {
   private boolean bFirst = true;
   private char charCode;

   public OneCharIterator(char c) {
      this.charCode = c;
   }

   public boolean hasNext() {
      return this.bFirst;
   }

   public char nextChar() throws NoSuchElementException {
      if (this.bFirst) {
         this.bFirst = false;
         return this.charCode;
      } else {
         throw new NoSuchElementException();
      }
   }
}

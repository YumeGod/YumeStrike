package org.apache.fop.fo;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RecursiveCharIterator extends CharIterator {
   private FONode fobj;
   private Iterator childIter = null;
   private FONode curChild;
   private CharIterator curCharIter = null;

   public RecursiveCharIterator(FObj fobj) {
      this.fobj = fobj;
      this.childIter = fobj.getChildNodes();
      this.getNextCharIter();
   }

   public RecursiveCharIterator(FObj fobj, FONode child) {
      this.fobj = fobj;
      this.childIter = fobj.getChildNodes(child);
      this.getNextCharIter();
   }

   public CharIterator mark() {
      return (CharIterator)this.clone();
   }

   public Object clone() {
      RecursiveCharIterator ci = (RecursiveCharIterator)super.clone();
      ci.childIter = this.fobj.getChildNodes(ci.curChild);
      ci.childIter.next();
      ci.curCharIter = (CharIterator)this.curCharIter.clone();
      return ci;
   }

   public void replaceChar(char c) {
      if (this.curCharIter != null) {
         this.curCharIter.replaceChar(c);
      }

   }

   private void getNextCharIter() {
      if (this.childIter != null && this.childIter.hasNext()) {
         this.curChild = (FONode)this.childIter.next();
         this.curCharIter = this.curChild.charIterator();
      } else {
         this.curChild = null;
         this.curCharIter = null;
      }

   }

   public boolean hasNext() {
      while(true) {
         if (this.curCharIter != null) {
            if (!this.curCharIter.hasNext()) {
               this.getNextCharIter();
               continue;
            }

            return true;
         }

         return false;
      }
   }

   public char nextChar() throws NoSuchElementException {
      if (this.curCharIter != null) {
         return this.curCharIter.nextChar();
      } else {
         throw new NoSuchElementException();
      }
   }

   public void remove() {
      if (this.curCharIter != null) {
         this.curCharIter.remove();
      }

   }
}

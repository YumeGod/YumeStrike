package org.apache.fop.fo;

import java.util.NoSuchElementException;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;

public class InlineCharIterator extends RecursiveCharIterator {
   private boolean startBoundary = false;
   private boolean endBoundary = false;

   public InlineCharIterator(FObj fobj, CommonBorderPaddingBackground bpb) {
      super(fobj);
      this.checkBoundaries(bpb);
   }

   private void checkBoundaries(CommonBorderPaddingBackground bpb) {
      this.startBoundary = true;
      this.endBoundary = true;
   }

   public boolean hasNext() {
      if (this.startBoundary) {
         return true;
      } else {
         return super.hasNext() || this.endBoundary;
      }
   }

   public char nextChar() throws NoSuchElementException {
      if (this.startBoundary) {
         this.startBoundary = false;
         return '\u0000';
      } else {
         try {
            return super.nextChar();
         } catch (NoSuchElementException var2) {
            if (this.endBoundary) {
               this.endBoundary = false;
               return '\u0000';
            } else {
               throw var2;
            }
         }
      }
   }
}

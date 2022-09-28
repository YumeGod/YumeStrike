package org.apache.fop.fo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.apache.fop.fo.flow.Block;
import org.apache.fop.util.CharUtilities;

public class XMLWhiteSpaceHandler {
   private boolean inWhiteSpace = false;
   private boolean afterLinefeed = true;
   private int nonWhiteSpaceCount;
   private int linefeedTreatment;
   private int whiteSpaceTreatment;
   private int whiteSpaceCollapse;
   private boolean endOfBlock;
   private boolean nextChildIsBlockLevel;
   private RecursiveCharIterator charIter;
   private List pendingInlines;
   private Stack nestedBlockStack = new Stack();
   private CharIterator firstWhiteSpaceInSeq;

   public void handleWhiteSpace(FObjMixed fo, FONode firstTextNode, FONode nextChild) {
      Block currentBlock = null;
      int foId = fo.getNameId();
      switch (foId) {
         case 3:
            currentBlock = (Block)fo;
            if (!this.nestedBlockStack.empty() && fo == this.nestedBlockStack.peek()) {
               if (nextChild == null) {
                  this.nestedBlockStack.pop();
               }
            } else if (nextChild != null) {
               this.nestedBlockStack.push(currentBlock);
            }
            break;
         case 64:
            FONode ancestor = fo;

            do {
               ancestor = ((FONode)ancestor).getParent();
            } while(((FONode)ancestor).getNameId() != 3 && ((FONode)ancestor).getNameId() != 70);

            if (((FONode)ancestor).getNameId() == 3) {
               currentBlock = (Block)ancestor;
               this.nestedBlockStack.push(currentBlock);
            }
            break;
         default:
            if (!this.nestedBlockStack.empty()) {
               currentBlock = (Block)this.nestedBlockStack.peek();
            }
      }

      if (currentBlock != null) {
         this.linefeedTreatment = currentBlock.getLinefeedTreatment();
         this.whiteSpaceCollapse = currentBlock.getWhitespaceCollapse();
         this.whiteSpaceTreatment = currentBlock.getWhitespaceTreatment();
      } else {
         this.linefeedTreatment = 147;
         this.whiteSpaceCollapse = 149;
         this.whiteSpaceTreatment = 63;
      }

      this.endOfBlock = nextChild == null && fo == currentBlock;
      if (firstTextNode == null) {
         this.afterLinefeed = fo == currentBlock && fo.firstChild == null;
         this.nonWhiteSpaceCount = 0;
         if (this.endOfBlock) {
            this.handlePendingInlines();
         }

      } else {
         this.charIter = new RecursiveCharIterator(fo, firstTextNode);
         this.inWhiteSpace = false;
         int nextChildId;
         if (fo == currentBlock || currentBlock == null || foId == 64 && fo.getParent() == currentBlock) {
            if (firstTextNode == fo.firstChild) {
               this.afterLinefeed = true;
            } else {
               nextChildId = firstTextNode.siblings[0].getNameId();
               this.afterLinefeed = nextChildId == 3 || nextChildId == 72 || nextChildId == 71 || nextChildId == 40 || nextChildId == 4;
            }
         }

         if (foId == 81) {
            FONode parent = fo.parent;

            int parentId;
            for(parentId = parent.getNameId(); parentId == 81; parentId = parent.getNameId()) {
               parent = parent.parent;
            }

            if (parentId == 16 || parentId == 70 || parentId == 4 || parentId == 75) {
               this.endOfBlock = nextChild == null;
            }
         }

         if (nextChild != null) {
            nextChildId = nextChild.getNameId();
            this.nextChildIsBlockLevel = nextChildId == 3 || nextChildId == 72 || nextChildId == 71 || nextChildId == 40 || nextChildId == 4;
         } else {
            this.nextChildIsBlockLevel = false;
         }

         this.handleWhiteSpace();
         if (fo == currentBlock && (this.endOfBlock || this.nextChildIsBlockLevel)) {
            this.handlePendingInlines();
         }

         if (nextChild == null) {
            if (fo != currentBlock) {
               if (this.nonWhiteSpaceCount > 0 && this.pendingInlines != null) {
                  this.pendingInlines.clear();
               }

               if (this.inWhiteSpace) {
                  this.addPendingInline(fo);
               }
            } else {
               if (!this.nestedBlockStack.empty()) {
                  this.nestedBlockStack.pop();
               }

               this.charIter = null;
               this.firstWhiteSpaceInSeq = null;
            }
         }

      }
   }

   protected final void reset() {
      if (this.pendingInlines != null) {
         this.pendingInlines.clear();
      }

      this.nestedBlockStack.clear();
      this.charIter = null;
      this.firstWhiteSpaceInSeq = null;
   }

   public void handleWhiteSpace(FObjMixed fo, FONode firstTextNode) {
      this.handleWhiteSpace(fo, firstTextNode, (FONode)null);
   }

   private void handleWhiteSpace() {
      // $FF: Couldn't be decompiled
   }

   private void addPendingInline(FObjMixed fo) {
      if (this.pendingInlines == null) {
         this.pendingInlines = new ArrayList(5);
      }

      this.pendingInlines.add(new PendingInline(fo, this.firstWhiteSpaceInSeq));
   }

   private void handlePendingInlines() {
      if (this.pendingInlines != null && !this.pendingInlines.isEmpty()) {
         if (this.nonWhiteSpaceCount == 0) {
            int i = this.pendingInlines.size();

            while(true) {
               --i;
               if (i < 0) {
                  break;
               }

               PendingInline p = (PendingInline)this.pendingInlines.get(i);
               this.charIter = (RecursiveCharIterator)p.firstTrailingWhiteSpace;
               this.handleWhiteSpace();
               this.pendingInlines.remove(p);
            }
         } else {
            this.pendingInlines.clear();
         }
      }

   }

   private class PendingInline {
      protected FObjMixed fo;
      protected CharIterator firstTrailingWhiteSpace;

      PendingInline(FObjMixed fo, CharIterator firstTrailingWhiteSpace) {
         this.fo = fo;
         this.firstTrailingWhiteSpace = firstTrailingWhiteSpace;
      }
   }

   private class EOLchecker {
      private boolean nextIsEOL = false;
      private RecursiveCharIterator charIter;

      EOLchecker(CharIterator charIter) {
         this.charIter = (RecursiveCharIterator)charIter;
      }

      boolean beforeLinefeed() {
         if (!this.nextIsEOL) {
            CharIterator lfIter = this.charIter.mark();

            while(lfIter.hasNext()) {
               int charClass = CharUtilities.classOf(lfIter.nextChar());
               if (charClass == 1) {
                  if (XMLWhiteSpaceHandler.this.linefeedTreatment == 108) {
                     this.nextIsEOL = true;
                     return this.nextIsEOL;
                  }
               } else if (charClass != 4) {
                  return this.nextIsEOL;
               }
            }

            this.nextIsEOL = XMLWhiteSpaceHandler.this.nextChildIsBlockLevel || XMLWhiteSpaceHandler.this.endOfBlock;
         }

         return this.nextIsEOL;
      }

      void reset() {
         this.nextIsEOL = false;
      }
   }
}

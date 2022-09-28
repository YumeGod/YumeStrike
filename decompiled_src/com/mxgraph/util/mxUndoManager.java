package com.mxgraph.util;

import java.util.ArrayList;
import java.util.List;

public class mxUndoManager extends mxEventSource {
   protected int size;
   protected List history;
   protected int indexOfNextAdd;

   public mxUndoManager() {
      this(100);
   }

   public mxUndoManager(int var1) {
      this.size = var1;
      this.clear();
   }

   public boolean isEmpty() {
      return this.history.isEmpty();
   }

   public void clear() {
      this.history = new ArrayList(this.size);
      this.indexOfNextAdd = 0;
      this.fireEvent(new mxEventObject("clear"));
   }

   public boolean canUndo() {
      return this.indexOfNextAdd > 0;
   }

   public void undo() {
      while(true) {
         if (this.indexOfNextAdd > 0) {
            mxUndoableEdit var1 = (mxUndoableEdit)this.history.get(--this.indexOfNextAdd);
            var1.undo();
            if (!var1.isSignificant()) {
               continue;
            }

            this.fireEvent(new mxEventObject("undo", new Object[]{"edit", var1}));
         }

         return;
      }
   }

   public boolean canRedo() {
      return this.indexOfNextAdd < this.history.size();
   }

   public void redo() {
      int var1 = this.history.size();

      while(this.indexOfNextAdd < var1) {
         mxUndoableEdit var2 = (mxUndoableEdit)this.history.get(this.indexOfNextAdd++);
         var2.redo();
         if (var2.isSignificant()) {
            this.fireEvent(new mxEventObject("redo", new Object[]{"edit", var2}));
            break;
         }
      }

   }

   public void undoableEditHappened(mxUndoableEdit var1) {
      this.trim();
      if (this.size > 0 && this.size == this.history.size()) {
         this.history.remove(0);
      }

      this.history.add(var1);
      this.indexOfNextAdd = this.history.size();
      this.fireEvent(new mxEventObject("add", new Object[]{"edit", var1}));
   }

   protected void trim() {
      while(this.history.size() > this.indexOfNextAdd) {
         mxUndoableEdit var1 = (mxUndoableEdit)this.history.remove(this.indexOfNextAdd);
         var1.die();
      }

   }
}

package com.mxgraph.util;

import java.util.ArrayList;
import java.util.List;

public class mxUndoableEdit {
   protected Object source;
   protected List changes;
   protected boolean significant;
   protected boolean undone;
   protected boolean redone;

   public mxUndoableEdit(Object var1) {
      this(var1, true);
   }

   public mxUndoableEdit(Object var1, boolean var2) {
      this.changes = new ArrayList();
      this.significant = true;
      this.source = var1;
      this.significant = var2;
   }

   public void dispatch() {
   }

   public void die() {
   }

   public Object getSource() {
      return this.source;
   }

   public List getChanges() {
      return this.changes;
   }

   public boolean isSignificant() {
      return this.significant;
   }

   public boolean isUndone() {
      return this.undone;
   }

   public boolean isRedone() {
      return this.redone;
   }

   public boolean isEmpty() {
      return this.changes.isEmpty();
   }

   public void add(mxUndoableChange var1) {
      this.changes.add(var1);
   }

   public void undo() {
      if (!this.undone) {
         int var1 = this.changes.size();

         for(int var2 = var1 - 1; var2 >= 0; --var2) {
            mxUndoableChange var3 = (mxUndoableChange)this.changes.get(var2);
            var3.execute();
         }

         this.undone = true;
         this.redone = false;
      }

      this.dispatch();
   }

   public void redo() {
      if (!this.redone) {
         int var1 = this.changes.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            mxUndoableChange var3 = (mxUndoableChange)this.changes.get(var2);
            var3.execute();
         }

         this.undone = false;
         this.redone = true;
      }

      this.dispatch();
   }

   public interface mxUndoableChange {
      void execute();
   }
}

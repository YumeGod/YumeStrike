package com.mxgraph.view;

import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxUndoableEdit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class mxGraphSelectionModel extends mxEventSource {
   protected mxGraph graph;
   protected boolean singleSelection = false;
   protected Set cells = new LinkedHashSet();

   public mxGraphSelectionModel(mxGraph var1) {
      this.graph = var1;
   }

   public boolean isSingleSelection() {
      return this.singleSelection;
   }

   public void setSingleSelection(boolean var1) {
      this.singleSelection = var1;
   }

   public boolean isSelected(Object var1) {
      return var1 == null ? false : this.cells.contains(var1);
   }

   public boolean isEmpty() {
      return this.cells.isEmpty();
   }

   public int size() {
      return this.cells.size();
   }

   public void clear() {
      this.changeSelection((Collection)null, this.cells);
   }

   public Object getCell() {
      return this.cells.isEmpty() ? null : this.cells.iterator().next();
   }

   public Object[] getCells() {
      return this.cells.toArray();
   }

   public void setCell(Object var1) {
      if (var1 != null) {
         this.setCells(new Object[]{var1});
      } else {
         this.clear();
      }

   }

   public void setCells(Object[] var1) {
      if (var1 != null) {
         if (this.singleSelection) {
            var1 = new Object[]{this.getFirstSelectableCell(var1)};
         }

         ArrayList var2 = new ArrayList(var1.length);

         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (this.graph.isCellSelectable(var1[var3])) {
               var2.add(var1[var3]);
            }
         }

         this.changeSelection(var2, this.cells);
      } else {
         this.clear();
      }

   }

   protected Object getFirstSelectableCell(Object[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (this.graph.isCellSelectable(var1[var2])) {
               return var1[var2];
            }
         }
      }

      return null;
   }

   public void addCell(Object var1) {
      if (var1 != null) {
         this.addCells(new Object[]{var1});
      }

   }

   public void addCells(Object[] var1) {
      if (var1 != null) {
         Set var2 = null;
         if (this.singleSelection) {
            var2 = this.cells;
            var1 = new Object[]{this.getFirstSelectableCell(var1)};
         }

         ArrayList var3 = new ArrayList(var1.length);

         for(int var4 = 0; var4 < var1.length; ++var4) {
            if (!this.isSelected(var1[var4]) && this.graph.isCellSelectable(var1[var4])) {
               var3.add(var1[var4]);
            }
         }

         this.changeSelection(var3, var2);
      }

   }

   public void removeCell(Object var1) {
      if (var1 != null) {
         this.removeCells(new Object[]{var1});
      }

   }

   public void removeCells(Object[] var1) {
      if (var1 != null) {
         ArrayList var2 = new ArrayList(var1.length);

         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (this.isSelected(var1[var3])) {
               var2.add(var1[var3]);
            }
         }

         this.changeSelection((Collection)null, var2);
      }

   }

   protected void changeSelection(Collection var1, Collection var2) {
      if (var1 != null && !var1.isEmpty() || var2 != null && !var2.isEmpty()) {
         mxSelectionChange var3 = new mxSelectionChange(this, var1, var2);
         var3.execute();
         mxUndoableEdit var4 = new mxUndoableEdit(this, false);
         var4.add(var3);
         this.fireEvent(new mxEventObject("undo", new Object[]{"edit", var4}));
      }

   }

   protected void cellAdded(Object var1) {
      if (var1 != null) {
         this.cells.add(var1);
      }

   }

   protected void cellRemoved(Object var1) {
      if (var1 != null) {
         this.cells.remove(var1);
      }

   }

   public static class mxSelectionChange implements mxUndoableEdit.mxUndoableChange {
      protected mxGraphSelectionModel model;
      protected Collection added;
      protected Collection removed;

      public mxSelectionChange(mxGraphSelectionModel var1, Collection var2, Collection var3) {
         this.model = var1;
         this.added = var2 != null ? new ArrayList(var2) : null;
         this.removed = var3 != null ? new ArrayList(var3) : null;
      }

      public void execute() {
         Iterator var1;
         if (this.removed != null) {
            var1 = this.removed.iterator();

            while(var1.hasNext()) {
               this.model.cellRemoved(var1.next());
            }
         }

         if (this.added != null) {
            var1 = this.added.iterator();

            while(var1.hasNext()) {
               this.model.cellAdded(var1.next());
            }
         }

         Collection var2 = this.added;
         this.added = this.removed;
         this.removed = var2;
         this.model.fireEvent(new mxEventObject("change", new Object[]{"added", this.added, "removed", this.removed}));
      }
   }
}

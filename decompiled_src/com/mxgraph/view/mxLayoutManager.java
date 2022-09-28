package com.mxgraph.view;

import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class mxLayoutManager extends mxEventSource {
   protected mxGraph graph;
   protected boolean enabled = true;
   protected boolean bubbling = true;
   protected mxEventSource.mxIEventListener undoHandler = new mxEventSource.mxIEventListener() {
      public void invoke(Object var1, mxEventObject var2) {
         if (mxLayoutManager.this.isEnabled()) {
            mxLayoutManager.this.beforeUndo((mxUndoableEdit)var2.getProperty("edit"));
         }

      }
   };
   protected mxEventSource.mxIEventListener moveHandler = new mxEventSource.mxIEventListener() {
      public void invoke(Object var1, mxEventObject var2) {
         if (mxLayoutManager.this.isEnabled()) {
            mxLayoutManager.this.cellsMoved((Object[])((Object[])var2.getProperty("cells")), (Point)var2.getProperty("location"));
         }

      }
   };

   public mxLayoutManager(mxGraph var1) {
      this.setGraph(var1);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public boolean isBubbling() {
      return this.bubbling;
   }

   public void setBubbling(boolean var1) {
      this.bubbling = var1;
   }

   public mxGraph getGraph() {
      return this.graph;
   }

   public void setGraph(mxGraph var1) {
      mxIGraphModel var2;
      if (this.graph != null) {
         var2 = this.graph.getModel();
         var2.removeListener(this.undoHandler);
         this.graph.removeListener(this.moveHandler);
      }

      this.graph = var1;
      if (this.graph != null) {
         var2 = this.graph.getModel();
         var2.addListener("beforeUndo", this.undoHandler);
         this.graph.addListener("moveCells", this.moveHandler);
      }

   }

   protected mxIGraphLayout getLayout(Object var1) {
      return null;
   }

   protected void cellsMoved(Object[] var1, Point var2) {
      if (var1 != null && var2 != null) {
         mxIGraphModel var3 = this.getGraph().getModel();

         for(int var4 = 0; var4 < var1.length; ++var4) {
            mxIGraphLayout var5 = this.getLayout(var3.getParent(var1[var4]));
            if (var5 != null) {
               var5.moveCell(var1[var4], (double)var2.x, (double)var2.y);
            }
         }
      }

   }

   protected void beforeUndo(mxUndoableEdit var1) {
      Collection var2 = this.getCellsForChanges(var1.getChanges());
      mxIGraphModel var3 = this.getGraph().getModel();
      if (this.isBubbling()) {
         for(Object[] var4 = mxGraphModel.getParents(var3, var2.toArray()); var4.length > 0; var4 = mxGraphModel.getParents(var3, var4)) {
            var2.addAll(Arrays.asList(var4));
         }
      }

      this.layoutCells(mxUtils.sortCells(var2, false).toArray());
   }

   protected Collection getCellsForChanges(List var1) {
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         mxUndoableEdit.mxUndoableChange var4 = (mxUndoableEdit.mxUndoableChange)var3.next();
         if (var4 instanceof mxGraphModel.mxRootChange) {
            return new HashSet();
         }

         var2.addAll(this.getCellsForChange(var4));
      }

      return var2;
   }

   protected Collection getCellsForChange(mxUndoableEdit.mxUndoableChange var1) {
      mxIGraphModel var2 = this.getGraph().getModel();
      HashSet var3 = new HashSet();
      Object var5;
      if (var1 instanceof mxGraphModel.mxChildChange) {
         mxGraphModel.mxChildChange var4 = (mxGraphModel.mxChildChange)var1;
         var5 = var2.getParent(var4.getChild());
         if (var4.getChild() != null) {
            var3.add(var4.getChild());
         }

         if (var5 != null) {
            var3.add(var5);
         }

         if (var4.getPrevious() != null) {
            var3.add(var4.getPrevious());
         }
      } else if (var1 instanceof mxGraphModel.mxTerminalChange || var1 instanceof mxGraphModel.mxGeometryChange) {
         Object var6 = var1 instanceof mxGraphModel.mxTerminalChange ? ((mxGraphModel.mxTerminalChange)var1).getCell() : ((mxGraphModel.mxGeometryChange)var1).getCell();
         if (var6 != null) {
            var3.add(var6);
            var5 = var2.getParent(var6);
            if (var5 != null) {
               var3.add(var5);
            }
         }
      }

      return var3;
   }

   protected void layoutCells(Object[] var1) {
      if (var1.length > 0) {
         mxIGraphModel var2 = this.getGraph().getModel();
         var2.beginUpdate();

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var1[var3] != var2.getRoot()) {
                  this.executeLayout(this.getLayout(var1[var3]), var1[var3]);
               }
            }

            this.fireEvent(new mxEventObject(mxEvent.LAYOUT_CELLS, new Object[]{"cells", var1}));
         } finally {
            var2.endUpdate();
         }
      }

   }

   protected void executeLayout(mxIGraphLayout var1, Object var2) {
      if (var1 != null && var2 != null) {
         var1.execute(var2);
      }

   }

   public void destroy() {
      this.setGraph((mxGraph)null);
   }
}

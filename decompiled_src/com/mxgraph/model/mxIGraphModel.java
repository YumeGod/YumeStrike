package com.mxgraph.model;

import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxUndoableEdit;

public interface mxIGraphModel {
   Object getRoot();

   Object setRoot(Object var1);

   Object[] cloneCells(Object[] var1, boolean var2);

   boolean isAncestor(Object var1, Object var2);

   boolean contains(Object var1);

   Object getParent(Object var1);

   Object add(Object var1, Object var2, int var3);

   Object remove(Object var1);

   int getChildCount(Object var1);

   Object getChildAt(Object var1, int var2);

   Object getTerminal(Object var1, boolean var2);

   Object setTerminal(Object var1, Object var2, boolean var3);

   int getEdgeCount(Object var1);

   Object getEdgeAt(Object var1, int var2);

   boolean isVertex(Object var1);

   boolean isEdge(Object var1);

   boolean isConnectable(Object var1);

   Object getValue(Object var1);

   Object setValue(Object var1, Object var2);

   mxGeometry getGeometry(Object var1);

   mxGeometry setGeometry(Object var1, mxGeometry var2);

   String getStyle(Object var1);

   String setStyle(Object var1, String var2);

   boolean isCollapsed(Object var1);

   boolean setCollapsed(Object var1, boolean var2);

   boolean isVisible(Object var1);

   boolean setVisible(Object var1, boolean var2);

   void beginUpdate();

   void endUpdate();

   void addListener(String var1, mxEventSource.mxIEventListener var2);

   void removeListener(mxEventSource.mxIEventListener var1);

   void removeListener(mxEventSource.mxIEventListener var1, String var2);

   public abstract static class mxAtomicGraphModelChange implements mxUndoableEdit.mxUndoableChange {
      protected mxIGraphModel model;

      public mxAtomicGraphModelChange() {
         this((mxIGraphModel)null);
      }

      public mxAtomicGraphModelChange(mxIGraphModel var1) {
         this.model = var1;
      }

      public mxIGraphModel getModel() {
         return this.model;
      }

      public void setModel(mxIGraphModel var1) {
         this.model = var1;
      }

      public abstract void execute();
   }
}

package com.mxgraph.view;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxRectangle;

public class mxSwimlaneManager extends mxEventSource {
   protected mxGraph graph;
   protected boolean enabled;
   protected boolean horizontal;
   protected boolean siblings;
   protected boolean bubbling;
   protected mxEventSource.mxIEventListener addHandler = new mxEventSource.mxIEventListener() {
      public void invoke(Object var1, mxEventObject var2) {
         if (mxSwimlaneManager.this.isEnabled()) {
            mxSwimlaneManager.this.cellsAdded((Object[])((Object[])var2.getProperty("cells")));
         }

      }
   };
   protected mxEventSource.mxIEventListener resizeHandler = new mxEventSource.mxIEventListener() {
      public void invoke(Object var1, mxEventObject var2) {
         if (mxSwimlaneManager.this.isEnabled()) {
            mxSwimlaneManager.this.cellsResized((Object[])((Object[])var2.getProperty("cells")));
         }

      }
   };

   public mxSwimlaneManager(mxGraph var1) {
      this.setGraph(var1);
   }

   public boolean isSwimlaneIgnored(Object var1) {
      return !this.getGraph().isSwimlane(var1);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public boolean isHorizontal() {
      return this.horizontal;
   }

   public void setHorizontal(boolean var1) {
      this.horizontal = var1;
   }

   public boolean isSiblings() {
      return this.siblings;
   }

   public void setSiblings(boolean var1) {
      this.siblings = var1;
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
      if (this.graph != null) {
         this.graph.removeListener(this.addHandler);
         this.graph.removeListener(this.resizeHandler);
      }

      this.graph = var1;
      if (this.graph != null) {
         this.graph.addListener("addCells", this.addHandler);
         this.graph.addListener("cellsResized", this.resizeHandler);
      }

   }

   protected void cellsAdded(Object[] var1) {
      if (var1 != null) {
         mxIGraphModel var2 = this.getGraph().getModel();
         var2.beginUpdate();

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (!this.isSwimlaneIgnored(var1[var3])) {
                  this.swimlaneAdded(var1[var3]);
               }
            }
         } finally {
            var2.endUpdate();
         }
      }

   }

   protected void swimlaneAdded(Object var1) {
      mxIGraphModel var2 = this.getGraph().getModel();
      mxGeometry var3 = null;
      Object var4 = var2.getParent(var1);
      int var5 = var2.getChildCount(var4);

      for(int var6 = 0; var6 < var5; ++var6) {
         Object var7 = var2.getChildAt(var4, var6);
         if (var7 != var1 && !this.isSwimlaneIgnored(var7)) {
            var3 = var2.getGeometry(var7);
            break;
         }
      }

      if (var3 != null) {
         var2.beginUpdate();

         try {
            this.resizeSwimlane(var1, var3.getWidth(), var3.getHeight());
         } finally {
            var2.endUpdate();
         }
      }

   }

   protected void cellsResized(Object[] var1) {
      if (var1 != null) {
         mxIGraphModel var2 = this.getGraph().getModel();
         var2.beginUpdate();

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (!this.isSwimlaneIgnored(var1[var3])) {
                  this.swimlaneResized(var1[var3]);
               }
            }
         } finally {
            var2.endUpdate();
         }
      }

   }

   protected void swimlaneResized(Object var1) {
      mxIGraphModel var2 = this.getGraph().getModel();
      mxGeometry var3 = var2.getGeometry(var1);
      if (var3 != null) {
         double var4 = var3.getWidth();
         double var6 = var3.getHeight();
         var2.beginUpdate();

         try {
            Object var8 = var2.getParent(var1);
            if (this.isSiblings()) {
               int var9 = var2.getChildCount(var8);

               for(int var10 = 0; var10 < var9; ++var10) {
                  Object var11 = var2.getChildAt(var8, var10);
                  if (var11 != var1 && !this.isSwimlaneIgnored(var11)) {
                     this.resizeSwimlane(var11, var4, var6);
                  }
               }
            }

            if (this.isBubbling() && !this.isSwimlaneIgnored(var8)) {
               this.resizeParent(var8, var4, var6);
               this.swimlaneResized(var8);
            }
         } finally {
            var2.endUpdate();
         }
      }

   }

   protected void resizeSwimlane(Object var1, double var2, double var4) {
      mxIGraphModel var6 = this.getGraph().getModel();
      mxGeometry var7 = var6.getGeometry(var1);
      if (var7 != null) {
         var7 = (mxGeometry)var7.clone();
         if (this.isHorizontal()) {
            var7.setWidth(var2);
         } else {
            var7.setHeight(var4);
         }

         var6.setGeometry(var1, var7);
      }

   }

   protected void resizeParent(Object var1, double var2, double var4) {
      mxIGraphModel var6 = this.getGraph().getModel();
      mxGeometry var7 = var6.getGeometry(var1);
      if (var7 != null) {
         var7 = (mxGeometry)var7.clone();
         mxRectangle var8 = this.graph.getStartSize(var1);
         if (this.isHorizontal()) {
            var7.setWidth(var2 + var8.getWidth());
         } else {
            var7.setHeight(var4 + var8.getHeight());
         }

         var6.setGeometry(var1, var7);
      }

   }

   public void destroy() {
      this.setGraph((mxGraph)null);
   }
}

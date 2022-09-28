package com.mxgraph.view;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;

public class mxSpaceManager extends mxEventSource {
   protected mxGraph graph;
   protected boolean enabled;
   protected boolean shiftRightwards;
   protected boolean shiftDownwards;
   protected boolean extendParents;
   protected mxEventSource.mxIEventListener resizeHandler = new mxEventSource.mxIEventListener() {
      public void invoke(Object var1, mxEventObject var2) {
         if (mxSpaceManager.this.isEnabled()) {
            mxSpaceManager.this.cellsResized((Object[])((Object[])var2.getProperty("cells")));
         }

      }
   };

   public mxSpaceManager(mxGraph var1) {
      this.setGraph(var1);
   }

   public boolean isCellIgnored(Object var1) {
      return !this.getGraph().getModel().isVertex(var1);
   }

   public boolean isCellShiftable(Object var1) {
      return this.getGraph().getModel().isVertex(var1) && this.getGraph().isCellMovable(var1);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public boolean isShiftRightwards() {
      return this.shiftRightwards;
   }

   public void setShiftRightwards(boolean var1) {
      this.shiftRightwards = var1;
   }

   public boolean isShiftDownwards() {
      return this.shiftDownwards;
   }

   public void setShiftDownwards(boolean var1) {
      this.shiftDownwards = var1;
   }

   public boolean isExtendParents() {
      return this.extendParents;
   }

   public void setExtendParents(boolean var1) {
      this.extendParents = var1;
   }

   public mxGraph getGraph() {
      return this.graph;
   }

   public void setGraph(mxGraph var1) {
      if (this.graph != null) {
         this.graph.removeListener(this.resizeHandler);
      }

      this.graph = var1;
      if (this.graph != null) {
         this.graph.addListener("resizeCells", this.resizeHandler);
         this.graph.addListener("foldCells", this.resizeHandler);
      }

   }

   protected void cellsResized(Object[] var1) {
      if (var1 != null) {
         mxIGraphModel var2 = this.getGraph().getModel();
         var2.beginUpdate();

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (!this.isCellIgnored(var1[var3])) {
                  this.cellResized(var1[var3]);
                  break;
               }
            }
         } finally {
            var2.endUpdate();
         }
      }

   }

   protected void cellResized(Object var1) {
      mxGraph var2 = this.getGraph();
      mxGraphView var3 = var2.getView();
      mxIGraphModel var4 = var2.getModel();
      mxCellState var5 = var3.getState(var1);
      mxCellState var6 = var3.getState(var4.getParent(var1));
      if (var5 != null && var6 != null) {
         Object[] var7 = this.getCellsToShift(var5);
         mxGeometry var8 = var4.getGeometry(var1);
         if (var7 != null && var8 != null) {
            mxPoint var9 = var3.getTranslate();
            double var10 = var3.getScale();
            double var12 = var5.getX() - var6.getOrigin().getX() - var9.getX() * var10;
            double var14 = var5.getY() - var6.getOrigin().getY() - var9.getY() * var10;
            double var16 = var5.getX() + var5.getWidth();
            double var18 = var5.getY() + var5.getHeight();
            double var20 = var5.getWidth() - var8.getWidth() * var10 + var12 - var8.getX() * var10;
            double var22 = var5.getHeight() - var8.getHeight() * var10 + var14 - var8.getY() * var10;
            double var24 = 1.0 - var8.getWidth() * var10 / var5.getWidth();
            double var26 = 1.0 - var8.getHeight() * var10 / var5.getHeight();
            var4.beginUpdate();

            try {
               for(int var28 = 0; var28 < var7.length; ++var28) {
                  if (var7[var28] != var1 && this.isCellShiftable(var7[var28])) {
                     this.shiftCell(var7[var28], var20, var22, var12, var14, var16, var18, var24, var26, this.isExtendParents() && var2.isExtendParent(var7[var28]));
                  }
               }
            } finally {
               var4.endUpdate();
            }
         }
      }

   }

   protected void shiftCell(Object var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, boolean var18) {
      mxGraph var19 = this.getGraph();
      mxCellState var20 = var19.getView().getState(var1);
      if (var20 != null) {
         mxIGraphModel var21 = var19.getModel();
         mxGeometry var22 = var21.getGeometry(var1);
         if (var22 != null) {
            var21.beginUpdate();

            try {
               double var23;
               if (this.isShiftRightwards()) {
                  if (var20.getX() >= var10) {
                     var22 = (mxGeometry)var22.clone();
                     var22.translate(-var2, 0.0);
                  } else {
                     var23 = Math.max(0.0, var20.getX() - var6);
                     var22 = (mxGeometry)var22.clone();
                     var22.translate(-var14 * var23, 0.0);
                  }
               }

               if (this.isShiftDownwards()) {
                  if (var20.getY() >= var12) {
                     var22 = (mxGeometry)var22.clone();
                     var22.translate(0.0, -var4);
                  } else {
                     var23 = Math.max(0.0, var20.getY() - var8);
                     var22 = (mxGeometry)var22.clone();
                     var22.translate(0.0, -var16 * var23);
                  }

                  if (var22 != var21.getGeometry(var1)) {
                     var21.setGeometry(var1, var22);
                     if (var18) {
                        var19.extendParent(var1);
                     }
                  }
               }
            } finally {
               var21.endUpdate();
            }
         }
      }

   }

   protected Object[] getCellsToShift(mxCellState var1) {
      mxGraph var2 = this.getGraph();
      Object var3 = var2.getModel().getParent(var1.getCell());
      boolean var4 = this.isShiftDownwards();
      boolean var5 = this.isShiftRightwards();
      return var2.getCellsBeyond(var1.getX() + (var4 ? 0.0 : var1.getWidth()), var1.getY() + (var4 && var5 ? 0.0 : var1.getHeight()), var3, var5, var4);
   }

   public void destroy() {
      this.setGraph((mxGraph)null);
   }
}

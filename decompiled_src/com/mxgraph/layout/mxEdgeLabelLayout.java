package com.mxgraph.layout;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import java.awt.Rectangle;
import java.util.ArrayList;

public class mxEdgeLabelLayout extends mxGraphLayout {
   public mxEdgeLabelLayout(mxGraph var1) {
      super(var1);
   }

   public void execute(Object var1) {
      mxGraphView var2 = this.graph.getView();
      mxIGraphModel var3 = this.graph.getModel();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      int var6 = var3.getChildCount(var1);

      for(int var7 = 0; var7 < var6; ++var7) {
         Object var8 = var3.getChildAt(var1, var7);
         mxCellState var9 = var2.getState(var8);
         if (var9 != null) {
            if (!this.isVertexIgnored(var8)) {
               var5.add(var9);
            } else if (!this.isEdgeIgnored(var8)) {
               var4.add(var9);
            }
         }
      }

      this.placeLabels(var5.toArray(), var4.toArray());
   }

   protected void placeLabels(Object[] var1, Object[] var2) {
      mxIGraphModel var3 = this.graph.getModel();
      var3.beginUpdate();

      try {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            mxCellState var5 = (mxCellState)var2[var4];
            if (var5 != null && var5.getLabelBounds() != null) {
               for(int var6 = 0; var6 < var1.length; ++var6) {
                  mxCellState var7 = (mxCellState)var1[var6];
                  if (var7 != null) {
                     this.avoid(var5, var7);
                  }
               }
            }
         }
      } finally {
         var3.endUpdate();
      }

   }

   protected void avoid(mxCellState var1, mxCellState var2) {
      mxIGraphModel var3 = this.graph.getModel();
      Rectangle var4 = var1.getLabelBounds().getRectangle();
      Rectangle var5 = var2.getRectangle();
      if (var4.intersects(var5)) {
         int var6 = -var4.y - var4.height + var5.y;
         int var7 = -var4.y + var5.y + var5.height;
         int var8 = Math.abs(var6) < Math.abs(var7) ? var6 : var7;
         int var9 = -var4.x - var4.width + var5.x;
         int var10 = -var4.x + var5.x + var5.width;
         int var11 = Math.abs(var9) < Math.abs(var10) ? var9 : var10;
         if (Math.abs(var11) < Math.abs(var8)) {
            var8 = 0;
         } else {
            var11 = 0;
         }

         mxGeometry var12 = var3.getGeometry(var1.getCell());
         if (var12 != null) {
            var12 = (mxGeometry)var12.clone();
            if (var12.getOffset() != null) {
               var12.getOffset().setX(var12.getOffset().getX() + (double)var11);
               var12.getOffset().setY(var12.getOffset().getY() + (double)var8);
            } else {
               var12.setOffset(new mxPoint((double)var11, (double)var8));
            }

            var3.setGeometry(var1.getCell(), var12);
         }
      }

   }
}

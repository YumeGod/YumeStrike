package com.mxgraph.layout;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;

public class mxPartitionLayout extends mxGraphLayout {
   protected boolean horizontal;
   protected int spacing;
   protected int border;
   protected boolean resizeVertices;

   public mxPartitionLayout(mxGraph var1) {
      this(var1, true);
   }

   public mxPartitionLayout(mxGraph var1, boolean var2) {
      this(var1, var2, 0);
   }

   public mxPartitionLayout(mxGraph var1, boolean var2, int var3) {
      this(var1, var2, var3, 0);
   }

   public mxPartitionLayout(mxGraph var1, boolean var2, int var3, int var4) {
      super(var1);
      this.resizeVertices = true;
      this.horizontal = var2;
      this.spacing = var3;
      this.border = var4;
   }

   public void moveCell(Object var1, double var2, double var4) {
      mxIGraphModel var6 = this.graph.getModel();
      Object var7 = var6.getParent(var1);
      if (var1 instanceof mxICell && var7 instanceof mxICell) {
         boolean var8 = false;
         double var9 = 0.0;
         int var11 = var6.getChildCount(var7);

         int var16;
         for(var16 = 0; var16 < var11; ++var16) {
            Object var12 = var6.getChildAt(var7, var16);
            mxRectangle var13 = this.getVertexBounds(var12);
            if (var13 != null) {
               double var14 = var13.getX() + var13.getWidth() / 2.0;
               if (var9 < var2 && var14 > var2) {
                  break;
               }

               var9 = var14;
            }
         }

         int var17 = ((mxICell)var7).getIndex((mxICell)var1);
         var17 = Math.max(0, var16 - (var16 > var17 ? 1 : 0));
         var6.add(var7, var1, var17);
      }

   }

   public mxRectangle getContainerSize() {
      return new mxRectangle();
   }

   public void execute(Object var1) {
      mxIGraphModel var2 = this.graph.getModel();
      mxGeometry var3 = var2.getGeometry(var1);
      if (var3 == null && var2.getParent(var1) == var2.getRoot() || var1 == this.graph.getView().getCurrentRoot()) {
         mxRectangle var4 = this.getContainerSize();
         var3 = new mxGeometry(0.0, 0.0, var4.getWidth(), var4.getHeight());
      }

      if (var3 != null) {
         int var24 = var2.getChildCount(var1);
         ArrayList var5 = new ArrayList(var24);

         int var6;
         for(var6 = 0; var6 < var24; ++var6) {
            Object var7 = var2.getChildAt(var1, var6);
            if (!this.isVertexIgnored(var7) && this.isVertexMovable(var7)) {
               var5.add(var7);
            }
         }

         var6 = var5.size();
         if (var6 > 0) {
            double var25 = (double)this.border;
            double var9 = (double)this.border;
            double var11 = this.horizontal ? var3.getHeight() : var3.getWidth();
            var11 -= (double)(2 * this.border);
            mxRectangle var13 = this.graph.getStartSize(var1);
            var11 -= this.horizontal ? var13.getHeight() : var13.getWidth();
            var25 += var13.getWidth();
            var9 += var13.getHeight();
            double var14 = (double)(this.border + (var6 - 1) * this.spacing);
            double var16 = this.horizontal ? (var3.getWidth() - var25 - var14) / (double)var6 : (var3.getHeight() - var9 - var14) / (double)var6;
            if (var16 > 0.0) {
               var2.beginUpdate();

               try {
                  for(int var18 = 0; var18 < var6; ++var18) {
                     Object var19 = var5.get(var18);
                     mxGeometry var20 = var2.getGeometry(var19);
                     if (var20 != null) {
                        var20 = (mxGeometry)var20.clone();
                        var20.setX(var25);
                        var20.setY(var9);
                        if (this.horizontal) {
                           if (this.resizeVertices) {
                              var20.setWidth(var16);
                              var20.setHeight(var11);
                           }

                           var25 += var16 + (double)this.spacing;
                        } else {
                           if (this.resizeVertices) {
                              var20.setHeight(var16);
                              var20.setWidth(var11);
                           }

                           var9 += var16 + (double)this.spacing;
                        }

                        var2.setGeometry(var19, var20);
                     }
                  }
               } finally {
                  var2.endUpdate();
               }
            }
         }
      }

   }
}

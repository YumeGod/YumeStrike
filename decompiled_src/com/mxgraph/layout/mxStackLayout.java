package com.mxgraph.layout;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

public class mxStackLayout extends mxGraphLayout {
   protected boolean horizontal;
   protected int spacing;
   protected int x0;
   protected int y0;
   protected int border;
   protected boolean fill;
   protected boolean resizeParent;
   protected int wrap;

   public mxStackLayout(mxGraph var1) {
      this(var1, true);
   }

   public mxStackLayout(mxGraph var1, boolean var2) {
      this(var1, var2, 0);
   }

   public mxStackLayout(mxGraph var1, boolean var2, int var3) {
      this(var1, var2, var3, 0, 0, 0);
   }

   public mxStackLayout(mxGraph var1, boolean var2, int var3, int var4, int var5, int var6) {
      super(var1);
      this.fill = false;
      this.resizeParent = false;
      this.wrap = 0;
      this.horizontal = var2;
      this.spacing = var3;
      this.x0 = var4;
      this.y0 = var5;
      this.border = var6;
   }

   public boolean isHorizontal() {
      return this.horizontal;
   }

   public void moveCell(Object var1, double var2, double var4) {
      mxIGraphModel var6 = this.graph.getModel();
      Object var7 = var6.getParent(var1);
      boolean var8 = this.isHorizontal();
      if (var1 instanceof mxICell && var7 instanceof mxICell) {
         boolean var9 = false;
         double var10 = 0.0;
         int var12 = var6.getChildCount(var7);
         double var13 = var8 ? var2 : var4;
         mxCellState var15 = this.graph.getView().getState(var7);
         if (var15 != null) {
            var13 -= var8 ? var15.getX() : var15.getY();
         }

         int var20;
         for(var20 = 0; var20 < var12; ++var20) {
            Object var16 = var6.getChildAt(var7, var20);
            if (var16 != var1) {
               mxGeometry var17 = var6.getGeometry(var16);
               if (var17 != null) {
                  double var18 = var8 ? var17.getX() + var17.getWidth() / 2.0 : var17.getY() + var17.getHeight() / 2.0;
                  if (var10 < var13 && var18 > var13) {
                     break;
                  }

                  var10 = var18;
               }
            }
         }

         int var21 = ((mxICell)var7).getIndex((mxICell)var1);
         var21 = Math.max(0, var20 - (var20 > var21 ? 1 : 0));
         var6.add(var7, var1, var21);
      }

   }

   public mxRectangle getContainerSize() {
      return new mxRectangle();
   }

   public void execute(Object var1) {
      if (var1 != null) {
         boolean var2 = this.isHorizontal();
         mxIGraphModel var3 = this.graph.getModel();
         mxGeometry var4 = var3.getGeometry(var1);
         if (var4 == null && var3.getParent(var1) == var3.getRoot() || var1 == this.graph.getView().getCurrentRoot()) {
            mxRectangle var5 = this.getContainerSize();
            var4 = new mxGeometry(0.0, 0.0, var5.getWidth(), var5.getHeight());
         }

         double var22 = 0.0;
         if (var4 != null) {
            var22 = var2 ? var4.getHeight() : var4.getWidth();
         }

         var22 -= (double)(2 * this.spacing + 2 * this.border);
         mxRectangle var7 = this.graph.getStartSize(var1);
         var22 -= var2 ? var7.getHeight() : var7.getWidth();
         double var8 = (double)this.x0 + var7.getWidth() + (double)this.border;
         double var10 = (double)this.y0 + var7.getHeight() + (double)this.border;
         var3.beginUpdate();

         try {
            double var12 = 0.0;
            mxGeometry var14 = null;
            int var15 = var3.getChildCount(var1);

            for(int var16 = 0; var16 < var15; ++var16) {
               Object var17 = var3.getChildAt(var1, var16);
               if (!this.isVertexIgnored(var17) && this.isVertexMovable(var17)) {
                  mxGeometry var18 = var3.getGeometry(var17);
                  if (var18 != null) {
                     var18 = (mxGeometry)var18.clone();
                     if (this.wrap != 0 && var14 != null && (var2 && var14.getX() + var14.getWidth() + var18.getWidth() + (double)(2 * this.spacing) > (double)this.wrap || !var2 && var14.getY() + var14.getHeight() + var18.getHeight() + (double)(2 * this.spacing) > (double)this.wrap)) {
                        var14 = null;
                        if (var2) {
                           var10 += var12 + (double)this.spacing;
                        } else {
                           var8 += var12 + (double)this.spacing;
                        }

                        var12 = 0.0;
                     }

                     var12 = Math.max(var12, var2 ? var18.getHeight() : var18.getWidth());
                     if (var14 != null) {
                        if (var2) {
                           var18.setX(var14.getX() + var14.getWidth() + (double)this.spacing);
                        } else {
                           var18.setY(var14.getY() + var14.getHeight() + (double)this.spacing);
                        }
                     } else if (var2) {
                        var18.setX(var8);
                     } else {
                        var18.setY(var10);
                     }

                     if (var2) {
                        var18.setY(var10);
                     } else {
                        var18.setX(var8);
                     }

                     if (this.fill && var22 > 0.0) {
                        if (var2) {
                           var18.setHeight(var22);
                        } else {
                           var18.setWidth(var22);
                        }
                     }

                     var3.setGeometry(var17, var18);
                     var14 = var18;
                  }
               }
            }

            if (this.resizeParent && var4 != null && var14 != null && !this.graph.isCellCollapsed(var1)) {
               var4 = (mxGeometry)var4.clone();
               if (var2) {
                  var4.setWidth(var14.getX() + var14.getWidth() + (double)this.spacing);
               } else {
                  var4.setHeight(var14.getY() + var14.getHeight() + (double)this.spacing);
               }

               var3.setGeometry(var1, var4);
            }
         } finally {
            var3.endUpdate();
         }
      }

   }
}

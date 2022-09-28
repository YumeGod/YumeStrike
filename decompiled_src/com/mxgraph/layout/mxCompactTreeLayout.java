package com.mxgraph.layout;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class mxCompactTreeLayout extends mxGraphLayout {
   protected boolean horizontal;
   protected boolean invert;
   protected boolean resizeParent;
   protected boolean moveTree;
   protected boolean resetEdges;
   protected int levelDistance;
   protected int nodeDistance;

   public mxCompactTreeLayout(mxGraph var1) {
      this(var1, true);
   }

   public mxCompactTreeLayout(mxGraph var1, boolean var2) {
      this(var1, var2, false);
   }

   public mxCompactTreeLayout(mxGraph var1, boolean var2, boolean var3) {
      super(var1);
      this.resizeParent = true;
      this.moveTree = true;
      this.resetEdges = true;
      this.levelDistance = 10;
      this.nodeDistance = 20;
      this.horizontal = var2;
      this.invert = var3;
   }

   public boolean isVertexIgnored(Object var1) {
      return super.isVertexIgnored(var1) || this.graph.getConnections(var1).length == 0;
   }

   public boolean isHorizontal() {
      return this.horizontal;
   }

   public void setHorizontal(boolean var1) {
      this.horizontal = var1;
   }

   public boolean isInvert() {
      return this.invert;
   }

   public void setInvert(boolean var1) {
      this.invert = var1;
   }

   public boolean isResizeParent() {
      return this.resizeParent;
   }

   public void setResizeParent(boolean var1) {
      this.resizeParent = var1;
   }

   public boolean isMoveTree() {
      return this.moveTree;
   }

   public void setMoveTree(boolean var1) {
      this.moveTree = var1;
   }

   public boolean isResetEdges() {
      return this.resetEdges;
   }

   public void setResetEdges(boolean var1) {
      this.resetEdges = var1;
   }

   public int getLevelDistance() {
      return this.levelDistance;
   }

   public void setLevelDistance(int var1) {
      this.levelDistance = var1;
   }

   public int getNodeDistance() {
      return this.nodeDistance;
   }

   public void setNodeDistance(int var1) {
      this.nodeDistance = var1;
   }

   public void execute(Object var1) {
      this.execute(var1, (Object)null);
   }

   public void execute(Object var1, Object var2) {
      mxIGraphModel var3 = this.graph.getModel();
      if (var2 == null) {
         if (this.graph.getEdges(var1, var3.getParent(var1), this.invert, !this.invert, false).length > 0) {
            var2 = var1;
         } else {
            Object[] var4 = this.graph.findTreeRoots(var1, true, this.invert);
            if (var4.length > 0) {
               for(int var5 = 0; var5 < var4.length; ++var5) {
                  if (!this.isVertexIgnored(var4[var5]) && this.graph.getEdges(var4[var5], (Object)null, this.invert, !this.invert, false).length > 0) {
                     var2 = var4[var5];
                     break;
                  }
               }
            }
         }
      }

      if (var2 != null) {
         var1 = var3.getParent(var2);
         var3.beginUpdate();

         try {
            TreeNode var23 = this.dfs(var2, var1, (Set)null);
            if (var23 != null) {
               this.layout(var23);
               double var24 = (double)this.graph.getGridSize();
               double var7 = var24;
               mxGeometry var9;
               if (!this.moveTree || var3.getParent(var1) == var3.getRoot()) {
                  var9 = var3.getGeometry(var2);
                  if (var9 != null) {
                     var24 = var9.getX();
                     var7 = var9.getY();
                  }
               }

               var9 = null;
               mxRectangle var25;
               if (this.horizontal) {
                  var25 = this.horizontalLayout(var23, var24, var7, (mxRectangle)null);
               } else {
                  var25 = this.verticalLayout(var23, (Object)null, var24, var7, (mxRectangle)null);
               }

               if (var25 != null) {
                  double var10 = 0.0;
                  double var12 = 0.0;
                  if (var25.getX() < 0.0) {
                     var10 = Math.abs(var24 - var25.getX());
                  }

                  if (var25.getY() < 0.0) {
                     var12 = Math.abs(var7 - var25.getY());
                  }

                  if (var1 != null) {
                     mxRectangle var14 = this.graph.getStartSize(var1);
                     var10 += var14.getWidth();
                     var12 += var14.getHeight();
                     if (this.resizeParent && !this.graph.isCellCollapsed(var1)) {
                        mxGeometry var15 = var3.getGeometry(var1);
                        if (var15 != null) {
                           double var16 = var25.getWidth() + var14.getWidth() - var25.getX() + 2.0 * var24;
                           double var18 = var25.getHeight() + var14.getHeight() - var25.getY() + 2.0 * var7;
                           var15 = (mxGeometry)var15.clone();
                           if (var15.getWidth() > var16) {
                              var10 += (var15.getWidth() - var16) / 2.0;
                           } else {
                              var15.setWidth(var16);
                           }

                           if (var15.getHeight() > var18) {
                              if (this.horizontal) {
                                 var12 += (var15.getHeight() - var18) / 2.0;
                              }
                           } else {
                              var15.setHeight(var18);
                           }

                           var3.setGeometry(var1, var15);
                        }
                     }
                  }

                  this.moveNode(var23, var10, var12);
               }
            }
         } finally {
            var3.endUpdate();
         }
      }

   }

   protected void moveNode(TreeNode var1, double var2, double var4) {
      var1.x += var2;
      var1.y += var4;
      this.apply(var1, (mxRectangle)null);

      for(TreeNode var6 = var1.child; var6 != null; var6 = var6.next) {
         this.moveNode(var6, var2, var4);
      }

   }

   protected TreeNode dfs(Object var1, Object var2, Set var3) {
      if (var3 == null) {
         var3 = new HashSet();
      }

      TreeNode var4 = null;
      if (var1 != null && !((Set)var3).contains(var1) && !this.isVertexIgnored(var1)) {
         ((Set)var3).add(var1);
         var4 = this.createNode(var1);
         mxIGraphModel var5 = this.graph.getModel();
         TreeNode var6 = null;
         Object[] var7 = this.graph.getEdges(var1, var2, this.invert, !this.invert, false);

         for(int var8 = 0; var8 < var7.length; ++var8) {
            Object var9 = var7[var8];
            if (!this.isEdgeIgnored(var9)) {
               if (this.resetEdges) {
                  this.setEdgePoints(var9, (List)null);
               }

               Object var10 = this.graph.getView().getVisibleTerminal(var9, this.invert);
               TreeNode var11 = this.dfs(var10, var2, (Set)var3);
               if (var11 != null && var5.getGeometry(var10) != null) {
                  if (var6 == null) {
                     var4.child = var11;
                  } else {
                     var6.next = var11;
                  }

                  var6 = var11;
               }
            }
         }
      }

      return var4;
   }

   protected void layout(TreeNode var1) {
      if (var1 != null) {
         for(TreeNode var2 = var1.child; var2 != null; var2 = var2.next) {
            this.layout(var2);
         }

         if (var1.child != null) {
            this.attachParent(var1, this.join(var1));
         } else {
            this.layoutLeaf(var1);
         }
      }

   }

   protected mxRectangle horizontalLayout(TreeNode var1, double var2, double var4, mxRectangle var6) {
      var1.x += var2 + var1.offsetX;
      var1.y += var4 + var1.offsetY;
      var6 = this.apply(var1, var6);
      TreeNode var7 = var1.child;
      if (var7 != null) {
         var6 = this.horizontalLayout(var7, var1.x, var1.y, var6);
         double var8 = var1.y + var7.offsetY;

         for(TreeNode var10 = var7.next; var10 != null; var10 = var10.next) {
            var6 = this.horizontalLayout(var10, var1.x + var7.offsetX, var8, var6);
            var8 += var10.offsetY;
         }
      }

      return var6;
   }

   protected mxRectangle verticalLayout(TreeNode var1, Object var2, double var3, double var5, mxRectangle var7) {
      var1.x += var3 + var1.offsetY;
      var1.y += var5 + var1.offsetX;
      var7 = this.apply(var1, var7);
      TreeNode var8 = var1.child;
      if (var8 != null) {
         var7 = this.verticalLayout(var8, var1, var1.x, var1.y, var7);
         double var9 = var1.x + var8.offsetY;

         for(TreeNode var11 = var8.next; var11 != null; var11 = var11.next) {
            var7 = this.verticalLayout(var11, var1, var9, var1.y + var8.offsetX, var7);
            var9 += var11.offsetY;
         }
      }

      return var7;
   }

   protected void attachParent(TreeNode var1, double var2) {
      double var4 = (double)(this.nodeDistance + this.levelDistance);
      double var6 = (var2 - var1.width) / 2.0 - (double)this.nodeDistance;
      double var8 = var6 + var1.width + (double)(2 * this.nodeDistance) - var2;
      var1.child.offsetX = var4 + var1.height;
      var1.child.offsetY = var8;
      var1.contour.upperHead = this.createLine(var1.height, 0.0, this.createLine(var4, var8, var1.contour.upperHead));
      var1.contour.lowerHead = this.createLine(var1.height, 0.0, this.createLine(var4, var6, var1.contour.lowerHead));
   }

   protected void layoutLeaf(TreeNode var1) {
      double var2 = (double)(2 * this.nodeDistance);
      var1.contour.upperTail = this.createLine(var1.height + var2, 0.0, (Polyline)null);
      var1.contour.upperHead = var1.contour.upperTail;
      var1.contour.lowerTail = this.createLine(0.0, -var1.width - var2, (Polyline)null);
      var1.contour.lowerHead = this.createLine(var1.height + var2, 0.0, var1.contour.lowerTail);
   }

   protected double join(TreeNode var1) {
      double var2 = (double)(2 * this.nodeDistance);
      TreeNode var4 = var1.child;
      var1.contour = var4.contour;
      double var5 = var4.width + var2;
      double var7 = var5;

      for(var4 = var4.next; var4 != null; var4 = var4.next) {
         double var9 = this.merge(var1.contour, var4.contour);
         var4.offsetY = var9 + var5;
         var4.offsetX = 0.0;
         var5 = var4.width + var2;
         var7 += var9 + var5;
      }

      return var7;
   }

   protected double merge(Polygon var1, Polygon var2) {
      double var3 = 0.0;
      double var5 = 0.0;
      double var7 = 0.0;
      Polyline var9 = var1.lowerHead;
      Polyline var10 = var2.upperHead;

      while(var10 != null && var9 != null) {
         double var11 = this.offset(var3, var5, var10.dx, var10.dy, var9.dx, var9.dy);
         var5 += var11;
         var7 += var11;
         if (var3 + var10.dx <= var9.dx) {
            var3 += var10.dx;
            var5 += var10.dy;
            var10 = var10.next;
         } else {
            var3 -= var9.dx;
            var5 -= var9.dy;
            var9 = var9.next;
         }
      }

      Polyline var13;
      if (var10 != null) {
         var13 = this.bridge(var1.upperTail, 0.0, 0.0, var10, var3, var5);
         var1.upperTail = var13.next != null ? var2.upperTail : var13;
         var1.lowerTail = var2.lowerTail;
      } else {
         var13 = this.bridge(var2.lowerTail, var3, var5, var9, 0.0, 0.0);
         if (var13.next == null) {
            var1.lowerTail = var13;
         }
      }

      var1.lowerHead = var2.lowerHead;
      return var7;
   }

   protected double offset(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = 0.0;
      if (!(var9 <= var1) && !(var1 + var5 <= 0.0)) {
         double var15 = var9 * var7 - var5 * var11;
         double var17;
         if (var15 > 0.0) {
            if (var1 < 0.0) {
               var17 = var1 * var7;
               var13 = var17 / var5 - var3;
            } else if (var1 > 0.0) {
               var17 = var1 * var11;
               var13 = var17 / var9 - var3;
            } else {
               var13 = -var3;
            }
         } else if (var9 < var1 + var5) {
            var17 = (var9 - var1) * var7;
            var13 = var11 - (var3 + var17 / var5);
         } else if (var9 > var1 + var5) {
            var17 = (var5 + var1) * var11;
            var13 = var17 / var9 - (var3 + var7);
         } else {
            var13 = var11 - (var3 + var7);
         }

         return var13 > 0.0 ? var13 : 0.0;
      } else {
         return 0.0;
      }
   }

   protected Polyline bridge(Polyline var1, double var2, double var4, Polyline var6, double var7, double var9) {
      double var11 = var7 + var6.dx - var2;
      double var13 = 0.0;
      double var15 = 0.0;
      if (var6.dx == 0.0) {
         var13 = var6.dy;
      } else {
         var15 = var11 * var6.dy;
         var13 = var15 / var6.dx;
      }

      Polyline var17 = this.createLine(var11, var13, var6.next);
      var1.next = this.createLine(0.0, var9 + var6.dy - var13 - var4, var17);
      return var17;
   }

   protected TreeNode createNode(Object var1) {
      TreeNode var2 = new TreeNode(var1);
      mxRectangle var3 = this.getVertexBounds(var1);
      if (var3 != null) {
         if (this.horizontal) {
            var2.width = var3.getHeight();
            var2.height = var3.getWidth();
         } else {
            var2.width = var3.getWidth();
            var2.height = var3.getHeight();
         }
      }

      return var2;
   }

   protected mxRectangle apply(TreeNode var1, mxRectangle var2) {
      Object var3 = this.graph.getModel().getGeometry(var1.cell);
      if (var1.cell != null && var3 != null) {
         if (this.isVertexMovable(var1.cell)) {
            var3 = this.setVertexLocation(var1.cell, var1.x, var1.y);
         }

         if (var2 == null) {
            var2 = new mxRectangle(((mxRectangle)var3).getX(), ((mxRectangle)var3).getY(), ((mxRectangle)var3).getWidth(), ((mxRectangle)var3).getHeight());
         } else {
            var2 = new mxRectangle(Math.min(var2.getX(), ((mxRectangle)var3).getX()), Math.min(var2.getY(), ((mxRectangle)var3).getY()), Math.max(var2.getX() + var2.getWidth(), ((mxRectangle)var3).getX() + ((mxRectangle)var3).getWidth()), Math.max(var2.getY() + var2.getHeight(), ((mxRectangle)var3).getY() + ((mxRectangle)var3).getHeight()));
         }
      }

      return var2;
   }

   protected Polyline createLine(double var1, double var3, Polyline var5) {
      return new Polyline(var1, var3, var5);
   }

   protected static class Polyline {
      protected double dx;
      protected double dy;
      protected Polyline next;

      protected Polyline(double var1, double var3, Polyline var5) {
         this.dx = var1;
         this.dy = var3;
         this.next = var5;
      }
   }

   protected static class Polygon {
      protected Polyline lowerHead;
      protected Polyline lowerTail;
      protected Polyline upperHead;
      protected Polyline upperTail;
   }

   protected static class TreeNode {
      protected Object cell;
      protected double x;
      protected double y;
      protected double width;
      protected double height;
      protected double offsetX;
      protected double offsetY;
      protected TreeNode child;
      protected TreeNode next;
      protected Polygon contour = new Polygon();

      public TreeNode(Object var1) {
         this.cell = var1;
      }
   }
}

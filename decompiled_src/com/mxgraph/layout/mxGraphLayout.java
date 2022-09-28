package com.mxgraph.layout;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import java.util.List;
import java.util.Map;

public abstract class mxGraphLayout implements mxIGraphLayout {
   protected mxGraph graph;
   protected boolean useBoundingBox = true;

   public mxGraphLayout(mxGraph var1) {
      this.graph = var1;
   }

   public void moveCell(Object var1, double var2, double var4) {
   }

   public mxGraph getGraph() {
      return this.graph;
   }

   public Object getConstraint(Object var1, Object var2) {
      return this.getConstraint(var1, var2, (Object)null, false);
   }

   public Object getConstraint(Object var1, Object var2, Object var3, boolean var4) {
      mxCellState var5 = this.graph.getView().getState(var2);
      Map var6 = var5 != null ? var5.getStyle() : this.graph.getCellStyle(var2);
      return var6 != null ? var6.get(var1) : null;
   }

   public boolean isUseBoundingBox() {
      return this.useBoundingBox;
   }

   public void setUseBoundingBox(boolean var1) {
      this.useBoundingBox = var1;
   }

   public boolean isVertexMovable(Object var1) {
      return this.graph.isCellMovable(var1);
   }

   public boolean isVertexIgnored(Object var1) {
      return !this.graph.getModel().isVertex(var1) || !this.graph.isCellVisible(var1);
   }

   public boolean isEdgeIgnored(Object var1) {
      mxIGraphModel var2 = this.graph.getModel();
      return !var2.isEdge(var1) || !this.graph.isCellVisible(var1) || var2.getTerminal(var1, true) == null || var2.getTerminal(var1, false) == null;
   }

   public void setEdgeStyleEnabled(Object var1, boolean var2) {
      this.graph.setCellStyles(mxConstants.STYLE_NOEDGESTYLE, var2 ? "0" : "1", new Object[]{var1});
   }

   public void setEdgePoints(Object var1, List var2) {
      mxIGraphModel var3 = this.graph.getModel();
      mxGeometry var4 = var3.getGeometry(var1);
      if (var4 == null) {
         var4 = new mxGeometry();
         var4.setRelative(true);
      } else {
         var4 = (mxGeometry)var4.clone();
      }

      var4.setPoints(var2);
      var3.setGeometry(var1, var4);
   }

   public mxRectangle getVertexBounds(Object var1) {
      Object var2 = this.graph.getModel().getGeometry(var1);
      if (this.useBoundingBox) {
         mxCellState var3 = this.graph.getView().getState(var1);
         if (var3 != null) {
            double var4 = this.graph.getView().getScale();
            mxRectangle var6 = var3.getBoundingBox();
            double var7 = (var6.getX() - var3.getX()) / var4;
            double var9 = (var6.getY() - var3.getY()) / var4;
            double var11 = (var6.getX() + var6.getWidth() - var3.getX() - var3.getWidth()) / var4;
            double var13 = (var6.getY() + var6.getHeight() - var3.getY() - var3.getHeight()) / var4;
            var2 = new mxRectangle(((mxRectangle)var2).getX() + var7, ((mxRectangle)var2).getY() + var9, ((mxRectangle)var2).getWidth() - var7 + var11, ((mxRectangle)var2).getHeight() + -var9 + var13);
         }
      }

      return new mxRectangle((mxRectangle)var2);
   }

   public mxRectangle setVertexLocation(Object var1, double var2, double var4) {
      mxIGraphModel var6 = this.graph.getModel();
      mxGeometry var7 = var6.getGeometry(var1);
      mxRectangle var8 = null;
      if (var7 != null) {
         var8 = new mxRectangle(var2, var4, var7.getWidth(), var7.getHeight());
         if (this.useBoundingBox) {
            mxCellState var9 = this.graph.getView().getState(var1);
            if (var9 != null) {
               double var10 = this.graph.getView().getScale();
               mxRectangle var12 = var9.getBoundingBox();
               if (var9.getBoundingBox().getX() < var9.getX()) {
                  var2 += (var9.getX() - var12.getX()) / var10;
                  var8.setWidth(var12.getWidth());
               }

               if (var9.getBoundingBox().getY() < var9.getY()) {
                  var4 += (var9.getY() - var12.getY()) / var10;
                  var8.setHeight(var12.getHeight());
               }
            }
         }

         if (var7.getX() != var2 || var7.getY() != var4) {
            var7 = (mxGeometry)var7.clone();
            var7.setX(var2);
            var7.setY(var4);
            var6.setGeometry(var1, var7);
         }
      }

      return var8;
   }
}

package com.mxgraph.view;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class mxGraphView extends mxEventSource {
   private static mxPoint EMPTY_POINT = new mxPoint();
   protected mxGraph graph;
   protected Object currentRoot = null;
   protected mxRectangle graphBounds = new mxRectangle();
   protected double scale = 1.0;
   protected mxPoint translate = new mxPoint(0.0, 0.0);
   protected Hashtable states = new Hashtable();

   public mxGraphView(mxGraph var1) {
      this.graph = var1;
   }

   public mxGraph getGraph() {
      return this.graph;
   }

   public Hashtable getStates() {
      return this.states;
   }

   public void setStates(Hashtable var1) {
      this.states = var1;
   }

   public mxRectangle getGraphBounds() {
      return this.graphBounds;
   }

   public void setGraphBounds(mxRectangle var1) {
      this.graphBounds = var1;
   }

   public Object getCurrentRoot() {
      return this.currentRoot;
   }

   public Object setCurrentRoot(Object var1) {
      if (this.currentRoot != var1) {
         mxCurrentRootChange var2 = new mxCurrentRootChange(this, var1);
         var2.execute();
         mxUndoableEdit var3 = new mxUndoableEdit(this, false);
         var3.add(var2);
         this.fireEvent(new mxEventObject("undo", new Object[]{"edit", var3}));
      }

      return var1;
   }

   public void scaleAndTranslate(double var1, double var3, double var5) {
      double var7 = this.scale;
      Object var9 = this.translate.clone();
      if (var1 != this.scale || var3 != this.translate.getX() || var5 != this.translate.getY()) {
         this.scale = var1;
         this.translate = new mxPoint(var3, var5);
         if (this.isEventsEnabled()) {
            this.revalidate();
         }
      }

      this.fireEvent(new mxEventObject("scaleAndTranslate", new Object[]{"scale", var1, "previousScale", var7, "translate", this.translate, "previousTranslate", var9}));
   }

   public double getScale() {
      return this.scale;
   }

   public void setScale(double var1) {
      double var3 = this.scale;
      if (this.scale != var1) {
         this.scale = var1;
         if (this.isEventsEnabled()) {
            this.revalidate();
         }
      }

      this.fireEvent(new mxEventObject("scale", new Object[]{"scale", this.scale, "previousScale", var3}));
   }

   public mxPoint getTranslate() {
      return this.translate;
   }

   public void setTranslate(mxPoint var1) {
      Object var2 = this.translate.clone();
      if (var1 != null && (var1.getX() != this.translate.getX() || var1.getY() != this.translate.getY())) {
         this.translate = var1;
         if (this.isEventsEnabled()) {
            this.revalidate();
         }
      }

      this.fireEvent(new mxEventObject("translate", new Object[]{"translate", this.translate, "previousTranslate", var2}));
   }

   public mxRectangle getBounds(Object[] var1) {
      return this.getBounds(var1, false);
   }

   public mxRectangle getBoundingBox(Object[] var1) {
      return this.getBounds(var1, true);
   }

   public mxRectangle getBounds(Object[] var1, boolean var2) {
      mxRectangle var3 = null;
      if (var1 != null && var1.length > 0) {
         mxIGraphModel var4 = this.graph.getModel();

         for(int var5 = 0; var5 < var1.length; ++var5) {
            if (var4.isVertex(var1[var5]) || var4.isEdge(var1[var5])) {
               mxCellState var6 = this.getState(var1[var5]);
               if (var6 != null) {
                  Object var7 = var2 ? var6.getBoundingBox() : var6;
                  if (var7 != null) {
                     if (var3 == null) {
                        var3 = new mxRectangle((mxRectangle)var7);
                     } else {
                        var3.add((mxRectangle)var7);
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   public void reload() {
      this.states.clear();
      this.validate();
   }

   public void revalidate() {
      this.invalidate();
      this.validate();
   }

   public void invalidate() {
      this.invalidate((Object)null);
   }

   public void clear(Object var1, boolean var2, boolean var3) {
      this.removeState(var1);
      if (!var3 || !var2 && var1 == this.currentRoot) {
         this.invalidate(var1);
      } else {
         mxIGraphModel var4 = this.graph.getModel();
         int var5 = var4.getChildCount(var1);

         for(int var6 = 0; var6 < var5; ++var6) {
            this.clear(var4.getChildAt(var1, var6), var2, var3);
         }
      }

   }

   public void invalidate(Object var1) {
      mxIGraphModel var2 = this.graph.getModel();
      var1 = var1 != null ? var1 : var2.getRoot();
      mxCellState var3 = this.getState(var1);
      if (var3 == null || !var3.isInvalid()) {
         if (var3 != null) {
            var3.setInvalid(true);
         }

         int var4 = var2.getChildCount(var1);

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            Object var6 = var2.getChildAt(var1, var5);
            this.invalidate(var6);
         }

         var5 = var2.getEdgeCount(var1);

         for(int var7 = 0; var7 < var5; ++var7) {
            this.invalidate(var2.getEdgeAt(var1, var7));
         }
      }

   }

   public void validate() {
      Object var1 = this.currentRoot != null ? this.currentRoot : this.graph.getModel().getRoot();
      if (var1 != null) {
         this.validateBounds((mxCellState)null, var1);
         mxRectangle var2 = this.validatePoints((mxCellState)null, var1);
         if (var2 == null) {
            var2 = new mxRectangle();
         }

         this.setGraphBounds(var2);
      }

   }

   public void validateBounds(mxCellState var1, Object var2) {
      mxIGraphModel var3 = this.graph.getModel();
      mxCellState var4 = this.getState(var2, true);
      if (var4 != null && var4.isInvalid()) {
         if (!this.graph.isCellVisible(var2)) {
            this.removeState(var2);
         } else if (var2 != this.currentRoot && var1 != null) {
            var4.getAbsoluteOffset().setX(0.0);
            var4.getAbsoluteOffset().setY(0.0);
            var4.setOrigin(new mxPoint(var1.getOrigin()));
            mxGeometry var5 = this.graph.getCellGeometry(var2);
            if (var5 != null) {
               if (!var3.isEdge(var2)) {
                  mxPoint var6 = var4.getOrigin();
                  mxPoint var7 = var5.getOffset();
                  if (var7 == null) {
                     var7 = EMPTY_POINT;
                  }

                  if (var5.isRelative()) {
                     var6.setX(var6.getX() + var5.getX() * var1.getWidth() / this.scale + var7.getX());
                     var6.setY(var6.getY() + var5.getY() * var1.getHeight() / this.scale + var7.getY());
                  } else {
                     var4.setAbsoluteOffset(new mxPoint(this.scale * var7.getX(), this.scale * var7.getY()));
                     var6.setX(var6.getX() + var5.getX());
                     var6.setY(var6.getY() + var5.getY());
                  }
               }

               var4.setX(this.scale * (this.translate.getX() + var4.getOrigin().getX()));
               var4.setY(this.scale * (this.translate.getY() + var4.getOrigin().getY()));
               var4.setWidth(this.scale * var5.getWidth());
               var4.setHeight(this.scale * var5.getHeight());
               if (var3.isVertex(var2)) {
                  this.updateVertexLabelOffset(var4);
               }
            }
         }

         mxPoint var8 = this.graph.getChildOffsetForCell(var2);
         if (var8 != null) {
            var4.getOrigin().setX(var4.getOrigin().getX() + var8.getX());
            var4.getOrigin().setY(var4.getOrigin().getY() + var8.getY());
         }
      }

      if (var4 != null && (!this.graph.isCellCollapsed(var2) || var2 == this.currentRoot)) {
         int var9 = var3.getChildCount(var2);

         for(int var10 = 0; var10 < var9; ++var10) {
            this.validateBounds(var4, var3.getChildAt(var2, var10));
         }
      }

   }

   public void updateVertexLabelOffset(mxCellState var1) {
      String var2 = mxUtils.getString(var1.getStyle(), mxConstants.STYLE_LABEL_POSITION, "center");
      if (var2.equals("left")) {
         var1.absoluteOffset.setX(var1.absoluteOffset.getX() - var1.getWidth());
      } else if (var2.equals("right")) {
         var1.absoluteOffset.setX(var1.absoluteOffset.getX() + var1.getWidth());
      }

      String var3 = mxUtils.getString(var1.getStyle(), mxConstants.STYLE_VERTICAL_LABEL_POSITION, "middle");
      if (var3.equals("top")) {
         var1.absoluteOffset.setY(var1.absoluteOffset.getY() - var1.getHeight());
      } else if (var3.equals("bottom")) {
         var1.absoluteOffset.setY(var1.absoluteOffset.getY() + var1.getHeight());
      }

   }

   public mxRectangle validatePoints(mxCellState var1, Object var2) {
      mxIGraphModel var3 = this.graph.getModel();
      mxCellState var4 = this.getState(var2);
      mxRectangle var5 = null;
      if (var4 != null) {
         if (var4.isInvalid()) {
            mxGeometry var6 = this.graph.getCellGeometry(var2);
            if (var6 != null && var3.isEdge(var2)) {
               mxCellState var11 = this.getState(this.getVisibleTerminal(var2, true));
               mxCellState var8;
               if (var11 != null && var3.isEdge(var11.getCell()) && !var3.isAncestor(var11, var2)) {
                  var8 = this.getState(var3.getParent(var11.getCell()));
                  this.validatePoints(var8, var11);
               }

               var8 = this.getState(this.getVisibleTerminal(var2, false));
               if (var8 != null && var3.isEdge(var8.getCell()) && !var3.isAncestor(var8, var2)) {
                  mxCellState var9 = this.getState(var3.getParent(var8.getCell()));
                  this.validatePoints(var9, var8);
               }

               this.updateFixedTerminalPoints(var4, var11, var8);
               this.updatePoints(var4, var6.getPoints(), var11, var8);
               this.updateFloatingTerminalPoints(var4, var11, var8);
               this.updateEdgeBounds(var4);
               var4.setAbsoluteOffset(this.getPoint(var4, var6));
            } else if (var6 != null && var6.isRelative() && var1 != null && var3.isEdge(var1.getCell())) {
               mxPoint var7 = this.getPoint(var1, var6);
               if (var7 != null) {
                  var4.setX(var7.getX());
                  var4.setY(var7.getY());
                  var7.setX(var7.getX() / this.scale - this.translate.getX());
                  var7.setY(var7.getY() / this.scale - this.translate.getY());
                  var4.setOrigin(var7);
                  this.childMoved(var1, var4);
               }
            }

            var4.setInvalid(false);
         }

         if (var3.isEdge(var2) || var3.isVertex(var2)) {
            this.updateLabelBounds(var4);
            var5 = new mxRectangle(this.updateBoundingBox(var4));
         }
      }

      if (var4 != null && (!this.graph.isCellCollapsed(var2) || var2 == this.currentRoot)) {
         int var10 = var3.getChildCount(var2);

         for(int var12 = 0; var12 < var10; ++var12) {
            Object var13 = var3.getChildAt(var2, var12);
            mxRectangle var14 = this.validatePoints(var4, var13);
            if (var14 != null) {
               if (var5 == null) {
                  var5 = var14;
               } else {
                  var5.add(var14);
               }
            }
         }
      }

      return var5;
   }

   protected void childMoved(mxCellState var1, mxCellState var2) {
      Object var3 = var2.getCell();
      if (!this.graph.isCellCollapsed(var3) || var3 == this.currentRoot) {
         mxIGraphModel var4 = this.graph.getModel();
         int var5 = var4.getChildCount(var3);

         for(int var6 = 0; var6 < var5; ++var6) {
            this.validateBounds(var2, var4.getChildAt(var3, var6));
         }
      }

   }

   public void updateLabelBounds(mxCellState var1) {
      Object var2 = var1.getCell();
      Map var3 = var1.getStyle();
      if (mxUtils.getString(var3, mxConstants.STYLE_OVERFLOW, "").equals("fill")) {
         var1.setLabelBounds(new mxRectangle(var1));
      } else {
         String var4 = this.graph.getLabel(var2);
         mxCellState var5 = !this.graph.getModel().isEdge(var2) ? var1 : null;
         var1.setLabelBounds(mxUtils.getLabelPaintBounds(var4, var3, this.graph.isHtmlLabel(var2), var1.getAbsoluteOffset(), var5, this.scale));
      }

   }

   public mxRectangle updateBoundingBox(mxCellState var1) {
      mxRectangle var2 = new mxRectangle(var1);
      Map var3 = var1.getStyle();
      double var4 = (double)Math.max(1L, Math.round((double)mxUtils.getInt(var3, mxConstants.STYLE_STROKEWIDTH, 1) * this.scale));
      var4 -= Math.max(1.0, var4 / 2.0);
      if (this.graph.getModel().isEdge(var1.getCell())) {
         int var6 = 0;
         if (var3.containsKey(mxConstants.STYLE_ENDARROW) || var3.containsKey(mxConstants.STYLE_STARTARROW)) {
            var6 = (int)Math.round((double)mxConstants.DEFAULT_MARKERSIZE * this.scale);
         }

         var2.grow((double)var6 + var4);
         if (mxUtils.getString(var3, mxConstants.STYLE_SHAPE, "").equals("arrow")) {
            var2.grow((double)(mxConstants.ARROW_WIDTH / 2));
         }
      } else {
         var2.grow(var4);
      }

      if (mxUtils.isTrue(var3, mxConstants.STYLE_SHADOW)) {
         var2.setWidth(var2.getWidth() + (double)mxConstants.SHADOW_OFFSETX);
         var2.setHeight(var2.getHeight() + (double)mxConstants.SHADOW_OFFSETY);
      }

      double var16;
      if (mxUtils.getString(var3, mxConstants.STYLE_SHAPE, "").equals("label") && mxUtils.getString(var3, mxConstants.STYLE_IMAGE) != null) {
         var16 = (double)mxUtils.getInt(var3, mxConstants.STYLE_IMAGE_WIDTH, mxConstants.DEFAULT_IMAGESIZE) * this.scale;
         double var8 = (double)mxUtils.getInt(var3, mxConstants.STYLE_IMAGE_HEIGHT, mxConstants.DEFAULT_IMAGESIZE) * this.scale;
         double var10 = var1.getX();
         double var12 = 0.0;
         String var14 = mxUtils.getString(var3, mxConstants.STYLE_IMAGE_ALIGN, "center");
         String var15 = mxUtils.getString(var3, mxConstants.STYLE_IMAGE_VERTICAL_ALIGN, "middle");
         if (var14.equals("right")) {
            var10 += var1.getWidth() - var16;
         } else if (var14.equals("center")) {
            var10 += (var1.getWidth() - var16) / 2.0;
         }

         if (var15.equals("top")) {
            var12 = var1.getY();
         } else if (var15.equals("bottom")) {
            var12 = var1.getY() + var1.getHeight() - var8;
         } else {
            var12 = var1.getY() + (var1.getHeight() - var8) / 2.0;
         }

         var2.add(new mxRectangle(var10, var12, var16, var8));
      }

      var16 = mxUtils.getDouble(var3, mxConstants.STYLE_ROTATION);
      mxRectangle var17 = mxUtils.getBoundingBox(var2, var16);
      var2.add(var17);
      if (!this.graph.isLabelClipped(var1.getCell())) {
         var2.add(var1.getLabelBounds());
      }

      var1.setBoundingBox(var2);
      return var2;
   }

   public void updateFixedTerminalPoints(mxCellState var1, mxCellState var2, mxCellState var3) {
      this.updateFixedTerminalPoint(var1, var2, true, this.graph.getConnectionConstraint(var1, var2, true));
      this.updateFixedTerminalPoint(var1, var3, false, this.graph.getConnectionConstraint(var1, var3, false));
   }

   public void updateFixedTerminalPoint(mxCellState var1, mxCellState var2, boolean var3, mxConnectionConstraint var4) {
      mxPoint var5 = null;
      if (var4 != null) {
         var5 = this.graph.getConnectionPoint(var2, var4);
      }

      if (var5 == null && var2 == null) {
         mxPoint var6 = var1.getOrigin();
         mxGeometry var7 = this.graph.getCellGeometry(var1.cell);
         var5 = var7.getTerminalPoint(var3);
         if (var5 != null) {
            var5 = new mxPoint(this.scale * (this.translate.getX() + var5.getX() + var6.getX()), this.scale * (this.translate.getY() + var5.getY() + var6.getY()));
         }
      }

      var1.setAbsoluteTerminalPoint(var5, var3);
   }

   public void updatePoints(mxCellState var1, List var2, mxCellState var3, mxCellState var4) {
      if (var1 != null) {
         ArrayList var5 = new ArrayList();
         var5.add(var1.getAbsolutePoint(0));
         mxEdgeStyle.mxEdgeStyleFunction var6 = this.getEdgeStyle(var1, var2, var3, var4);
         if (var6 != null) {
            mxCellState var9 = this.getTerminalPort(var1, var3, true);
            mxCellState var8 = this.getTerminalPort(var1, var4, false);
            var6.apply(var1, var9, var8, var2, var5);
         } else if (var2 != null) {
            for(int var7 = 0; var7 < var2.size(); ++var7) {
               var5.add(this.transformControlPoint(var1, (mxPoint)var2.get(var7)));
            }
         }

         var5.add(var1.getAbsolutePoint(var1.getAbsolutePointCount() - 1));
         var1.setAbsolutePoints(var5);
      }

   }

   public mxPoint transformControlPoint(mxCellState var1, mxPoint var2) {
      mxPoint var3 = var1.getOrigin();
      return new mxPoint(this.scale * (var2.getX() + this.translate.getX() + var3.getX()), this.scale * (var2.getY() + this.translate.getY() + var3.getY()));
   }

   public mxEdgeStyle.mxEdgeStyleFunction getEdgeStyle(mxCellState var1, List var2, Object var3, Object var4) {
      Object var5 = null;
      if (var3 != null && var3 == var4) {
         var5 = var1.getStyle().get(mxConstants.STYLE_LOOP);
         if (var5 == null) {
            var5 = this.graph.getDefaultLoopStyle();
         }
      } else if (!mxUtils.isTrue(var1.getStyle(), mxConstants.STYLE_NOEDGESTYLE, false)) {
         var5 = var1.getStyle().get(mxConstants.STYLE_EDGE);
      }

      if (var5 instanceof String) {
         String var6 = String.valueOf(var5);
         Object var7 = mxStyleRegistry.getValue(var6);
         if (var7 == null) {
            var7 = mxUtils.eval(var6);
         }

         var5 = var7;
      }

      return var5 instanceof mxEdgeStyle.mxEdgeStyleFunction ? (mxEdgeStyle.mxEdgeStyleFunction)var5 : null;
   }

   public void updateFloatingTerminalPoints(mxCellState var1, mxCellState var2, mxCellState var3) {
      mxPoint var4 = var1.getAbsolutePoint(0);
      mxPoint var5 = var1.getAbsolutePoint(var1.getAbsolutePointCount() - 1);
      if (var5 == null && var3 != null) {
         this.updateFloatingTerminalPoint(var1, var3, var2, false);
      }

      if (var4 == null && var2 != null) {
         this.updateFloatingTerminalPoint(var1, var2, var3, true);
      }

   }

   public void updateFloatingTerminalPoint(mxCellState var1, mxCellState var2, mxCellState var3, boolean var4) {
      var2 = this.getTerminalPort(var1, var2, var4);
      mxPoint var5 = this.getNextPoint(var1, var3, var4);
      double var6 = mxUtils.getDouble(var1.getStyle(), mxConstants.STYLE_PERIMETER_SPACING);
      var6 += mxUtils.getDouble(var1.getStyle(), var4 ? mxConstants.STYLE_SOURCE_PERIMETER_SPACING : mxConstants.STYLE_TARGET_PERIMETER_SPACING);
      mxPoint var8 = this.getPerimeterPoint(var2, var5, this.graph.isOrthogonal(var1), var6);
      var1.setAbsoluteTerminalPoint(var8, var4);
   }

   public mxCellState getTerminalPort(mxCellState var1, mxCellState var2, boolean var3) {
      String var4 = var3 ? mxConstants.STYLE_SOURCE_PORT : mxConstants.STYLE_TARGET_PORT;
      String var5 = mxUtils.getString(var1.style, var4);
      if (var5 != null && this.graph.getModel() instanceof mxGraphModel) {
         mxCellState var6 = this.getState(((mxGraphModel)this.graph.getModel()).getCell(var5));
         if (var6 != null) {
            var2 = var6;
         }
      }

      return var2;
   }

   public mxPoint getPerimeterPoint(mxCellState var1, mxPoint var2, boolean var3) {
      return this.getPerimeterPoint(var1, var2, var3, 0.0);
   }

   public mxPoint getPerimeterPoint(mxCellState var1, mxPoint var2, boolean var3, double var4) {
      mxPoint var6 = null;
      if (var1 != null) {
         mxPerimeter.mxPerimeterFunction var7 = this.getPerimeterFunction(var1);
         if (var7 != null && var2 != null) {
            mxRectangle var8 = this.getPerimeterBounds(var1, var4);
            if (var8.getWidth() > 0.0 || var8.getHeight() > 0.0) {
               var6 = var7.apply(var8, var1, var2, var3);
            }
         }

         if (var6 == null) {
            var6 = this.getPoint(var1);
         }
      }

      return var6;
   }

   public double getRoutingCenterX(mxCellState var1) {
      float var2 = var1.getStyle() != null ? mxUtils.getFloat(var1.getStyle(), mxConstants.STYLE_ROUTING_CENTER_X) : 0.0F;
      return var1.getCenterX() + (double)var2 * var1.getWidth();
   }

   public double getRoutingCenterY(mxCellState var1) {
      float var2 = var1.getStyle() != null ? mxUtils.getFloat(var1.getStyle(), mxConstants.STYLE_ROUTING_CENTER_Y) : 0.0F;
      return var1.getCenterY() + (double)var2 * var1.getHeight();
   }

   public mxRectangle getPerimeterBounds(mxCellState var1, double var2) {
      if (var1 != null) {
         var2 += mxUtils.getDouble(var1.getStyle(), mxConstants.STYLE_PERIMETER_SPACING);
      }

      return var1.getPerimeterBounds(var2 * this.scale);
   }

   public mxPerimeter.mxPerimeterFunction getPerimeterFunction(mxCellState var1) {
      Object var2 = var1.getStyle().get(mxConstants.STYLE_PERIMETER);
      if (var2 instanceof String) {
         String var3 = String.valueOf(var2);
         Object var4 = mxStyleRegistry.getValue(var3);
         if (var4 == null) {
            var4 = mxUtils.eval(var3);
         }

         var2 = var4;
      }

      return var2 instanceof mxPerimeter.mxPerimeterFunction ? (mxPerimeter.mxPerimeterFunction)var2 : null;
   }

   public mxPoint getNextPoint(mxCellState var1, mxCellState var2, boolean var3) {
      List var4 = var1.getAbsolutePoints();
      mxPoint var5 = null;
      if (var4 != null && (var3 || var4.size() > 2 || var2 == null)) {
         int var6 = var4.size();
         int var7 = var3 ? Math.min(1, var6 - 1) : Math.max(0, var6 - 2);
         var5 = (mxPoint)var4.get(var7);
      }

      if (var5 == null && var2 != null) {
         var5 = new mxPoint(var2.getCenterX(), var2.getCenterY());
      }

      return var5;
   }

   public Object getVisibleTerminal(Object var1, boolean var2) {
      mxIGraphModel var3 = this.graph.getModel();
      Object var4 = var3.getTerminal(var1, var2);

      Object var5;
      for(var5 = var4; var4 != null && var4 != this.currentRoot; var4 = var3.getParent(var4)) {
         if (!this.graph.isCellVisible(var5) || this.graph.isCellCollapsed(var4)) {
            var5 = var4;
         }
      }

      if (var3.getParent(var5) == var3.getRoot()) {
         var5 = null;
      }

      return var5;
   }

   public void updateEdgeBounds(mxCellState var1) {
      List var2 = var1.getAbsolutePoints();
      if (var2 != null && var2.size() > 0) {
         mxPoint var3 = (mxPoint)var2.get(0);
         mxPoint var4 = (mxPoint)var2.get(var2.size() - 1);
         if (var3 != null && var4 != null) {
            double var5;
            if (var3.getX() == var4.getX() && var3.getY() == var4.getY()) {
               var1.setTerminalDistance(0.0);
            } else {
               var5 = var4.getX() - var3.getX();
               double var7 = var4.getY() - var3.getY();
               var1.setTerminalDistance(Math.sqrt(var5 * var5 + var7 * var7));
            }

            var5 = 0.0;
            double[] var25 = new double[var2.size() - 1];
            mxPoint var8 = var3;
            if (var3 != null) {
               double var9 = var3.getX();
               double var11 = var3.getY();
               double var13 = var9;
               double var15 = var11;

               for(int var17 = 1; var17 < var2.size(); ++var17) {
                  mxPoint var18 = (mxPoint)var2.get(var17);
                  if (var18 != null) {
                     double var19 = var8.getX() - var18.getX();
                     double var21 = var8.getY() - var18.getY();
                     double var23 = Math.sqrt(var19 * var19 + var21 * var21);
                     var25[var17 - 1] = var23;
                     var5 += var23;
                     var8 = var18;
                     var9 = Math.min(var18.getX(), var9);
                     var11 = Math.min(var18.getY(), var11);
                     var13 = Math.max(var18.getX(), var13);
                     var15 = Math.max(var18.getY(), var15);
                  }
               }

               var1.setLength(var5);
               var1.setSegments(var25);
               double var26 = 1.0;
               var1.setX(var9);
               var1.setY(var11);
               var1.setWidth(Math.max(var26, var13 - var9));
               var1.setHeight(Math.max(var26, var15 - var11));
            } else {
               var1.setLength(0.0);
            }
         } else {
            this.removeState(var1.getCell());
         }
      }

   }

   public mxPoint getPoint(mxCellState var1) {
      return this.getPoint(var1, (mxGeometry)null);
   }

   public mxPoint getPoint(mxCellState var1, mxGeometry var2) {
      double var3 = var1.getCenterX();
      double var5 = var1.getCenterY();
      if (var1.getSegments() == null || var2 != null && !var2.isRelative()) {
         if (var2 != null) {
            mxPoint var37 = var2.getOffset();
            if (var37 != null) {
               var3 += var37.getX();
               var5 += var37.getY();
            }
         }
      } else {
         double var7 = var2 != null ? var2.getX() / 2.0 : 0.0;
         int var9 = var1.getAbsolutePointCount();
         double var10 = (var7 + 0.5) * var1.getLength();
         double[] var12 = var1.getSegments();
         double var13 = var12[0];
         double var15 = 0.0;

         int var17;
         for(var17 = 1; var10 > var15 + var13 && var17 < var9 - 1; var13 = var12[var17++]) {
            var15 += var13;
         }

         if (var13 != 0.0) {
            double var18 = (var10 - var15) / var13;
            mxPoint var20 = var1.getAbsolutePoint(var17 - 1);
            mxPoint var21 = var1.getAbsolutePoint(var17);
            if (var20 != null && var21 != null) {
               double var22 = 0.0;
               double var24 = 0.0;
               double var26 = 0.0;
               if (var2 != null) {
                  var22 = var2.getY();
                  mxPoint var28 = var2.getOffset();
                  if (var28 != null) {
                     var24 = var28.getX();
                     var26 = var28.getY();
                  }
               }

               double var36 = var21.getX() - var20.getX();
               double var30 = var21.getY() - var20.getY();
               double var32 = var30 / var13;
               double var34 = var36 / var13;
               var3 = var20.getX() + var36 * var18 + (var32 * var22 + var24) * this.scale;
               var5 = var20.getY() + var30 * var18 - (var34 * var22 - var26) * this.scale;
            }
         }
      }

      return new mxPoint(var3, var5);
   }

   public mxPoint getRelativePoint(mxCellState var1, double var2, double var4) {
      mxIGraphModel var6 = this.graph.getModel();
      mxGeometry var7 = var6.getGeometry(var1.getCell());
      if (var7 != null) {
         int var8 = var1.getAbsolutePointCount();
         if (var7.isRelative() && var8 > 1) {
            double var9 = var1.getLength();
            double[] var11 = var1.getSegments();
            mxPoint var12 = var1.getAbsolutePoint(0);
            mxPoint var13 = var1.getAbsolutePoint(1);
            Line2D.Double var14 = new Line2D.Double(var12.getPoint(), var13.getPoint());
            double var15 = var14.ptSegDistSq(var2, var4);
            int var17 = 0;
            double var18 = 0.0;
            double var20 = 0.0;

            for(int var22 = 2; var22 < var8; ++var22) {
               var18 += var11[var22 - 2];
               var13 = var1.getAbsolutePoint(var22);
               var14 = new Line2D.Double(var12.getPoint(), var13.getPoint());
               double var23 = var14.ptSegDistSq(var2, var4);
               if (var23 < var15) {
                  var15 = var23;
                  var17 = var22 - 1;
                  var20 = var18;
               }

               var12 = var13;
            }

            double var49 = var11[var17];
            var12 = var1.getAbsolutePoint(var17);
            var13 = var1.getAbsolutePoint(var17 + 1);
            double var24 = var12.getX();
            double var26 = var12.getY();
            double var28 = var13.getX();
            double var30 = var13.getY();
            double var36 = var24 - var28;
            double var38 = var26 - var30;
            double var32 = var2 - var28;
            double var34 = var4 - var30;
            double var40 = 0.0;
            var32 = var36 - var32;
            var34 = var38 - var34;
            double var42 = var32 * var36 + var34 * var38;
            if (var42 <= 0.0) {
               var40 = 0.0;
            } else {
               var40 = var42 * var42 / (var36 * var36 + var38 * var38);
            }

            double var44 = Math.sqrt(var40);
            if (var44 > var49) {
               var44 = var49;
            }

            double var46 = Line2D.ptLineDist(var12.getX(), var12.getY(), var13.getX(), var13.getY(), var2, var4);
            int var48 = Line2D.relativeCCW(var12.getX(), var12.getY(), var13.getX(), var13.getY(), var2, var4);
            if (var48 == -1) {
               var46 = -var46;
            }

            return new mxPoint((double)Math.round((var9 / 2.0 - var20 - var44) / var9 * -2.0), (double)Math.round(var46 / this.scale));
         }
      }

      return new mxPoint();
   }

   public mxCellState[] getCellStates(Object[] var1) {
      ArrayList var2 = new ArrayList(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         mxCellState var4 = this.getState(var1[var3]);
         if (var4 != null) {
            var2.add(var4);
         }
      }

      mxCellState[] var5 = new mxCellState[var2.size()];
      return (mxCellState[])var2.toArray(var5);
   }

   public mxCellState getState(Object var1) {
      return this.getState(var1, false);
   }

   public mxCellState getState(Object var1, boolean var2) {
      mxCellState var3 = null;
      if (var1 != null) {
         var3 = (mxCellState)this.states.get(var1);
         if (var3 == null && var2 && this.graph.isCellVisible(var1)) {
            var3 = this.createState(var1);
            this.states.put(var1, var3);
         }
      }

      return var3;
   }

   public mxCellState removeState(Object var1) {
      return var1 != null ? (mxCellState)this.states.remove(var1) : null;
   }

   public mxCellState createState(Object var1) {
      return new mxCellState(this, var1, this.graph.getCellStyle(var1));
   }

   public static class mxCurrentRootChange implements mxUndoableEdit.mxUndoableChange {
      protected mxGraphView view;
      protected Object root;
      protected Object previous;
      protected boolean up;

      public mxCurrentRootChange(mxGraphView var1, Object var2) {
         this.view = var1;
         this.root = var2;
         this.previous = this.root;
         this.up = var2 == null;
         if (!this.up) {
            Object var3 = var1.getCurrentRoot();

            for(mxIGraphModel var4 = var1.graph.getModel(); var3 != null; var3 = var4.getParent(var3)) {
               if (var3 == var2) {
                  this.up = true;
                  break;
               }
            }
         }

      }

      public mxGraphView getView() {
         return this.view;
      }

      public Object getRoot() {
         return this.root;
      }

      public Object getPrevious() {
         return this.previous;
      }

      public boolean isUp() {
         return this.up;
      }

      public void execute() {
         Object var1 = this.view.getCurrentRoot();
         this.view.currentRoot = this.previous;
         this.previous = var1;
         mxPoint var2 = this.view.graph.getTranslateForRoot(this.view.getCurrentRoot());
         if (var2 != null) {
            this.view.translate = new mxPoint(-var2.getX(), var2.getY());
         }

         this.view.reload();
         this.up = !this.up;
         String var3 = this.up ? "up" : "down";
         this.view.fireEvent(new mxEventObject(var3, new Object[]{"root", this.view.currentRoot, "previous", this.previous}));
      }
   }
}

package com.mxgraph.view;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Element;

public class mxGraph extends mxEventSource {
   public static final String VERSION = "1.4.0.9";
   protected PropertyChangeSupport changeSupport;
   protected mxIGraphModel model;
   protected mxGraphView view;
   protected mxStylesheet stylesheet;
   protected mxGraphSelectionModel selectionModel;
   protected int gridSize;
   protected boolean gridEnabled;
   protected double defaultOverlap;
   protected Object defaultParent;
   protected String alternateEdgeStyle;
   protected boolean enabled;
   protected boolean cellsLocked;
   protected boolean cellsEditable;
   protected boolean cellsResizable;
   protected boolean cellsMovable;
   protected boolean cellsBendable;
   protected boolean cellsSelectable;
   protected boolean cellsDeletable;
   protected boolean cellsCloneable;
   protected boolean cellsDisconnectable;
   protected boolean labelsClipped;
   protected boolean edgeLabelsMovable;
   protected boolean vertexLabelsMovable;
   protected boolean dropEnabled;
   protected boolean splitEnabled;
   protected boolean autoSizeCells;
   protected mxRectangle maximumGraphBounds;
   protected mxRectangle minimumGraphSize;
   protected int border;
   protected boolean keepEdgesInForeground;
   protected boolean keepEdgesInBackground;
   protected boolean collapseToPreferredSize;
   protected boolean allowNegativeCoordinates;
   protected boolean constrainChildren;
   protected boolean extendParents;
   protected boolean extendParentsOnAdd;
   protected boolean resetViewOnRootChange;
   protected boolean resetEdgesOnResize;
   protected boolean resetEdgesOnMove;
   protected boolean resetEdgesOnConnect;
   protected boolean allowLoops;
   protected mxMultiplicity[] multiplicities;
   protected mxEdgeStyle.mxEdgeStyleFunction defaultLoopStyle;
   protected boolean multigraph;
   protected boolean connectableEdges;
   protected boolean allowDanglingEdges;
   protected boolean cloneInvalidEdges;
   protected boolean disconnectOnMove;
   protected boolean labelsVisible;
   protected boolean htmlLabels;
   protected boolean swimlaneNesting;
   protected int changesRepaintThreshold;
   protected boolean autoOrigin;
   protected mxPoint origin;
   protected mxEventSource.mxIEventListener fullRepaintHandler;
   protected mxEventSource.mxIEventListener graphModelChangeHandler;

   public mxGraph() {
      this((mxIGraphModel)null, (mxStylesheet)null);
   }

   public mxGraph(mxIGraphModel var1) {
      this(var1, (mxStylesheet)null);
   }

   public mxGraph(mxStylesheet var1) {
      this((mxIGraphModel)null, var1);
   }

   public mxGraph(mxIGraphModel var1, mxStylesheet var2) {
      this.changeSupport = new PropertyChangeSupport(this);
      this.gridSize = 10;
      this.gridEnabled = true;
      this.defaultOverlap = 0.5;
      this.enabled = true;
      this.cellsLocked = false;
      this.cellsEditable = true;
      this.cellsResizable = true;
      this.cellsMovable = true;
      this.cellsBendable = true;
      this.cellsSelectable = true;
      this.cellsDeletable = true;
      this.cellsCloneable = true;
      this.cellsDisconnectable = true;
      this.labelsClipped = false;
      this.edgeLabelsMovable = true;
      this.vertexLabelsMovable = false;
      this.dropEnabled = true;
      this.splitEnabled = true;
      this.autoSizeCells = false;
      this.maximumGraphBounds = null;
      this.minimumGraphSize = null;
      this.border = 0;
      this.keepEdgesInForeground = false;
      this.keepEdgesInBackground = false;
      this.collapseToPreferredSize = true;
      this.allowNegativeCoordinates = true;
      this.constrainChildren = true;
      this.extendParents = true;
      this.extendParentsOnAdd = true;
      this.resetViewOnRootChange = true;
      this.resetEdgesOnResize = false;
      this.resetEdgesOnMove = false;
      this.resetEdgesOnConnect = true;
      this.allowLoops = false;
      this.defaultLoopStyle = mxEdgeStyle.Loop;
      this.multigraph = true;
      this.connectableEdges = false;
      this.allowDanglingEdges = true;
      this.cloneInvalidEdges = false;
      this.disconnectOnMove = true;
      this.labelsVisible = true;
      this.htmlLabels = false;
      this.swimlaneNesting = true;
      this.changesRepaintThreshold = 1000;
      this.autoOrigin = false;
      this.origin = new mxPoint();
      this.fullRepaintHandler = new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            mxGraph.this.repaint();
         }
      };
      this.graphModelChangeHandler = new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            mxRectangle var3 = mxGraph.this.graphModelChanged((mxIGraphModel)var1, (List)var2.getProperty("changes"));
            mxGraph.this.repaint(var3);
         }
      };
      this.selectionModel = this.createSelectionModel();
      this.setModel((mxIGraphModel)(var1 != null ? var1 : new mxGraphModel()));
      this.setStylesheet(var2 != null ? var2 : this.createStylesheet());
      this.setView(this.createGraphView());
   }

   protected mxGraphSelectionModel createSelectionModel() {
      return new mxGraphSelectionModel(this);
   }

   protected mxStylesheet createStylesheet() {
      return new mxStylesheet();
   }

   protected mxGraphView createGraphView() {
      return new mxGraphView(this);
   }

   public mxIGraphModel getModel() {
      return this.model;
   }

   public void setModel(mxIGraphModel var1) {
      if (this.model != null) {
         this.model.removeListener(this.graphModelChangeHandler);
      }

      mxIGraphModel var2 = this.model;
      this.model = var1;
      if (this.view != null) {
         this.view.revalidate();
      }

      this.model.addListener("change", this.graphModelChangeHandler);
      this.changeSupport.firePropertyChange("model", var2, this.model);
      this.repaint();
   }

   public mxGraphView getView() {
      return this.view;
   }

   public void setView(mxGraphView var1) {
      if (this.view != null) {
         this.view.removeListener(this.fullRepaintHandler);
      }

      mxGraphView var2 = this.view;
      this.view = var1;
      if (this.view != null) {
         this.view.revalidate();
      }

      this.view.addListener("scale", this.fullRepaintHandler);
      this.view.addListener("translate", this.fullRepaintHandler);
      this.view.addListener("scaleAndTranslate", this.fullRepaintHandler);
      this.view.addListener("up", this.fullRepaintHandler);
      this.view.addListener("down", this.fullRepaintHandler);
      this.changeSupport.firePropertyChange("view", var2, this.view);
   }

   public mxStylesheet getStylesheet() {
      return this.stylesheet;
   }

   public void setStylesheet(mxStylesheet var1) {
      mxStylesheet var2 = this.stylesheet;
      this.stylesheet = var1;
      this.changeSupport.firePropertyChange("stylesheet", var2, this.stylesheet);
   }

   public Object[] getSelectionCellsForChanges(List var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         if (var4 instanceof mxGraphModel.mxChildChange) {
            var2.add(((mxGraphModel.mxChildChange)var4).getChild());
         } else if (var4 instanceof mxGraphModel.mxTerminalChange) {
            var2.add(((mxGraphModel.mxTerminalChange)var4).getCell());
         } else if (var4 instanceof mxGraphModel.mxValueChange) {
            var2.add(((mxGraphModel.mxValueChange)var4).getCell());
         } else if (var4 instanceof mxGraphModel.mxStyleChange) {
            var2.add(((mxGraphModel.mxStyleChange)var4).getCell());
         } else if (var4 instanceof mxGraphModel.mxGeometryChange) {
            var2.add(((mxGraphModel.mxGeometryChange)var4).getCell());
         } else if (var4 instanceof mxGraphModel.mxCollapseChange) {
            var2.add(((mxGraphModel.mxCollapseChange)var4).getCell());
         } else if (var4 instanceof mxGraphModel.mxVisibleChange) {
            mxGraphModel.mxVisibleChange var5 = (mxGraphModel.mxVisibleChange)var4;
            if (var5.isVisible()) {
               var2.add(((mxGraphModel.mxVisibleChange)var4).getCell());
            }
         }
      }

      return mxGraphModel.getTopmostCells(this.model, var2.toArray());
   }

   public mxRectangle graphModelChanged(mxIGraphModel var1, List var2) {
      int var3 = this.getChangesRepaintThreshold();
      boolean var4 = var3 > 0 && var2.size() > var3;
      if (!var4) {
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            if (var5.next() instanceof mxGraphModel.mxRootChange) {
               var4 = true;
               break;
            }
         }
      }

      mxRectangle var7 = this.processChanges(var2, true, var4);
      this.view.validate();
      if (this.isAutoOrigin()) {
         this.updateOrigin();
      }

      if (!var4) {
         mxRectangle var6 = this.processChanges(var2, false, var4);
         if (var6 != null) {
            if (var7 == null) {
               var7 = var6;
            } else {
               var7.add(var6);
            }
         }
      }

      this.removeSelectionCells(this.getRemovedCellsForChanges(var2));
      return var7;
   }

   protected void updateOrigin() {
      mxRectangle var1 = this.getGraphBounds();
      if (var1 != null) {
         double var2 = this.getView().getScale();
         double var4 = var1.getX() / var2 - (double)this.getBorder();
         double var6 = var1.getY() / var2 - (double)this.getBorder();
         double var8;
         double var10;
         mxPoint var12;
         if (var1 == null || !(var4 < 0.0) && !(var6 < 0.0)) {
            if ((var4 > 0.0 || var6 > 0.0) && (this.origin.getX() < 0.0 || this.origin.getY() < 0.0)) {
               var8 = Math.min(-this.origin.getX(), var4);
               var10 = Math.min(-this.origin.getY(), var6);
               this.origin.setX(this.origin.getX() + var8);
               this.origin.setY(this.origin.getY() + var10);
               var12 = this.getView().getTranslate();
               this.getView().setTranslate(new mxPoint(var12.getX() - var8, var12.getY() - var10));
            }
         } else {
            var8 = Math.min(0.0, var4);
            var10 = Math.min(0.0, var6);
            this.origin.setX(this.origin.getX() + var8);
            this.origin.setY(this.origin.getY() + var10);
            var12 = this.getView().getTranslate();
            this.getView().setTranslate(new mxPoint(var12.getX() - var8, var12.getY() - var10));
         }
      }

   }

   public Object[] getRemovedCellsForChanges(List var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         if (var4 instanceof mxGraphModel.mxRootChange) {
            break;
         }

         if (var4 instanceof mxGraphModel.mxChildChange) {
            mxGraphModel.mxChildChange var5 = (mxGraphModel.mxChildChange)var4;
            if (var5.getParent() == null) {
               var2.addAll(mxGraphModel.getDescendants(this.model, var5.getChild()));
            }
         } else if (var4 instanceof mxGraphModel.mxVisibleChange) {
            Object var6 = ((mxGraphModel.mxVisibleChange)var4).getCell();
            var2.addAll(mxGraphModel.getDescendants(this.model, var6));
         }
      }

      return var2.toArray();
   }

   public mxRectangle processChanges(List var1, boolean var2, boolean var3) {
      mxRectangle var4 = null;
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         mxRectangle var6 = this.processChange((mxUndoableEdit.mxUndoableChange)var5.next(), var2, var3);
         if (var4 == null) {
            var4 = var6;
         } else {
            var4.add(var6);
         }
      }

      return var4;
   }

   public mxRectangle processChange(mxUndoableEdit.mxUndoableChange var1, boolean var2, boolean var3) {
      mxRectangle var4 = null;
      if (var1 instanceof mxGraphModel.mxRootChange) {
         var4 = var3 ? null : this.getGraphBounds();
         if (var2) {
            this.clearSelection();
            this.removeStateForCell(((mxGraphModel.mxRootChange)var1).getPrevious());
            if (this.isResetViewOnRootChange()) {
               this.view.setEventsEnabled(false);

               try {
                  this.view.scaleAndTranslate(1.0, 0.0, 0.0);
               } finally {
                  this.view.setEventsEnabled(true);
               }
            }
         }

         this.fireEvent(new mxEventObject(mxEvent.ROOT));
      } else if (var1 instanceof mxGraphModel.mxChildChange) {
         mxGraphModel.mxChildChange var5 = (mxGraphModel.mxChildChange)var1;
         if (!var3) {
            if (var5.getParent() != var5.getPrevious()) {
               if (this.model.isVertex(var5.getParent()) || this.model.isEdge(var5.getParent())) {
                  var4 = this.getBoundingBox(var5.getParent(), true, true);
               }

               if (this.model.isVertex(var5.getPrevious()) || this.model.isEdge(var5.getPrevious())) {
                  if (var4 != null) {
                     var4.add(this.getBoundingBox(var5.getPrevious(), true, true));
                  } else {
                     var4 = this.getBoundingBox(var5.getPrevious(), true, true);
                  }
               }
            }

            if (var4 == null) {
               var4 = this.getBoundingBox(var5.getChild(), true, true);
            }
         }

         if (var2) {
            if (var5.getParent() != null) {
               this.view.clear(var5.getChild(), false, true);
            } else {
               this.removeStateForCell(var5.getChild());
            }
         }
      } else {
         Object var8;
         if (var1 instanceof mxGraphModel.mxTerminalChange) {
            var8 = ((mxGraphModel.mxTerminalChange)var1).getCell();
            if (!var3) {
               var4 = this.getBoundingBox(var8, true);
            }

            if (var2) {
               this.view.invalidate(var8);
            }
         } else if (var1 instanceof mxGraphModel.mxValueChange) {
            var8 = ((mxGraphModel.mxValueChange)var1).getCell();
            if (!var3) {
               var4 = this.getBoundingBox(var8);
            }

            if (var2) {
               this.view.clear(var8, false, false);
            }
         } else if (var1 instanceof mxGraphModel.mxStyleChange) {
            var8 = ((mxGraphModel.mxStyleChange)var1).getCell();
            if (!var3) {
               var4 = this.getBoundingBox(var8, true);
            }

            if (var2) {
               this.view.clear(var8, false, false);
               this.view.invalidate(var8);
            }
         } else if (var1 instanceof mxGraphModel.mxGeometryChange) {
            var8 = ((mxGraphModel.mxGeometryChange)var1).getCell();
            if (!var3) {
               var4 = this.getBoundingBox(var8, true, true);
            }

            if (var2) {
               this.view.invalidate(var8);
            }
         } else if (var1 instanceof mxGraphModel.mxCollapseChange) {
            var8 = ((mxGraphModel.mxCollapseChange)var1).getCell();
            if (!var3) {
               var4 = this.getBoundingBox(((mxGraphModel.mxCollapseChange)var1).getCell(), true, true);
            }

            if (var2) {
               this.removeStateForCell(var8);
            }
         } else if (var1 instanceof mxGraphModel.mxVisibleChange) {
            var8 = ((mxGraphModel.mxVisibleChange)var1).getCell();
            if (!var3) {
               var4 = this.getBoundingBox(((mxGraphModel.mxVisibleChange)var1).getCell(), true, true);
            }

            if (var2) {
               this.removeStateForCell(var8);
            }
         }
      }

      return var4;
   }

   protected void removeStateForCell(Object var1) {
      int var2 = this.model.getChildCount(var1);

      for(int var3 = 0; var3 < var2; ++var3) {
         this.removeStateForCell(this.model.getChildAt(var1, var3));
      }

      this.view.removeState(var1);
   }

   public Map getCellStyle(Object var1) {
      Map var2 = this.model.isEdge(var1) ? this.stylesheet.getDefaultEdgeStyle() : this.stylesheet.getDefaultVertexStyle();
      String var3 = this.model.getStyle(var1);
      if (var3 != null) {
         var2 = this.stylesheet.getCellStyle(var3, var2);
      }

      if (var2 == null) {
         var2 = mxStylesheet.EMPTY_STYLE;
      }

      return var2;
   }

   public Object[] setCellStyle(String var1) {
      return this.setCellStyle(var1, (Object[])null);
   }

   public Object[] setCellStyle(String var1, Object[] var2) {
      if (var2 == null) {
         var2 = this.getSelectionCells();
      }

      if (var2 != null) {
         this.model.beginUpdate();

         try {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               this.model.setStyle(var2[var3], var1);
            }
         } finally {
            this.model.endUpdate();
         }
      }

      return var2;
   }

   public Object toggleCellStyle(String var1, boolean var2, Object var3) {
      return this.toggleCellStyles(var1, var2, new Object[]{var3})[0];
   }

   public Object[] toggleCellStyles(String var1, boolean var2) {
      return this.toggleCellStyles(var1, var2, (Object[])null);
   }

   public Object[] toggleCellStyles(String var1, boolean var2, Object[] var3) {
      if (var3 == null) {
         var3 = this.getSelectionCells();
      }

      if (var3 != null && var3.length > 0) {
         mxCellState var4 = this.view.getState(var3[0]);
         Map var5 = var4 != null ? var4.getStyle() : this.getCellStyle(var3[0]);
         if (var5 != null) {
            String var6 = mxUtils.isTrue(var5, var1, var2) ? "0" : "1";
            this.setCellStyles(var1, var6, var3);
         }
      }

      return var3;
   }

   public Object[] setCellStyles(String var1, String var2) {
      return this.setCellStyles(var1, var2, (Object[])null);
   }

   public Object[] setCellStyles(String var1, String var2, Object[] var3) {
      if (var3 == null) {
         var3 = this.getSelectionCells();
      }

      mxUtils.setCellStyles(this.model, var3, var1, var2);
      return var3;
   }

   public Object[] toggleCellStyleFlags(String var1, int var2) {
      return this.toggleCellStyleFlags(var1, var2, (Object[])null);
   }

   public Object[] toggleCellStyleFlags(String var1, int var2, Object[] var3) {
      return this.setCellStyleFlags(var1, var2, (Boolean)null, var3);
   }

   public Object[] setCellStyleFlags(String var1, int var2, boolean var3) {
      return this.setCellStyleFlags(var1, var2, var3, (Object[])null);
   }

   public Object[] setCellStyleFlags(String var1, int var2, Boolean var3, Object[] var4) {
      if (var4 == null) {
         var4 = this.getSelectionCells();
      }

      if (var4 != null && var4.length > 0) {
         if (var3 == null) {
            mxCellState var5 = this.view.getState(var4[0]);
            Map var6 = var5 != null ? var5.getStyle() : this.getCellStyle(var4[0]);
            if (var6 != null) {
               int var7 = mxUtils.getInt(var6, var1);
               var3 = (var7 & var2) != var2;
            }
         }

         mxUtils.setCellStyleFlags(this.model, var4, var1, var2, var3);
      }

      return var4;
   }

   public Object[] alignCells(String var1) {
      return this.alignCells(var1, (Object[])null);
   }

   public Object[] alignCells(String var1, Object[] var2) {
      return this.alignCells(var1, var2, (Object)null);
   }

   public Object[] alignCells(String var1, Object[] var2, Object var3) {
      if (var2 == null) {
         var2 = this.getSelectionCells();
      }

      if (var2 != null && var2.length > 1) {
         if (var3 == null) {
            for(int var4 = 0; var4 < var2.length; ++var4) {
               mxGeometry var5 = this.getCellGeometry(var2[var4]);
               if (var5 != null && !this.model.isEdge(var2[var4])) {
                  if (var3 == null) {
                     if (var1 != null && !var1.equals("left")) {
                        if (var1.equals("center")) {
                           var3 = var5.getX() + var5.getWidth() / 2.0;
                           break;
                        }

                        if (var1.equals("right")) {
                           var3 = var5.getX() + var5.getWidth();
                        } else if (var1.equals("top")) {
                           var3 = var5.getY();
                        } else {
                           if (var1.equals("middle")) {
                              var3 = var5.getY() + var5.getHeight() / 2.0;
                              break;
                           }

                           if (var1.equals("bottom")) {
                              var3 = var5.getY() + var5.getHeight();
                           }
                        }
                     } else {
                        var3 = var5.getX();
                     }
                  } else {
                     double var6 = Double.parseDouble(String.valueOf(var3));
                     if (var1 != null && !var1.equals("left")) {
                        if (var1.equals("right")) {
                           var3 = Math.max(var6, var5.getX() + var5.getWidth());
                        } else if (var1.equals("top")) {
                           var3 = Math.min(var6, var5.getY());
                        } else if (var1.equals("bottom")) {
                           var3 = Math.max(var6, var5.getY() + var5.getHeight());
                        }
                     } else {
                        var3 = Math.min(var6, var5.getX());
                     }
                  }
               }
            }
         }

         this.model.beginUpdate();

         try {
            double var11 = Double.parseDouble(String.valueOf(var3));

            for(int var12 = 0; var12 < var2.length; ++var12) {
               mxGeometry var7 = this.getCellGeometry(var2[var12]);
               if (var7 != null && !this.model.isEdge(var2[var12])) {
                  var7 = (mxGeometry)var7.clone();
                  if (var1 != null && !var1.equals("left")) {
                     if (var1.equals("center")) {
                        var7.setX(var11 - var7.getWidth() / 2.0);
                     } else if (var1.equals("right")) {
                        var7.setX(var11 - var7.getWidth());
                     } else if (var1.equals("top")) {
                        var7.setY(var11);
                     } else if (var1.equals("middle")) {
                        var7.setY(var11 - var7.getHeight() / 2.0);
                     } else if (var1.equals("bottom")) {
                        var7.setY(var11 - var7.getHeight());
                     }
                  } else {
                     var7.setX(var11);
                  }

                  this.model.setGeometry(var2[var12], var7);
                  if (this.isResetEdgesOnMove()) {
                     this.resetEdges(new Object[]{var2[var12]});
                  }
               }
            }

            this.fireEvent(new mxEventObject("alignCells", new Object[]{"cells", var2, "align", var1}));
         } finally {
            this.model.endUpdate();
         }
      }

      return var2;
   }

   public Object flipEdge(Object var1) {
      if (var1 != null && this.alternateEdgeStyle != null) {
         this.model.beginUpdate();

         try {
            String var2 = this.model.getStyle(var1);
            if (var2 != null && var2.length() != 0) {
               this.model.setStyle(var1, (String)null);
            } else {
               this.model.setStyle(var1, this.alternateEdgeStyle);
            }

            this.resetEdge(var1);
            this.fireEvent(new mxEventObject("flipEdge", new Object[]{"edge", var1}));
         } finally {
            this.model.endUpdate();
         }
      }

      return var1;
   }

   public Object[] orderCells(boolean var1) {
      return this.orderCells(var1, (Object[])null);
   }

   public Object[] orderCells(boolean var1, Object[] var2) {
      if (var2 == null) {
         var2 = mxUtils.sortCells(this.getSelectionCells(), true);
      }

      this.model.beginUpdate();

      try {
         this.cellsOrdered(var2, var1);
         this.fireEvent(new mxEventObject("orderCells", new Object[]{"cells", var2, "back", var1}));
      } finally {
         this.model.endUpdate();
      }

      return var2;
   }

   public void cellsOrdered(Object[] var1, boolean var2) {
      if (var1 != null) {
         this.model.beginUpdate();

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               Object var4 = this.model.getParent(var1[var3]);
               if (var2) {
                  this.model.add(var4, var1[var3], var3);
               } else {
                  this.model.add(var4, var1[var3], this.model.getChildCount(var4) - 1);
               }
            }

            this.fireEvent(new mxEventObject("cellsOrdered", new Object[]{"cells", var1, "back", var2}));
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public Object groupCells() {
      return this.groupCells((Object)null);
   }

   public Object groupCells(Object var1) {
      return this.groupCells(var1, 0.0);
   }

   public Object groupCells(Object var1, double var2) {
      return this.groupCells(var1, var2, (Object[])null);
   }

   public Object groupCells(Object var1, double var2, Object[] var4) {
      if (var4 == null) {
         var4 = mxUtils.sortCells(this.getSelectionCells(), true);
      }

      var4 = this.getCellsForGroup(var4);
      if (var1 == null) {
         var1 = this.createGroupCell(var4);
      }

      mxRectangle var5 = this.getBoundsForGroup(var1, var4, var2);
      if (var4.length > 0 && var5 != null) {
         Object var6 = this.model.getParent(var4[0]);
         this.model.beginUpdate();

         try {
            if (this.getCellGeometry(var1) == null) {
               this.model.setGeometry(var1, new mxGeometry());
            }

            int var7 = this.model.getChildCount(var1);
            this.cellsAdded(var4, var1, var7, (Object)null, (Object)null, false);
            this.cellsMoved(var4, -var5.getX(), -var5.getY(), false, true);
            var7 = this.model.getChildCount(var6);
            this.cellsAdded(new Object[]{var1}, var6, var7, (Object)null, (Object)null, false);
            this.cellsResized(new Object[]{var1}, new mxRectangle[]{var5});
            this.fireEvent(new mxEventObject("groupCells", new Object[]{"group", var1, "cells", var4, "border", var2}));
         } finally {
            this.model.endUpdate();
         }
      }

      return var1;
   }

   public Object[] getCellsForGroup(Object[] var1) {
      ArrayList var2 = new ArrayList(var1.length);
      if (var1 != null && var1.length > 0) {
         Object var3 = this.model.getParent(var1[0]);
         var2.add(var1[0]);

         for(int var4 = 1; var4 < var1.length; ++var4) {
            if (this.model.getParent(var1[var4]) == var3) {
               var2.add(var1[var4]);
            }
         }
      }

      return var2.toArray();
   }

   public mxRectangle getBoundsForGroup(Object var1, Object[] var2, double var3) {
      mxRectangle var5 = this.getBoundingBoxFromGeometry(var2);
      if (var5 != null) {
         if (this.isSwimlane(var1)) {
            mxRectangle var6 = this.getStartSize(var1);
            var5.setX(var5.getX() - var6.getWidth());
            var5.setY(var5.getY() - var6.getHeight());
            var5.setWidth(var5.getWidth() + var6.getWidth());
            var5.setHeight(var5.getHeight() + var6.getHeight());
         }

         var5.setX(var5.getX() - var3);
         var5.setY(var5.getY() - var3);
         var5.setWidth(var5.getWidth() + 2.0 * var3);
         var5.setHeight(var5.getHeight() + 2.0 * var3);
      }

      return var5;
   }

   public Object createGroupCell(Object[] var1) {
      mxCell var2 = new mxCell("", new mxGeometry(), (String)null);
      var2.setVertex(true);
      var2.setConnectable(false);
      return var2;
   }

   public Object[] ungroupCells() {
      return this.ungroupCells((Object[])null);
   }

   public Object[] ungroupCells(Object[] var1) {
      ArrayList var2 = new ArrayList();
      if (var1 == null) {
         var1 = this.getSelectionCells();
         ArrayList var3 = new ArrayList(var1.length);

         for(int var4 = 0; var4 < var1.length; ++var4) {
            if (this.model.getChildCount(var1[var4]) > 0) {
               var3.add(var1[var4]);
            }
         }

         var1 = var3.toArray();
      }

      if (var1 != null && var1.length > 0) {
         this.model.beginUpdate();

         try {
            for(int var10 = 0; var10 < var1.length; ++var10) {
               Object[] var11 = mxGraphModel.getChildren(this.model, var1[var10]);
               if (var11 != null && var11.length > 0) {
                  Object var5 = this.model.getParent(var1[var10]);
                  int var6 = this.model.getChildCount(var5);
                  this.cellsAdded(var11, var5, var6, (Object)null, (Object)null, true);
                  var2.addAll(Arrays.asList(var11));
               }
            }

            this.cellsRemoved(this.addAllEdges(var1));
            this.fireEvent(new mxEventObject("ungroupCells", new Object[]{"cells", var1}));
         } finally {
            this.model.endUpdate();
         }
      }

      return var2.toArray();
   }

   public Object[] removeCellsFromParent() {
      return this.removeCellsFromParent((Object[])null);
   }

   public Object[] removeCellsFromParent(Object[] var1) {
      if (var1 == null) {
         var1 = this.getSelectionCells();
      }

      this.model.beginUpdate();

      try {
         Object var2 = this.getDefaultParent();
         int var3 = this.model.getChildCount(var2);
         this.cellsAdded(var1, var2, var3, (Object)null, (Object)null, true);
         this.fireEvent(new mxEventObject("removeCellsFromParent", new Object[]{"cells", var1}));
      } finally {
         this.model.endUpdate();
      }

      return var1;
   }

   public Object[] updateGroupBounds() {
      return this.updateGroupBounds((Object[])null);
   }

   public Object[] updateGroupBounds(Object[] var1) {
      return this.updateGroupBounds(var1, 0);
   }

   public Object[] updateGroupBounds(Object[] var1, int var2) {
      return this.updateGroupBounds(var1, var2, false);
   }

   public Object[] updateGroupBounds(Object[] var1, int var2, boolean var3) {
      if (var1 == null) {
         var1 = this.getSelectionCells();
      }

      this.model.beginUpdate();

      try {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            mxGeometry var5 = this.getCellGeometry(var1[var4]);
            if (var5 != null) {
               Object[] var6 = this.getChildCells(var1[var4]);
               if (var6 != null && var6.length > 0) {
                  mxRectangle var7 = this.getBoundingBoxFromGeometry(var6);
                  if (var7.getWidth() > 0.0 && var7.getHeight() > 0.0) {
                     mxRectangle var8 = this.isSwimlane(var1[var4]) ? this.getStartSize(var1[var4]) : new mxRectangle();
                     var5 = (mxGeometry)var5.clone();
                     if (var3) {
                        var5.setX(var5.getX() + var7.getX() - var8.getWidth() - (double)var2);
                        var5.setY(var5.getY() + var7.getY() - var8.getHeight() - (double)var2);
                     }

                     var5.setWidth(var7.getWidth() + var8.getWidth() + (double)(2 * var2));
                     var5.setHeight(var7.getHeight() + var8.getHeight() + (double)(2 * var2));
                     this.model.setGeometry(var1[var4], var5);
                     this.moveCells(var6, -var7.getX() + var8.getWidth() + (double)var2, -var7.getY() + var8.getHeight() + (double)var2);
                  }
               }
            }
         }
      } finally {
         this.model.endUpdate();
      }

      return var1;
   }

   public Object[] cloneCells(Object[] var1) {
      return this.cloneCells(var1, true);
   }

   public Object[] cloneCells(Object[] var1, boolean var2) {
      Object[] var3 = null;
      if (var1 != null) {
         LinkedHashSet var4 = new LinkedHashSet(var1.length);
         var4.addAll(Arrays.asList(var1));
         if (!var4.isEmpty()) {
            double var5 = this.view.getScale();
            mxPoint var7 = this.view.getTranslate();
            var3 = this.model.cloneCells(var1, true);

            for(int var8 = 0; var8 < var1.length; ++var8) {
               if (!var2 && this.model.isEdge(var3[var8]) && this.getEdgeValidationError(var3[var8], this.model.getTerminal(var3[var8], true), this.model.getTerminal(var3[var8], false)) != null) {
                  var3[var8] = null;
               } else {
                  mxGeometry var9 = this.model.getGeometry(var3[var8]);
                  if (var9 != null) {
                     mxCellState var10 = this.view.getState(var1[var8]);
                     mxCellState var11 = this.view.getState(this.model.getParent(var1[var8]));
                     if (var10 != null && var11 != null) {
                        double var12 = var11.getOrigin().getX();
                        double var14 = var11.getOrigin().getY();
                        if (!this.model.isEdge(var3[var8])) {
                           var9.setX(var9.getX() + var12);
                           var9.setY(var9.getY() + var14);
                        } else {
                           Object var16;
                           for(var16 = this.model.getTerminal(var1[var8], true); var16 != null && !var4.contains(var16); var16 = this.model.getParent(var16)) {
                           }

                           if (var16 == null) {
                              mxPoint var17 = var10.getAbsolutePoint(0);
                              var9.setTerminalPoint(new mxPoint(var17.getX() / var5 - var7.getX(), var17.getY() / var5 - var7.getY()), true);
                           }

                           Object var21;
                           for(var21 = this.model.getTerminal(var1[var8], false); var21 != null && !var4.contains(var21); var21 = this.model.getParent(var21)) {
                           }

                           if (var21 == null) {
                              mxPoint var18 = var10.getAbsolutePoint(var10.getAbsolutePointCount() - 1);
                              var9.setTerminalPoint(new mxPoint(var18.getX() / var5 - var7.getX(), var18.getY() / var5 - var7.getY()), false);
                           }

                           List var22 = var9.getPoints();
                           if (var22 != null) {
                              Iterator var19 = var22.iterator();

                              while(var19.hasNext()) {
                                 mxPoint var20 = (mxPoint)var19.next();
                                 var20.setX(var20.getX() + var12);
                                 var20.setY(var20.getY() + var14);
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            var3 = new Object[0];
         }
      }

      return var3;
   }

   public Object insertVertex(Object var1, String var2, Object var3, double var4, double var6, double var8, double var10) {
      return this.insertVertex(var1, var2, var3, var4, var6, var8, var10, (String)null);
   }

   public Object insertVertex(Object var1, String var2, Object var3, double var4, double var6, double var8, double var10, String var12) {
      Object var13 = this.createVertex(var1, var2, var3, var4, var6, var8, var10, var12);
      return this.addCell(var13, var1);
   }

   public Object createVertex(Object var1, String var2, Object var3, double var4, double var6, double var8, double var10, String var12) {
      mxGeometry var13 = new mxGeometry(var4, var6, var8, var10);
      mxCell var14 = new mxCell(var3, var13, var12);
      var14.setId(var2);
      var14.setVertex(true);
      var14.setConnectable(true);
      return var14;
   }

   public Object insertEdge(Object var1, String var2, Object var3, Object var4, Object var5) {
      return this.insertEdge(var1, var2, var3, var4, var5, (String)null);
   }

   public Object insertEdge(Object var1, String var2, Object var3, Object var4, Object var5, String var6) {
      Object var7 = this.createEdge(var1, var2, var3, var4, var5, var6);
      return this.addEdge(var7, var1, var4, var5, (Integer)null);
   }

   public Object createEdge(Object var1, String var2, Object var3, Object var4, Object var5, String var6) {
      mxCell var7 = new mxCell(var3, new mxGeometry(), var6);
      var7.setId(var2);
      var7.setEdge(true);
      var7.getGeometry().setRelative(true);
      return var7;
   }

   public Object addEdge(Object var1, Object var2, Object var3, Object var4, Integer var5) {
      return this.addCell(var1, var2, var5, var3, var4);
   }

   public Object addCell(Object var1) {
      return this.addCell(var1, (Object)null);
   }

   public Object addCell(Object var1, Object var2) {
      return this.addCell(var1, var2, (Integer)null, (Object)null, (Object)null);
   }

   public Object addCell(Object var1, Object var2, Integer var3, Object var4, Object var5) {
      return this.addCells(new Object[]{var1}, var2, var3, var4, var5)[0];
   }

   public Object[] addCells(Object[] var1) {
      return this.addCells(var1, (Object)null);
   }

   public Object[] addCells(Object[] var1, Object var2) {
      return this.addCells(var1, var2, (Integer)null);
   }

   public Object[] addCells(Object[] var1, Object var2, Integer var3) {
      return this.addCells(var1, var2, var3, (Object)null, (Object)null);
   }

   public Object[] addCells(Object[] var1, Object var2, Integer var3, Object var4, Object var5) {
      if (var2 == null) {
         var2 = this.getDefaultParent();
      }

      if (var3 == null) {
         var3 = this.model.getChildCount(var2);
      }

      this.model.beginUpdate();

      try {
         this.cellsAdded(var1, var2, var3, var4, var5, false);
         this.fireEvent(new mxEventObject("addCells", new Object[]{"cells", var1, "parent", var2, "index", var3, "source", var4, "target", var5}));
      } finally {
         this.model.endUpdate();
      }

      return var1;
   }

   public void cellsAdded(Object[] var1, Object var2, Integer var3, Object var4, Object var5, boolean var6) {
      if (var1 != null && var2 != null && var3 != null) {
         this.model.beginUpdate();

         try {
            mxCellState var7 = var6 ? this.view.getState(var2) : null;
            mxPoint var8 = var7 != null ? var7.getOrigin() : null;
            mxPoint var9 = new mxPoint(0.0, 0.0);

            for(int var10 = 0; var10 < var1.length; ++var10) {
               Object var11 = this.model.getParent(var1[var10]);
               if (var8 != null && var1[var10] != var2 && var2 != var11) {
                  mxCellState var12 = this.view.getState(var11);
                  mxPoint var13 = var12 != null ? var12.getOrigin() : var9;
                  mxGeometry var14 = this.model.getGeometry(var1[var10]);
                  if (var14 != null) {
                     double var15 = var13.getX() - var8.getX();
                     double var17 = var13.getY() - var8.getY();
                     var14 = (mxGeometry)var14.clone();
                     var14.translate(var15, var17);
                     if (!var14.isRelative() && this.model.isVertex(var1[var10]) && !this.isAllowNegativeCoordinates()) {
                        var14.setX(Math.max(0.0, var14.getX()));
                        var14.setY(Math.max(0.0, var14.getY()));
                     }

                     this.model.setGeometry(var1[var10], var14);
                  }
               }

               if (var2 == var11) {
                  var3 = var3 - 1;
               }

               this.model.add(var2, var1[var10], var3 + var10);
               if (this.isExtendParentsOnAdd() && this.isExtendParent(var1[var10])) {
                  this.extendParent(var1[var10]);
               }

               this.constrainChild(var1[var10]);
               if (var4 != null) {
                  this.cellConnected(var1[var10], var4, true, (mxConnectionConstraint)null);
               }

               if (var5 != null) {
                  this.cellConnected(var1[var10], var5, false, (mxConnectionConstraint)null);
               }
            }

            this.fireEvent(new mxEventObject("cellsAdded", new Object[]{"cells", var1, "parent", var2, "index", var3, "source", var4, "target", var5, "absolute", var6}));
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public Object[] removeCells() {
      return this.removeCells((Object[])null);
   }

   public Object[] removeCells(Object[] var1) {
      return this.removeCells(var1, true);
   }

   public Object[] removeCells(Object[] var1, boolean var2) {
      if (var1 == null) {
         var1 = this.getDeletableCells(this.getSelectionCells());
      }

      if (var2) {
         var1 = this.getDeletableCells(this.addAllEdges(var1));
      }

      this.model.beginUpdate();

      try {
         this.cellsRemoved(var1);
         this.fireEvent(new mxEventObject("removeCells", new Object[]{"cells", var1, "includeEdges", var2}));
      } finally {
         this.model.endUpdate();
      }

      return var1;
   }

   public void cellsRemoved(Object[] var1) {
      if (var1 != null && var1.length > 0) {
         double var2 = this.view.getScale();
         mxPoint var4 = this.view.getTranslate();
         this.model.beginUpdate();

         try {
            for(int var5 = 0; var5 < var1.length; ++var5) {
               HashSet var6 = new HashSet();
               var6.addAll(Arrays.asList(var1));
               Object[] var7 = this.getConnections(var1[var5]);

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  if (!var6.contains(var7[var8])) {
                     mxGeometry var9 = this.model.getGeometry(var7[var8]);
                     if (var9 != null) {
                        mxCellState var10 = this.view.getState(var7[var8]);
                        if (var10 != null) {
                           var9 = (mxGeometry)var9.clone();
                           boolean var11 = this.view.getVisibleTerminal(var7[var8], true) == var1[var5];
                           int var12 = var11 ? 0 : var10.getAbsolutePointCount() - 1;
                           mxPoint var13 = var10.getAbsolutePoint(var12);
                           var9.setTerminalPoint(new mxPoint(var13.getX() / var2 - var4.getX(), var13.getY() / var2 - var4.getY()), var11);
                           this.model.setTerminal(var7[var8], (Object)null, var11);
                           this.model.setGeometry(var7[var8], var9);
                        }
                     }
                  }
               }

               this.model.remove(var1[var5]);
            }

            this.fireEvent(new mxEventObject("cellsRemoved", new Object[]{"cells", var1}));
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public Object splitEdge(Object var1, Object[] var2) {
      return this.splitEdge(var1, var2, (Object)null, 0.0, 0.0);
   }

   public Object splitEdge(Object var1, Object[] var2, double var3, double var5) {
      return this.splitEdge(var1, var2, (Object)null, var3, var5);
   }

   public Object splitEdge(Object var1, Object[] var2, Object var3, double var4, double var6) {
      if (var3 == null) {
         var3 = this.cloneCells(new Object[]{var1})[0];
      }

      Object var8 = this.model.getParent(var1);
      Object var9 = this.model.getTerminal(var1, true);
      this.model.beginUpdate();

      try {
         this.cellsMoved(var2, var4, var6, false, false);
         this.cellsAdded(var2, var8, this.model.getChildCount(var8), (Object)null, (Object)null, true);
         this.cellsAdded(new Object[]{var3}, var8, this.model.getChildCount(var8), var9, var2[0], false);
         this.cellConnected(var1, var2[0], true, (mxConnectionConstraint)null);
         this.fireEvent(new mxEventObject("splitEdge", new Object[]{"edge", var1, "cells", var2, "newEdge", var3, "dx", var4, "dy", var6}));
      } finally {
         this.model.endUpdate();
      }

      return var3;
   }

   public Object[] toggleCells(boolean var1) {
      return this.toggleCells(var1, (Object[])null);
   }

   public Object[] toggleCells(boolean var1, Object[] var2) {
      return this.toggleCells(var1, var2, true);
   }

   public Object[] toggleCells(boolean var1, Object[] var2, boolean var3) {
      if (var2 == null) {
         var2 = this.getSelectionCells();
      }

      if (var3) {
         var2 = this.addAllEdges(var2);
      }

      this.model.beginUpdate();

      try {
         this.cellsToggled(var2, var1);
         this.fireEvent(new mxEventObject("toggleCells", new Object[]{"show", var1, "cells", var2, "includeEdges", var3}));
      } finally {
         this.model.endUpdate();
      }

      return var2;
   }

   public void cellsToggled(Object[] var1, boolean var2) {
      if (var1 != null && var1.length > 0) {
         this.model.beginUpdate();

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               this.model.setVisible(var1[var3], var2);
            }
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public Object[] foldCells(boolean var1) {
      return this.foldCells(var1, false);
   }

   public Object[] foldCells(boolean var1, boolean var2) {
      return this.foldCells(var1, var2, (Object[])null);
   }

   public Object[] foldCells(boolean var1, boolean var2, Object[] var3) {
      if (var3 == null) {
         var3 = this.getFoldableCells(this.getSelectionCells(), var1);
      }

      this.model.beginUpdate();

      try {
         this.cellsFolded(var3, var1, var2);
         this.fireEvent(new mxEventObject("foldCells", new Object[]{"cells", var3, "collapse", var1, "recurse", var2}));
      } finally {
         this.model.endUpdate();
      }

      return var3;
   }

   public void cellsFolded(Object[] var1, boolean var2, boolean var3) {
      if (var1 != null && var1.length > 0) {
         this.model.beginUpdate();

         try {
            for(int var4 = 0; var4 < var1.length; ++var4) {
               if (var2 != this.isCellCollapsed(var1[var4])) {
                  this.model.setCollapsed(var1[var4], var2);
                  this.swapBounds(var1[var4], var2);
                  if (this.isExtendParent(var1[var4])) {
                     this.extendParent(var1[var4]);
                  }

                  if (var3) {
                     Object[] var5 = mxGraphModel.getChildren(this.model, var1[var4]);
                     this.cellsFolded(var5, var2, var3);
                  }
               }
            }

            this.fireEvent(new mxEventObject("cellsFolded", new Object[]{"cells", var1, "collapse", var2, "recurse", var3}));
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public void swapBounds(Object var1, boolean var2) {
      if (var1 != null) {
         mxGeometry var3 = this.model.getGeometry(var1);
         if (var3 != null) {
            var3 = (mxGeometry)var3.clone();
            this.updateAlternateBounds(var1, var3, var2);
            var3.swap();
            this.model.setGeometry(var1, var3);
         }
      }

   }

   public void updateAlternateBounds(Object var1, mxGeometry var2, boolean var3) {
      if (var1 != null && var2 != null) {
         if (var2.getAlternateBounds() == null) {
            Object var4 = null;
            if (this.isCollapseToPreferredSize()) {
               var4 = this.getPreferredSizeForCell(var1);
               if (this.isSwimlane(var1)) {
                  mxRectangle var5 = this.getStartSize(var1);
                  ((mxRectangle)var4).setHeight(Math.max(((mxRectangle)var4).getHeight(), var5.getHeight()));
                  ((mxRectangle)var4).setWidth(Math.max(((mxRectangle)var4).getWidth(), var5.getWidth()));
               }
            }

            if (var4 == null) {
               var4 = var2;
            }

            var2.setAlternateBounds(new mxRectangle(var2.getX(), var2.getY(), ((mxRectangle)var4).getWidth(), ((mxRectangle)var4).getHeight()));
         } else {
            var2.getAlternateBounds().setX(var2.getX());
            var2.getAlternateBounds().setY(var2.getY());
         }
      }

   }

   public Object[] addAllEdges(Object[] var1) {
      ArrayList var2 = new ArrayList(var1.length);
      var2.addAll(Arrays.asList(var1));
      var2.addAll(Arrays.asList(this.getAllEdges(var1)));
      return var2.toArray();
   }

   public Object[] getAllEdges(Object[] var1) {
      ArrayList var2 = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            int var4 = this.model.getEdgeCount(var1[var3]);

            for(int var5 = 0; var5 < var4; ++var5) {
               var2.add(this.model.getEdgeAt(var1[var3], var5));
            }

            Object[] var6 = mxGraphModel.getChildren(this.model, var1[var3]);
            var2.addAll(Arrays.asList(this.getAllEdges(var6)));
         }
      }

      return var2.toArray();
   }

   public Object updateCellSize(Object var1) {
      return this.updateCellSize(var1, false);
   }

   public Object updateCellSize(Object var1, boolean var2) {
      this.model.beginUpdate();

      try {
         this.cellSizeUpdated(var1, var2);
         this.fireEvent(new mxEventObject("updateCellSize", new Object[]{"cell", var1, "ignoreChildren", var2}));
      } finally {
         this.model.endUpdate();
      }

      return var1;
   }

   public void cellSizeUpdated(Object var1, boolean var2) {
      if (var1 != null) {
         this.model.beginUpdate();

         try {
            mxRectangle var3 = this.getPreferredSizeForCell(var1);
            mxGeometry var4 = this.model.getGeometry(var1);
            if (var3 != null && var4 != null) {
               boolean var5 = this.isCellCollapsed(var1);
               var4 = (mxGeometry)var4.clone();
               if (this.isSwimlane(var1)) {
                  mxCellState var6 = this.view.getState(var1);
                  Map var7 = var6 != null ? var6.getStyle() : this.getCellStyle(var1);
                  String var8 = this.model.getStyle(var1);
                  if (var8 == null) {
                     var8 = "";
                  }

                  if (mxUtils.isTrue(var7, mxConstants.STYLE_HORIZONTAL, true)) {
                     var8 = mxUtils.setStyle(var8, mxConstants.STYLE_STARTSIZE, String.valueOf(var3.getHeight() + 8.0));
                     if (var5) {
                        var4.setHeight(var3.getHeight() + 8.0);
                     }

                     var4.setWidth(var3.getWidth());
                  } else {
                     var8 = mxUtils.setStyle(var8, mxConstants.STYLE_STARTSIZE, String.valueOf(var3.getWidth() + 8.0));
                     if (var5) {
                        var4.setWidth(var3.getWidth() + 8.0);
                     }

                     var4.setHeight(var3.getHeight());
                  }

                  this.model.setStyle(var1, var8);
               } else {
                  var4.setWidth(var3.getWidth());
                  var4.setHeight(var3.getHeight());
               }

               if (!var2 && !var5) {
                  mxRectangle var17 = this.view.getBounds(mxGraphModel.getChildren(this.model, var1));
                  if (var17 != null) {
                     mxPoint var18 = this.view.getTranslate();
                     double var19 = this.view.getScale();
                     double var10 = (var17.getX() + var17.getWidth()) / var19 - var4.getX() - var18.getX();
                     double var12 = (var17.getY() + var17.getHeight()) / var19 - var4.getY() - var18.getY();
                     var4.setWidth(Math.max(var4.getWidth(), var10));
                     var4.setHeight(Math.max(var4.getHeight(), var12));
                  }
               }

               this.cellsResized(new Object[]{var1}, new mxRectangle[]{var4});
            }
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public mxRectangle getPreferredSizeForCell(Object var1) {
      mxRectangle var2 = null;
      if (var1 != null) {
         mxCellState var3 = this.view.getState(var1);
         Map var4 = var3 != null ? var3.style : this.getCellStyle(var1);
         if (var4 != null && !this.model.isEdge(var1)) {
            double var5 = 0.0;
            double var7 = 0.0;
            if ((this.getImage(var3) != null || mxUtils.getString(var4, mxConstants.STYLE_IMAGE) != null) && mxUtils.getString(var4, mxConstants.STYLE_SHAPE, "").equals("label")) {
               if (mxUtils.getString(var4, mxConstants.STYLE_VERTICAL_ALIGN, "").equals("middle")) {
                  var5 += mxUtils.getDouble(var4, mxConstants.STYLE_IMAGE_WIDTH, (double)mxConstants.DEFAULT_IMAGESIZE);
               }

               if (mxUtils.getString(var4, mxConstants.STYLE_ALIGN, "").equals("center")) {
                  var7 += mxUtils.getDouble(var4, mxConstants.STYLE_IMAGE_HEIGHT, (double)mxConstants.DEFAULT_IMAGESIZE);
               }
            }

            double var9 = mxUtils.getDouble(var4, mxConstants.STYLE_SPACING);
            var5 += 2.0 * var9;
            var5 += mxUtils.getDouble(var4, mxConstants.STYLE_SPACING_LEFT);
            var5 += mxUtils.getDouble(var4, mxConstants.STYLE_SPACING_RIGHT);
            var7 += 2.0 * var9;
            var7 += mxUtils.getDouble(var4, mxConstants.STYLE_SPACING_TOP);
            var7 += mxUtils.getDouble(var4, mxConstants.STYLE_SPACING_BOTTOM);
            String var11 = this.getLabel(var1);
            if (var11 != null && var11.length() > 0) {
               mxRectangle var19 = mxUtils.getLabelSize(var11, var4, this.isHtmlLabel(var1), 0);
               double var13 = var19.getWidth() + var5;
               double var15 = var19.getHeight() + var7;
               if (!mxUtils.isTrue(var4, mxConstants.STYLE_HORIZONTAL, true)) {
                  double var17 = var15;
                  var15 = var13;
                  var13 = var17;
               }

               if (this.gridEnabled) {
                  var13 = this.snap(var13 + (double)(this.gridSize / 2));
                  var15 = this.snap(var15 + (double)(this.gridSize / 2));
               }

               var2 = new mxRectangle(0.0, 0.0, var13, var15);
            } else {
               double var12 = (double)(4 * this.gridSize);
               var2 = new mxRectangle(0.0, 0.0, var12, var12);
            }
         }
      }

      return var2;
   }

   public Object resizeCell(Object var1, mxRectangle var2) {
      return this.resizeCells(new Object[]{var1}, new mxRectangle[]{var2})[0];
   }

   public Object[] resizeCells(Object[] var1, mxRectangle[] var2) {
      this.model.beginUpdate();

      try {
         this.cellsResized(var1, var2);
         this.fireEvent(new mxEventObject("resizeCells", new Object[]{"cells", var1, "bounds", var2}));
      } finally {
         this.model.endUpdate();
      }

      return var1;
   }

   public void cellsResized(Object[] var1, mxRectangle[] var2) {
      if (var1 != null && var2 != null && var1.length == var2.length) {
         this.model.beginUpdate();

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               mxRectangle var4 = var2[var3];
               mxGeometry var5 = this.model.getGeometry(var1[var3]);
               if (var5 != null && (var5.getX() != var4.getX() || var5.getY() != var4.getY() || var5.getWidth() != var4.getWidth() || var5.getHeight() != var4.getHeight())) {
                  var5 = (mxGeometry)var5.clone();
                  if (var5.isRelative()) {
                     mxPoint var6 = var5.getOffset();
                     if (var6 != null) {
                        var6.setX(var6.getX() + var4.getX() - var5.getX());
                        var6.setY(var6.getY() + var4.getY() - var5.getY());
                     }
                  } else {
                     var5.setX(var4.getX());
                     var5.setY(var4.getY());
                  }

                  var5.setWidth(var4.getWidth());
                  var5.setHeight(var4.getHeight());
                  if (!var5.isRelative() && this.model.isVertex(var1[var3]) && !this.isAllowNegativeCoordinates()) {
                     var5.setX(Math.max(0.0, var5.getX()));
                     var5.setY(Math.max(0.0, var5.getY()));
                  }

                  this.model.setGeometry(var1[var3], var5);
                  if (this.isExtendParent(var1[var3])) {
                     this.extendParent(var1[var3]);
                  }
               }
            }

            if (this.isResetEdgesOnResize()) {
               this.resetEdges(var1);
            }

            this.fireEvent(new mxEventObject("cellsResized", new Object[]{"cells", var1, "bounds", var2}));
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public void extendParent(Object var1) {
      if (var1 != null) {
         Object var2 = this.model.getParent(var1);
         mxGeometry var3 = this.model.getGeometry(var2);
         if (var2 != null && var3 != null && !this.isCellCollapsed(var2)) {
            mxGeometry var4 = this.model.getGeometry(var1);
            if (var4 != null && (var3.getWidth() < var4.getX() + var4.getWidth() || var3.getHeight() < var4.getY() + var4.getHeight())) {
               var3 = (mxGeometry)var3.clone();
               var3.setWidth(Math.max(var3.getWidth(), var4.getX() + var4.getWidth()));
               var3.setHeight(Math.max(var3.getHeight(), var4.getY() + var4.getHeight()));
               this.cellsResized(new Object[]{var2}, new mxRectangle[]{var3});
            }
         }
      }

   }

   public Object[] moveCells(Object[] var1, double var2, double var4) {
      return this.moveCells(var1, var2, var4, false);
   }

   public Object[] moveCells(Object[] var1, double var2, double var4, boolean var6) {
      return this.moveCells(var1, var2, var4, var6, (Object)null, (Point)null);
   }

   public Object[] moveCells(Object[] var1, double var2, double var4, boolean var6, Object var7, Point var8) {
      if (var1 != null && (var2 != 0.0 || var4 != 0.0 || var6 || var7 != null)) {
         this.model.beginUpdate();

         try {
            if (var6) {
               var1 = this.cloneCells(var1, this.isCloneInvalidEdges());
               if (var7 == null) {
                  var7 = this.getDefaultParent();
               }
            }

            this.cellsMoved(var1, var2, var4, !var6 && this.isDisconnectOnMove() && this.isAllowDanglingEdges(), var7 == null);
            if (var7 != null) {
               Integer var9 = this.model.getChildCount(var7);
               this.cellsAdded(var1, var7, var9, (Object)null, (Object)null, true);
            }

            this.fireEvent(new mxEventObject("moveCells", new Object[]{"cells", var1, "dx", var2, "dy", var4, "clone", var6, "target", var7, "location", var8}));
         } finally {
            this.model.endUpdate();
         }
      }

      return var1;
   }

   public void cellsMoved(Object[] var1, double var2, double var4, boolean var6, boolean var7) {
      if (var1 != null && (var2 != 0.0 || var4 != 0.0)) {
         this.model.beginUpdate();

         try {
            if (var6) {
               this.disconnectGraph(var1);
            }

            for(int var8 = 0; var8 < var1.length; ++var8) {
               this.translateCell(var1[var8], var2, var4);
               if (var7) {
                  this.constrainChild(var1[var8]);
               }
            }

            if (this.isResetEdgesOnMove()) {
               this.resetEdges(var1);
            }

            this.fireEvent(new mxEventObject("cellsMoved", new Object[]{"cells", var1, "dx", var2, "dy", var4, "disconnect", var6}));
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public void translateCell(Object var1, double var2, double var4) {
      mxGeometry var6 = this.model.getGeometry(var1);
      if (var6 != null) {
         var6 = (mxGeometry)var6.clone();
         var6.translate(var2, var4);
         if (!var6.isRelative() && this.model.isVertex(var1) && !this.isAllowNegativeCoordinates()) {
            var6.setX(Math.max(0.0, var6.getX()));
            var6.setY(Math.max(0.0, var6.getY()));
         }

         if (var6.isRelative() && !this.model.isEdge(var1)) {
            if (var6.getOffset() == null) {
               var6.setOffset(new mxPoint(var2, var4));
            } else {
               mxPoint var7 = var6.getOffset();
               var7.setX(var7.getX() + var2);
               var7.setY(var7.getY() + var4);
            }
         }

         this.model.setGeometry(var1, var6);
      }

   }

   public mxRectangle getCellContainmentArea(Object var1) {
      if (var1 != null && !this.model.isEdge(var1)) {
         Object var2 = this.model.getParent(var1);
         if (var2 == this.getDefaultParent() || var2 == this.getCurrentRoot()) {
            return this.getMaximumGraphBounds();
         }

         if (var2 != null && var2 != this.getDefaultParent()) {
            mxGeometry var3 = this.model.getGeometry(var2);
            if (var3 != null) {
               double var4 = 0.0;
               double var6 = 0.0;
               double var8 = var3.getWidth();
               double var10 = var3.getHeight();
               if (this.isSwimlane(var2)) {
                  mxRectangle var12 = this.getStartSize(var2);
                  var4 = var12.getWidth();
                  var8 -= var12.getWidth();
                  var6 = var12.getHeight();
                  var10 -= var12.getHeight();
               }

               return new mxRectangle(var4, var6, var8, var10);
            }
         }
      }

      return null;
   }

   public mxRectangle getMaximumGraphBounds() {
      return this.maximumGraphBounds;
   }

   public void setMaximumGraphBounds(mxRectangle var1) {
      mxRectangle var2 = this.maximumGraphBounds;
      this.maximumGraphBounds = var1;
      this.changeSupport.firePropertyChange("maximumGraphBounds", var2, this.maximumGraphBounds);
   }

   public void constrainChild(Object var1) {
      if (var1 != null) {
         mxGeometry var2 = this.model.getGeometry(var1);
         mxRectangle var3 = this.isConstrainChild(var1) ? this.getCellContainmentArea(var1) : this.getMaximumGraphBounds();
         if (var2 != null && var3 != null && !var2.isRelative() && (var2.getX() < var3.getX() || var2.getY() < var3.getY() || var3.getWidth() < var2.getX() + var2.getWidth() || var3.getHeight() < var2.getY() + var2.getHeight())) {
            double var4 = this.getOverlap(var1);
            if (var3.getWidth() > 0.0) {
               var2.setX(Math.min(var2.getX(), var3.getX() + var3.getWidth() - (1.0 - var4) * var2.getWidth()));
            }

            if (var3.getHeight() > 0.0) {
               var2.setY(Math.min(var2.getY(), var3.getY() + var3.getHeight() - (1.0 - var4) * var2.getHeight()));
            }

            var2.setX(Math.max(var2.getX(), var3.getX() - var2.getWidth() * var4));
            var2.setY(Math.max(var2.getY(), var3.getY() - var2.getHeight() * var4));
         }
      }

   }

   public void resetEdges(Object[] var1) {
      if (var1 != null) {
         HashSet var2 = new HashSet(Arrays.asList(var1));
         this.model.beginUpdate();

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               Object[] var4 = mxGraphModel.getEdges(this.model, var1[var3]);
               if (var4 != null) {
                  for(int var5 = 0; var5 < var4.length; ++var5) {
                     Object var6 = this.view.getVisibleTerminal(var4[var5], true);
                     Object var7 = this.view.getVisibleTerminal(var4[var5], false);
                     if (!var2.contains(var6) || !var2.contains(var7)) {
                        this.resetEdge(var4[var5]);
                     }
                  }
               }

               this.resetEdges(mxGraphModel.getChildren(this.model, var1[var3]));
            }
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public Object resetEdge(Object var1) {
      mxGeometry var2 = this.model.getGeometry(var1);
      if (var2 != null) {
         List var3 = var2.getPoints();
         if (var3 != null && !var3.isEmpty()) {
            var2 = (mxGeometry)var2.clone();
            var2.setPoints((List)null);
            this.model.setGeometry(var1, var2);
         }
      }

      return var1;
   }

   public mxConnectionConstraint[] getAllConnectionConstraints(mxCellState var1, boolean var2) {
      return null;
   }

   public mxConnectionConstraint getConnectionConstraint(mxCellState var1, mxCellState var2, boolean var3) {
      mxPoint var4 = null;
      Object var5 = var1.getStyle().get(var3 ? mxConstants.STYLE_EXIT_X : mxConstants.STYLE_ENTRY_X);
      if (var5 != null) {
         Object var6 = var1.getStyle().get(var3 ? mxConstants.STYLE_EXIT_Y : mxConstants.STYLE_ENTRY_Y);
         if (var6 != null) {
            var4 = new mxPoint(Double.parseDouble(var5.toString()), Double.parseDouble(var6.toString()));
         }
      }

      boolean var7 = false;
      if (var4 != null) {
         var7 = mxUtils.isTrue(var1.style, var3 ? mxConstants.STYLE_EXIT_PERIMETER : mxConstants.STYLE_ENTRY_PERIMETER, true);
      }

      return new mxConnectionConstraint(var4, var7);
   }

   public void setConnectionConstraint(Object var1, Object var2, boolean var3, mxConnectionConstraint var4) {
      if (var4 != null) {
         this.model.beginUpdate();

         try {
            Object[] var5 = new Object[]{var1};
            if (var4 != null && var4.point != null) {
               if (var4.point != null) {
                  this.setCellStyles(var3 ? mxConstants.STYLE_EXIT_X : mxConstants.STYLE_ENTRY_X, String.valueOf(var4.point.getX()), var5);
                  this.setCellStyles(var3 ? mxConstants.STYLE_EXIT_Y : mxConstants.STYLE_ENTRY_Y, String.valueOf(var4.point.getY()), var5);
                  if (!var4.perimeter) {
                     this.setCellStyles(var3 ? mxConstants.STYLE_EXIT_PERIMETER : mxConstants.STYLE_ENTRY_PERIMETER, "0", var5);
                  } else {
                     this.setCellStyles(var3 ? mxConstants.STYLE_EXIT_PERIMETER : mxConstants.STYLE_ENTRY_PERIMETER, (String)null, var5);
                  }
               }
            } else {
               this.setCellStyles(var3 ? mxConstants.STYLE_EXIT_X : mxConstants.STYLE_ENTRY_X, (String)null, var5);
               this.setCellStyles(var3 ? mxConstants.STYLE_EXIT_Y : mxConstants.STYLE_ENTRY_Y, (String)null, var5);
               this.setCellStyles(var3 ? mxConstants.STYLE_EXIT_PERIMETER : mxConstants.STYLE_ENTRY_PERIMETER, (String)null, var5);
            }
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public mxPoint getConnectionPoint(mxCellState var1, mxConnectionConstraint var2) {
      mxPoint var3 = null;
      if (var1 != null && var2.point != null) {
         var3 = new mxPoint(var1.getX() + var2.getPoint().getX() * var1.getWidth(), var1.getY() + var2.getPoint().getY() * var1.getHeight());
      }

      if (var3 != null && var2.perimeter) {
         var3 = this.view.getPerimeterPoint(var1, var3, false);
      }

      return var3;
   }

   public Object connectCell(Object var1, Object var2, boolean var3) {
      return this.connectCell(var1, var2, var3, (mxConnectionConstraint)null);
   }

   public Object connectCell(Object var1, Object var2, boolean var3, mxConnectionConstraint var4) {
      this.model.beginUpdate();

      try {
         Object var5 = this.model.getTerminal(var1, var3);
         this.cellConnected(var1, var2, var3, var4);
         this.fireEvent(new mxEventObject("connectCell", new Object[]{"edge", var1, "terminal", var2, "source", var3, "previous", var5}));
      } finally {
         this.model.endUpdate();
      }

      return var1;
   }

   public void cellConnected(Object var1, Object var2, boolean var3, mxConnectionConstraint var4) {
      if (var1 != null) {
         this.model.beginUpdate();

         try {
            Object var5 = this.model.getTerminal(var1, var3);
            this.setConnectionConstraint(var1, var2, var3, var4);
            String var6 = null;
            if (this.isPort(var2) && var2 instanceof mxICell) {
               var6 = ((mxICell)var2).getId();
               var2 = this.getTerminalForPort(var2, var3);
            }

            String var7 = var3 ? mxConstants.STYLE_SOURCE_PORT : mxConstants.STYLE_TARGET_PORT;
            this.setCellStyles(var7, var6, new Object[]{var1});
            this.model.setTerminal(var1, var2, var3);
            if (this.isResetEdgesOnConnect()) {
               this.resetEdge(var1);
            }

            this.fireEvent(new mxEventObject("cellConnected", new Object[]{"edge", var1, "terminal", var2, "source", var3, "previous", var5}));
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public void disconnectGraph(Object[] var1) {
      if (var1 != null) {
         this.model.beginUpdate();

         try {
            double var2 = this.view.getScale();
            mxPoint var4 = this.view.getTranslate();
            HashSet var5 = new HashSet();

            int var6;
            for(var6 = 0; var6 < var1.length; ++var6) {
               var5.add(var1[var6]);
            }

            for(var6 = 0; var6 < var1.length; ++var6) {
               if (this.model.isEdge(var1[var6])) {
                  mxGeometry var7 = this.model.getGeometry(var1[var6]);
                  if (var7 != null) {
                     mxCellState var8 = this.view.getState(var1[var6]);
                     mxCellState var9 = this.view.getState(this.model.getParent(var1[var6]));
                     if (var8 != null && var9 != null) {
                        var7 = (mxGeometry)var7.clone();
                        double var10 = -var9.getOrigin().getX();
                        double var12 = -var9.getOrigin().getY();
                        Object var14 = this.model.getTerminal(var1[var6], true);
                        if (var14 != null && this.isCellDisconnectable(var1[var6], var14, true)) {
                           while(var14 != null && !var5.contains(var14)) {
                              var14 = this.model.getParent(var14);
                           }

                           if (var14 == null) {
                              mxPoint var15 = var8.getAbsolutePoint(0);
                              var7.setTerminalPoint(new mxPoint(var15.getX() / var2 - var4.getX() + var10, var15.getY() / var2 - var4.getY() + var12), true);
                              this.model.setTerminal(var1[var6], (Object)null, true);
                           }
                        }

                        Object var21 = this.model.getTerminal(var1[var6], false);
                        if (var21 != null && this.isCellDisconnectable(var1[var6], var21, false)) {
                           while(var21 != null && !var5.contains(var21)) {
                              var21 = this.model.getParent(var21);
                           }

                           if (var21 == null) {
                              int var16 = var8.getAbsolutePointCount() - 1;
                              mxPoint var17 = var8.getAbsolutePoint(var16);
                              var7.setTerminalPoint(new mxPoint(var17.getX() / var2 - var4.getX() + var10, var17.getY() / var2 - var4.getY() + var12), false);
                              this.model.setTerminal(var1[var6], (Object)null, false);
                           }
                        }
                     }

                     this.model.setGeometry(var1[var6], var7);
                  }
               }
            }
         } finally {
            this.model.endUpdate();
         }
      }

   }

   public Object getCurrentRoot() {
      return this.view.getCurrentRoot();
   }

   public mxPoint getTranslateForRoot(Object var1) {
      return null;
   }

   public boolean isPort(Object var1) {
      return false;
   }

   public Object getTerminalForPort(Object var1, boolean var2) {
      return this.getModel().getParent(var1);
   }

   public mxPoint getChildOffsetForCell(Object var1) {
      return null;
   }

   public void enterGroup() {
      this.enterGroup((Object)null);
   }

   public void enterGroup(Object var1) {
      if (var1 == null) {
         var1 = this.getSelectionCell();
      }

      if (var1 != null && this.isValidRoot(var1)) {
         this.view.setCurrentRoot(var1);
         this.clearSelection();
      }

   }

   public void exitGroup() {
      Object var1 = this.model.getRoot();
      Object var2 = this.getCurrentRoot();
      if (var2 != null) {
         Object var3;
         for(var3 = this.model.getParent(var2); var3 != var1 && !this.isValidRoot(var3) && this.model.getParent(var3) != var1; var3 = this.model.getParent(var3)) {
         }

         if (var3 != var1 && this.model.getParent(var3) != var1) {
            this.view.setCurrentRoot(var3);
         } else {
            this.view.setCurrentRoot((Object)null);
         }

         mxCellState var4 = this.view.getState(var2);
         if (var4 != null) {
            this.setSelectionCell(var2);
         }
      }

   }

   public void home() {
      Object var1 = this.getCurrentRoot();
      if (var1 != null) {
         this.view.setCurrentRoot((Object)null);
         mxCellState var2 = this.view.getState(var1);
         if (var2 != null) {
            this.setSelectionCell(var1);
         }
      }

   }

   public boolean isValidRoot(Object var1) {
      return var1 != null;
   }

   public mxRectangle getGraphBounds() {
      return this.view.getGraphBounds();
   }

   public mxRectangle getCellBounds(Object var1) {
      return this.getCellBounds(var1, false);
   }

   public mxRectangle getCellBounds(Object var1, boolean var2) {
      return this.getCellBounds(var1, var2, false);
   }

   public mxRectangle getCellBounds(Object var1, boolean var2, boolean var3) {
      return this.getCellBounds(var1, var2, var3, false);
   }

   public mxRectangle getBoundingBoxFromGeometry(Object[] var1) {
      mxRectangle var2 = null;
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (this.getModel().isVertex(var1[var3])) {
               mxGeometry var4 = this.getCellGeometry(var1[var3]);
               if (var2 == null) {
                  var2 = new mxRectangle(var4);
               } else {
                  var2.add(var4);
               }
            }
         }
      }

      return var2;
   }

   public mxRectangle getBoundingBox(Object var1) {
      return this.getBoundingBox(var1, false);
   }

   public mxRectangle getBoundingBox(Object var1, boolean var2) {
      return this.getBoundingBox(var1, var2, false);
   }

   public mxRectangle getBoundingBox(Object var1, boolean var2, boolean var3) {
      return this.getCellBounds(var1, var2, var3, true);
   }

   public mxRectangle getPaintBounds(Object[] var1) {
      return this.getBoundsForCells(var1, false, true, true);
   }

   public mxRectangle getBoundsForCells(Object[] var1, boolean var2, boolean var3, boolean var4) {
      mxRectangle var5 = null;
      if (var1 != null && var1.length > 0) {
         for(int var6 = 0; var6 < var1.length; ++var6) {
            mxRectangle var7 = this.getCellBounds(var1[var6], var2, var3, var4);
            if (var7 != null) {
               if (var5 == null) {
                  var5 = new mxRectangle(var7);
               } else {
                  var5.add(var7);
               }
            }
         }
      }

      return var5;
   }

   public mxRectangle getCellBounds(Object var1, boolean var2, boolean var3, boolean var4) {
      mxRectangle var5 = null;
      Object[] var6;
      if (var2) {
         HashSet var7 = new HashSet();
         var7.add(var1);

         HashSet var9;
         for(HashSet var8 = new HashSet(Arrays.asList(this.getEdges(var1))); !var8.isEmpty() && !var7.containsAll(var8); var8 = var9) {
            var7.addAll(var8);
            var9 = new HashSet();
            Iterator var10 = var8.iterator();

            while(var10.hasNext()) {
               Object var11 = var10.next();
               var9.addAll(Arrays.asList(this.getEdges(var11)));
            }
         }

         var6 = var7.toArray();
      } else {
         var6 = new Object[]{var1};
      }

      if (var4) {
         var5 = this.view.getBoundingBox(var6);
      } else {
         var5 = this.view.getBounds(var6);
      }

      if (var3) {
         int var12 = this.model.getChildCount(var1);

         for(int var13 = 0; var13 < var12; ++var13) {
            mxRectangle var14 = this.getCellBounds(this.model.getChildAt(var1, var13), var2, true, var4);
            if (var5 != null) {
               var5.add(var14);
            } else {
               var5 = var14;
            }
         }
      }

      return var5;
   }

   public void refresh() {
      this.view.reload();
      this.repaint();
   }

   public void repaint() {
      this.repaint((mxRectangle)null);
   }

   public void repaint(mxRectangle var1) {
      this.fireEvent(new mxEventObject("repaint", new Object[]{"region", var1}));
   }

   public double snap(double var1) {
      if (this.gridEnabled) {
         var1 = (double)(Math.round(var1 / (double)this.gridSize) * (long)this.gridSize);
      }

      return var1;
   }

   public mxGeometry getCellGeometry(Object var1) {
      return this.model.getGeometry(var1);
   }

   public boolean isCellVisible(Object var1) {
      return this.model.isVisible(var1);
   }

   public boolean isCellCollapsed(Object var1) {
      return this.model.isCollapsed(var1);
   }

   public boolean isCellConnectable(Object var1) {
      return this.model.isConnectable(var1);
   }

   public boolean isOrthogonal(mxCellState var1) {
      if (var1.getStyle().containsKey(mxConstants.STYLE_ORTHOGONAL)) {
         return mxUtils.isTrue(var1.getStyle(), mxConstants.STYLE_ORTHOGONAL);
      } else {
         mxEdgeStyle.mxEdgeStyleFunction var2 = this.view.getEdgeStyle(var1, (List)null, (Object)null, (Object)null);
         return var2 == mxEdgeStyle.ElbowConnector || var2 == mxEdgeStyle.SideToSide || var2 == mxEdgeStyle.TopToBottom || var2 == mxEdgeStyle.EntityRelation;
      }
   }

   public boolean isLoop(mxCellState var1) {
      Object var2 = this.view.getVisibleTerminal(var1.getCell(), true);
      Object var3 = this.view.getVisibleTerminal(var1.getCell(), false);
      return var2 != null && var2 == var3;
   }

   public void setMultiplicities(mxMultiplicity[] var1) {
      mxMultiplicity[] var2 = this.multiplicities;
      this.multiplicities = var1;
      this.changeSupport.firePropertyChange("multiplicities", var2, this.multiplicities);
   }

   public mxMultiplicity[] getMultiplicities() {
      return this.multiplicities;
   }

   public boolean isEdgeValid(Object var1, Object var2, Object var3) {
      return this.getEdgeValidationError(var1, var2, var3) == null;
   }

   public String getEdgeValidationError(Object var1, Object var2, Object var3) {
      if (var1 != null && this.model.getTerminal(var1, true) == null && this.model.getTerminal(var1, false) == null) {
         return null;
      } else if (!this.isAllowLoops() && var2 == var3 && var2 != null) {
         return "";
      } else if (!this.isValidConnection(var2, var3)) {
         return "";
      } else if (var2 != null && var3 != null) {
         StringBuffer var4 = new StringBuffer();
         if (!this.multigraph) {
            Object[] var5 = mxGraphModel.getEdgesBetween(this.model, var2, var3, true);
            if (var5.length > 1 || var5.length == 1 && var5[0] != var1) {
               var4.append(mxResources.get("alreadyConnected", "Already Connected") + "\n");
            }
         }

         int var9 = mxGraphModel.getDirectedEdgeCount(this.model, var2, true, var1);
         int var6 = mxGraphModel.getDirectedEdgeCount(this.model, var3, false, var1);
         if (this.multiplicities != null) {
            for(int var7 = 0; var7 < this.multiplicities.length; ++var7) {
               String var8 = this.multiplicities[var7].check(this, var1, var2, var3, var9, var6);
               if (var8 != null) {
                  var4.append(var8);
               }
            }
         }

         String var10 = this.validateEdge(var1, var2, var3);
         if (var10 != null) {
            var4.append(var10);
         }

         return var4.length() > 0 ? var4.toString() : null;
      } else {
         return this.allowDanglingEdges ? null : "";
      }
   }

   public String validateEdge(Object var1, Object var2, Object var3) {
      return null;
   }

   public String getCellValidationError(Object var1) {
      int var2 = mxGraphModel.getDirectedEdgeCount(this.model, var1, true);
      int var3 = mxGraphModel.getDirectedEdgeCount(this.model, var1, false);
      StringBuffer var4 = new StringBuffer();
      Object var5 = this.model.getValue(var1);

      for(int var6 = 0; var6 < this.multiplicities.length; ++var6) {
         mxMultiplicity var7 = this.multiplicities[var6];
         int var8 = var7.getMaxValue();
         if (var7.source && mxUtils.isNode(var5, var7.type, var7.attr, var7.value) && (var8 == 0 && var2 > 0 || var7.min == 1 && var2 == 0 || var8 == 1 && var2 > 1)) {
            var4.append(var7.countError + '\n');
         } else if (!var7.source && mxUtils.isNode(var5, var7.type, var7.attr, var7.value) && (var8 == 0 && var3 > 0 || var7.min == 1 && var3 == 0 || var8 == 1 && var3 > 1)) {
            var4.append(var7.countError + '\n');
         }
      }

      return var4.length() > 0 ? var4.toString() : null;
   }

   public String validateCell(Object var1, Hashtable var2) {
      return null;
   }

   public boolean isLabelsVisible() {
      return this.labelsVisible;
   }

   public void setLabelsVisible(boolean var1) {
      boolean var2 = this.labelsVisible;
      this.labelsVisible = var1;
      this.changeSupport.firePropertyChange("labelsVisible", var2, this.labelsVisible);
   }

   public void setHtmlLabels(boolean var1) {
      boolean var2 = this.htmlLabels;
      this.htmlLabels = var1;
      this.changeSupport.firePropertyChange("htmlLabels", var2, this.htmlLabels);
   }

   public boolean isHtmlLabels() {
      return this.htmlLabels;
   }

   public String convertValueToString(Object var1) {
      Object var2 = this.model.getValue(var1);
      return var2 != null ? var2.toString() : "";
   }

   public String getLabel(Object var1) {
      String var2 = "";
      if (var1 != null) {
         mxCellState var3 = this.view.getState(var1);
         Map var4 = var3 != null ? var3.getStyle() : this.getCellStyle(var1);
         if (this.labelsVisible && !mxUtils.isTrue(var4, mxConstants.STYLE_NOLABEL, false)) {
            var2 = this.convertValueToString(var1);
         }
      }

      return var2;
   }

   public void cellLabelChanged(Object var1, Object var2, boolean var3) {
      this.model.beginUpdate();

      try {
         this.getModel().setValue(var1, var2);
         if (var3) {
            this.cellSizeUpdated(var1, false);
         }
      } finally {
         this.model.endUpdate();
      }

   }

   public boolean isHtmlLabel(Object var1) {
      return this.isHtmlLabels();
   }

   public String getToolTipForCell(Object var1) {
      return this.convertValueToString(var1);
   }

   public mxRectangle getStartSize(Object var1) {
      mxRectangle var2 = new mxRectangle();
      mxCellState var3 = this.view.getState(var1);
      Map var4 = var3 != null ? var3.getStyle() : this.getCellStyle(var1);
      if (var4 != null) {
         double var5 = mxUtils.getDouble(var4, mxConstants.STYLE_STARTSIZE, (double)mxConstants.DEFAULT_STARTSIZE);
         if (mxUtils.isTrue(var4, mxConstants.STYLE_HORIZONTAL, true)) {
            var2.setHeight(var5);
         } else {
            var2.setWidth(var5);
         }
      }

      return var2;
   }

   public String getImage(mxCellState var1) {
      return var1 != null && var1.getStyle() != null ? mxUtils.getString(var1.getStyle(), mxConstants.STYLE_IMAGE) : null;
   }

   public int getBorder() {
      return this.border;
   }

   public void setBorder(int var1) {
      this.border = var1;
   }

   public mxEdgeStyle.mxEdgeStyleFunction getDefaultLoopStyle() {
      return this.defaultLoopStyle;
   }

   public void setDefaultLoopStyle(mxEdgeStyle.mxEdgeStyleFunction var1) {
      mxEdgeStyle.mxEdgeStyleFunction var2 = this.defaultLoopStyle;
      this.defaultLoopStyle = var1;
      this.changeSupport.firePropertyChange("defaultLoopStyle", var2, this.defaultLoopStyle);
   }

   public boolean isSwimlane(Object var1) {
      if (var1 != null && this.model.getParent(var1) != this.model.getRoot()) {
         mxCellState var2 = this.view.getState(var1);
         Map var3 = var2 != null ? var2.getStyle() : this.getCellStyle(var1);
         if (var3 != null && !this.model.isEdge(var1)) {
            return mxUtils.getString(var3, mxConstants.STYLE_SHAPE, "").equals("swimlane");
         }
      }

      return false;
   }

   public boolean isCellLocked(Object var1) {
      mxGeometry var2 = this.model.getGeometry(var1);
      return this.isCellsLocked() || var2 != null && this.model.isVertex(var1) && var2.isRelative();
   }

   public boolean isCellsLocked() {
      return this.cellsLocked;
   }

   public void setCellsLocked(boolean var1) {
      boolean var2 = this.cellsLocked;
      this.cellsLocked = var1;
      this.changeSupport.firePropertyChange("cellsLocked", var2, this.cellsLocked);
   }

   public boolean isCellEditable(Object var1) {
      return this.isCellsEditable() && !this.isCellLocked(var1);
   }

   public boolean isCellsEditable() {
      return this.cellsEditable;
   }

   public void setCellsEditable(boolean var1) {
      boolean var2 = this.cellsEditable;
      this.cellsEditable = var1;
      this.changeSupport.firePropertyChange("cellsEditable", var2, this.cellsEditable);
   }

   public boolean isCellResizable(Object var1) {
      return this.isCellsResizable() && !this.isCellLocked(var1);
   }

   public boolean isCellsResizable() {
      return this.cellsResizable;
   }

   public void setCellsResizable(boolean var1) {
      boolean var2 = this.cellsResizable;
      this.cellsResizable = var1;
      this.changeSupport.firePropertyChange("cellsResizable", var2, this.cellsResizable);
   }

   public Object[] getMovableCells(Object[] var1) {
      return mxGraphModel.filterCells(var1, new mxGraphModel.Filter() {
         public boolean filter(Object var1) {
            return mxGraph.this.isCellMovable(var1);
         }
      });
   }

   public boolean isCellMovable(Object var1) {
      return this.isCellsMovable() && !this.isCellLocked(var1);
   }

   public boolean isCellsMovable() {
      return this.cellsMovable;
   }

   public void setCellsMovable(boolean var1) {
      boolean var2 = this.cellsMovable;
      this.cellsMovable = var1;
      this.changeSupport.firePropertyChange("cellsMovable", var2, this.cellsMovable);
   }

   public boolean isCellBendable(Object var1) {
      return this.isCellsBendable() && !this.isCellLocked(var1);
   }

   public boolean isCellsBendable() {
      return this.cellsBendable;
   }

   public void setCellsBendable(boolean var1) {
      boolean var2 = this.cellsBendable;
      this.cellsBendable = var1;
      this.changeSupport.firePropertyChange("cellsBendable", var2, this.cellsBendable);
   }

   public boolean isCellSelectable(Object var1) {
      return this.isCellsSelectable();
   }

   public boolean isCellsSelectable() {
      return this.cellsSelectable;
   }

   public void setCellsSelectable(boolean var1) {
      boolean var2 = this.cellsSelectable;
      this.cellsSelectable = var1;
      this.changeSupport.firePropertyChange("cellsSelectable", var2, this.cellsSelectable);
   }

   public Object[] getDeletableCells(Object[] var1) {
      return mxGraphModel.filterCells(var1, new mxGraphModel.Filter() {
         public boolean filter(Object var1) {
            return mxGraph.this.isCellDeletable(var1);
         }
      });
   }

   public boolean isCellDeletable(Object var1) {
      return this.isCellsDeletable();
   }

   public boolean isCellsDeletable() {
      return this.cellsDeletable;
   }

   public void setCellsDeletable(boolean var1) {
      boolean var2 = this.cellsDeletable;
      this.cellsDeletable = var1;
      this.changeSupport.firePropertyChange("cellsDeletable", var2, this.cellsDeletable);
   }

   public Object[] getCloneableCells(Object[] var1) {
      return mxGraphModel.filterCells(var1, new mxGraphModel.Filter() {
         public boolean filter(Object var1) {
            return mxGraph.this.isCellCloneable(var1);
         }
      });
   }

   public boolean isCellCloneable(Object var1) {
      return this.isCellsCloneable();
   }

   public boolean isCellsCloneable() {
      return this.cellsCloneable;
   }

   public void setCellsCloneable(boolean var1) {
      boolean var2 = this.cellsCloneable;
      this.cellsCloneable = var1;
      this.changeSupport.firePropertyChange("cellsCloneable", var2, this.cellsCloneable);
   }

   public boolean isCellDisconnectable(Object var1, Object var2, boolean var3) {
      return this.isCellsDisconnectable() && !this.isCellLocked(var1);
   }

   public boolean isCellsDisconnectable() {
      return this.cellsDisconnectable;
   }

   public void setCellsDisconnectable(boolean var1) {
      boolean var2 = this.cellsDisconnectable;
      this.cellsDisconnectable = var1;
      this.changeSupport.firePropertyChange("cellsDisconnectable", var2, this.cellsDisconnectable);
   }

   public boolean isLabelClipped(Object var1) {
      if (!this.isLabelsClipped()) {
         mxCellState var2 = this.view.getState(var1);
         Map var3 = var2 != null ? var2.getStyle() : this.getCellStyle(var1);
         return var3 != null ? mxUtils.getString(var3, mxConstants.STYLE_OVERFLOW, "").equals("hidden") : false;
      } else {
         return this.isLabelsClipped();
      }
   }

   public boolean isLabelsClipped() {
      return this.labelsClipped;
   }

   public void setLabelsClipped(boolean var1) {
      boolean var2 = this.labelsClipped;
      this.labelsClipped = var1;
      this.changeSupport.firePropertyChange("labelsClipped", var2, this.labelsClipped);
   }

   public boolean isLabelMovable(Object var1) {
      return !this.isCellLocked(var1) && (this.model.isEdge(var1) && this.isEdgeLabelsMovable() || this.model.isVertex(var1) && this.isVertexLabelsMovable());
   }

   public boolean isVertexLabelsMovable() {
      return this.vertexLabelsMovable;
   }

   public void setVertexLabelsMovable(boolean var1) {
      boolean var2 = this.vertexLabelsMovable;
      this.vertexLabelsMovable = var1;
      this.changeSupport.firePropertyChange("vertexLabelsMovable", var2, this.vertexLabelsMovable);
   }

   public boolean isEdgeLabelsMovable() {
      return this.edgeLabelsMovable;
   }

   public void setEdgeLabelsMovable(boolean var1) {
      boolean var2 = this.edgeLabelsMovable;
      this.edgeLabelsMovable = var1;
      this.changeSupport.firePropertyChange("edgeLabelsMovable", var2, this.edgeLabelsMovable);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      boolean var2 = this.enabled;
      this.enabled = var1;
      this.changeSupport.firePropertyChange("enabled", var2, this.enabled);
   }

   public boolean isDropEnabled() {
      return this.dropEnabled;
   }

   public void setDropEnabled(boolean var1) {
      boolean var2 = this.dropEnabled;
      this.dropEnabled = var1;
      this.changeSupport.firePropertyChange("dropEnabled", var2, this.dropEnabled);
   }

   public boolean isSplitEnabled() {
      return this.splitEnabled;
   }

   public void setSplitEnabled(boolean var1) {
      this.splitEnabled = var1;
   }

   public boolean isMultigraph() {
      return this.multigraph;
   }

   public void setMultigraph(boolean var1) {
      boolean var2 = this.multigraph;
      this.multigraph = var1;
      this.changeSupport.firePropertyChange("multigraph", var2, this.multigraph);
   }

   public boolean isSwimlaneNesting() {
      return this.swimlaneNesting;
   }

   public void setSwimlaneNesting(boolean var1) {
      boolean var2 = this.swimlaneNesting;
      this.swimlaneNesting = var1;
      this.changeSupport.firePropertyChange("swimlaneNesting", var2, this.swimlaneNesting);
   }

   public boolean isAllowDanglingEdges() {
      return this.allowDanglingEdges;
   }

   public void setAllowDanglingEdges(boolean var1) {
      boolean var2 = this.allowDanglingEdges;
      this.allowDanglingEdges = var1;
      this.changeSupport.firePropertyChange("allowDanglingEdges", var2, this.allowDanglingEdges);
   }

   public boolean isCloneInvalidEdges() {
      return this.cloneInvalidEdges;
   }

   public void setCloneInvalidEdges(boolean var1) {
      boolean var2 = this.cloneInvalidEdges;
      this.cloneInvalidEdges = var1;
      this.changeSupport.firePropertyChange("cloneInvalidEdges", var2, this.cloneInvalidEdges);
   }

   public boolean isDisconnectOnMove() {
      return this.disconnectOnMove;
   }

   public void setDisconnectOnMove(boolean var1) {
      boolean var2 = this.disconnectOnMove;
      this.disconnectOnMove = var1;
      this.changeSupport.firePropertyChange("disconnectOnMove", var2, this.disconnectOnMove);
   }

   public boolean isAllowLoops() {
      return this.allowLoops;
   }

   public void setAllowLoops(boolean var1) {
      boolean var2 = this.allowLoops;
      this.allowLoops = var1;
      this.changeSupport.firePropertyChange("allowLoops", var2, this.allowLoops);
   }

   public boolean isConnectableEdges() {
      return this.connectableEdges;
   }

   public void setConnectableEdges(boolean var1) {
      boolean var2 = this.connectableEdges;
      this.connectableEdges = var1;
      this.changeSupport.firePropertyChange("connectableEdges", var2, this.connectableEdges);
   }

   public boolean isResetEdgesOnMove() {
      return this.resetEdgesOnMove;
   }

   public void setResetEdgesOnMove(boolean var1) {
      boolean var2 = this.resetEdgesOnMove;
      this.resetEdgesOnMove = var1;
      this.changeSupport.firePropertyChange("resetEdgesOnMove", var2, this.resetEdgesOnMove);
   }

   public boolean isResetViewOnRootChange() {
      return this.resetViewOnRootChange;
   }

   public void setResetViewOnRootChange(boolean var1) {
      boolean var2 = this.resetViewOnRootChange;
      this.resetViewOnRootChange = var1;
      this.changeSupport.firePropertyChange("resetViewOnRootChange", var2, this.resetViewOnRootChange);
   }

   public boolean isResetEdgesOnResize() {
      return this.resetEdgesOnResize;
   }

   public void setResetEdgesOnResize(boolean var1) {
      boolean var2 = this.resetEdgesOnResize;
      this.resetEdgesOnResize = var1;
      this.changeSupport.firePropertyChange("resetEdgesOnResize", var2, this.resetEdgesOnResize);
   }

   public boolean isResetEdgesOnConnect() {
      return this.resetEdgesOnConnect;
   }

   public void setResetEdgesOnConnect(boolean var1) {
      boolean var2 = this.resetEdgesOnConnect;
      this.resetEdgesOnConnect = var1;
      this.changeSupport.firePropertyChange("resetEdgesOnConnect", var2, this.resetEdgesOnResize);
   }

   public boolean isAutoSizeCell(Object var1) {
      return this.isAutoSizeCells();
   }

   public boolean isAutoSizeCells() {
      return this.autoSizeCells;
   }

   public void setAutoSizeCells(boolean var1) {
      boolean var2 = this.autoSizeCells;
      this.autoSizeCells = var1;
      this.changeSupport.firePropertyChange("autoSizeCells", var2, this.autoSizeCells);
   }

   public boolean isExtendParent(Object var1) {
      return !this.getModel().isEdge(var1) && this.isExtendParents();
   }

   public boolean isExtendParents() {
      return this.extendParents;
   }

   public void setExtendParents(boolean var1) {
      boolean var2 = this.extendParents;
      this.extendParents = var1;
      this.changeSupport.firePropertyChange("extendParents", var2, this.extendParents);
   }

   public boolean isExtendParentsOnAdd() {
      return this.extendParentsOnAdd;
   }

   public void setExtendParentsOnAdd(boolean var1) {
      boolean var2 = this.extendParentsOnAdd;
      this.extendParentsOnAdd = var1;
      this.changeSupport.firePropertyChange("extendParentsOnAdd", var2, this.extendParentsOnAdd);
   }

   public boolean isConstrainChild(Object var1) {
      return this.isConstrainChildren();
   }

   public boolean isConstrainChildren() {
      return this.constrainChildren;
   }

   public void setConstrainChildren(boolean var1) {
      boolean var2 = this.constrainChildren;
      this.constrainChildren = var1;
      this.changeSupport.firePropertyChange("constrainChildren", var2, this.constrainChildren);
   }

   public boolean isAutoOrigin() {
      return this.autoOrigin;
   }

   public void setAutoOrigin(boolean var1) {
      boolean var2 = this.autoOrigin;
      this.autoOrigin = var1;
      this.changeSupport.firePropertyChange("autoOrigin", var2, this.autoOrigin);
   }

   public mxPoint getOrigin() {
      return this.origin;
   }

   public void setOrigin(mxPoint var1) {
      mxPoint var2 = this.origin;
      this.origin = var1;
      this.changeSupport.firePropertyChange("origin", var2, this.origin);
   }

   public int getChangesRepaintThreshold() {
      return this.changesRepaintThreshold;
   }

   public void setChangesRepaintThreshold(int var1) {
      int var2 = this.changesRepaintThreshold;
      this.changesRepaintThreshold = var1;
      this.changeSupport.firePropertyChange("changesRepaintThreshold", var2, this.changesRepaintThreshold);
   }

   public boolean isAllowNegativeCoordinates() {
      return this.allowNegativeCoordinates;
   }

   public void setAllowNegativeCoordinates(boolean var1) {
      boolean var2 = this.allowNegativeCoordinates;
      this.allowNegativeCoordinates = var1;
      this.changeSupport.firePropertyChange("allowNegativeCoordinates", var2, this.allowNegativeCoordinates);
   }

   public boolean isCollapseToPreferredSize() {
      return this.collapseToPreferredSize;
   }

   public void setCollapseToPreferredSize(boolean var1) {
      boolean var2 = this.collapseToPreferredSize;
      this.collapseToPreferredSize = var1;
      this.changeSupport.firePropertyChange("collapseToPreferredSize", var2, this.collapseToPreferredSize);
   }

   public boolean isKeepEdgesInForeground() {
      return this.keepEdgesInForeground;
   }

   public void setKeepEdgesInForeground(boolean var1) {
      boolean var2 = this.keepEdgesInForeground;
      this.keepEdgesInForeground = var1;
      this.changeSupport.firePropertyChange("keepEdgesInForeground", var2, this.keepEdgesInForeground);
   }

   public boolean isKeepEdgesInBackground() {
      return this.keepEdgesInBackground;
   }

   public void setKeepEdgesInBackground(boolean var1) {
      boolean var2 = this.keepEdgesInBackground;
      this.keepEdgesInBackground = var1;
      this.changeSupport.firePropertyChange("keepEdgesInBackground", var2, this.keepEdgesInBackground);
   }

   public boolean isValidSource(Object var1) {
      return var1 == null && this.allowDanglingEdges || var1 != null && (!this.model.isEdge(var1) || this.isConnectableEdges()) && this.isCellConnectable(var1);
   }

   public boolean isValidTarget(Object var1) {
      return this.isValidSource(var1);
   }

   public boolean isValidConnection(Object var1, Object var2) {
      return this.isValidSource(var1) && this.isValidTarget(var2) && (this.isAllowLoops() || var1 != var2);
   }

   public mxRectangle getMinimumGraphSize() {
      return this.minimumGraphSize;
   }

   public void setMinimumGraphSize(mxRectangle var1) {
      mxRectangle var2 = this.minimumGraphSize;
      this.minimumGraphSize = var1;
      this.changeSupport.firePropertyChange("minimumGraphSize", var2, var1);
   }

   public double getOverlap(Object var1) {
      return this.isAllowOverlapParent(var1) ? this.getDefaultOverlap() : 0.0;
   }

   public double getDefaultOverlap() {
      return this.defaultOverlap;
   }

   public void setDefaultOverlap(double var1) {
      double var3 = this.defaultOverlap;
      this.defaultOverlap = var1;
      this.changeSupport.firePropertyChange("defaultOverlap", var3, var1);
   }

   public boolean isAllowOverlapParent(Object var1) {
      return false;
   }

   public Object[] getFoldableCells(Object[] var1, final boolean var2) {
      return mxGraphModel.filterCells(var1, new mxGraphModel.Filter() {
         public boolean filter(Object var1) {
            return mxGraph.this.isCellFoldable(var1, var2);
         }
      });
   }

   public boolean isCellFoldable(Object var1, boolean var2) {
      return this.model.getChildCount(var1) > 0;
   }

   public boolean isGridEnabled() {
      return this.gridEnabled;
   }

   public void setGridEnabled(boolean var1) {
      boolean var2 = this.gridEnabled;
      this.gridEnabled = var1;
      this.changeSupport.firePropertyChange("gridEnabled", var2, this.gridSize);
   }

   public int getGridSize() {
      return this.gridSize;
   }

   public void setGridSize(int var1) {
      int var2 = this.gridSize;
      this.gridSize = var1;
      this.changeSupport.firePropertyChange("gridSize", var2, this.gridSize);
   }

   public String getAlternateEdgeStyle() {
      return this.alternateEdgeStyle;
   }

   public void setAlternateEdgeStyle(String var1) {
      String var2 = this.alternateEdgeStyle;
      this.alternateEdgeStyle = var1;
      this.changeSupport.firePropertyChange("alternateEdgeStyle", var2, this.alternateEdgeStyle);
   }

   public boolean isValidDropTarget(Object var1, Object[] var2) {
      return var1 != null && (this.isSplitEnabled() && this.isSplitTarget(var1, var2) || !this.model.isEdge(var1) && (this.isSwimlane(var1) || this.model.getChildCount(var1) > 0 && !this.isCellCollapsed(var1)));
   }

   public boolean isSplitTarget(Object var1, Object[] var2) {
      if (var1 != null && var2 != null && var2.length == 1) {
         Object var3 = this.model.getTerminal(var1, true);
         Object var4 = this.model.getTerminal(var1, false);
         return this.model.isEdge(var1) && this.isCellConnectable(var2[0]) && this.getEdgeValidationError(var1, this.model.getTerminal(var1, true), var2[0]) == null && !this.model.isAncestor(var2[0], var3) && !this.model.isAncestor(var2[0], var4);
      } else {
         return false;
      }
   }

   public Object getDropTarget(Object[] var1, Point var2, Object var3) {
      if (!this.isSwimlaneNesting()) {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            if (this.isSwimlane(var1[var4])) {
               return null;
            }
         }
      }

      Object var6 = null;
      if (var3 == null) {
         var3 = var6;
      } else if (var6 != null) {
         Object var5;
         for(var5 = this.model.getParent(var6); var5 != null && this.isSwimlane(var5) && var5 != var3; var5 = this.model.getParent(var5)) {
         }

         if (var5 == var3) {
            var3 = var6;
         }
      }

      while(var3 != null && !this.isValidDropTarget(var3, var1) && this.model.getParent(var3) != this.model.getRoot()) {
         var3 = this.model.getParent(var3);
      }

      return this.model.getParent(var3) != this.model.getRoot() && !mxUtils.contains(var1, var3) ? var3 : null;
   }

   public Object getDefaultParent() {
      Object var1 = this.defaultParent;
      if (var1 == null) {
         var1 = this.view.getCurrentRoot();
         if (var1 == null) {
            Object var2 = this.model.getRoot();
            var1 = this.model.getChildAt(var2, 0);
         }
      }

      return var1;
   }

   public void setDefaultParent(Object var1) {
      this.defaultParent = var1;
   }

   public Object[] getChildVertices(Object var1) {
      return this.getChildCells(var1, true, false);
   }

   public Object[] getChildEdges(Object var1) {
      return this.getChildCells(var1, false, true);
   }

   public Object[] getChildCells(Object var1) {
      return this.getChildCells(var1, false, false);
   }

   public Object[] getChildCells(Object var1, boolean var2, boolean var3) {
      Object[] var4 = mxGraphModel.getChildCells(this.model, var1, var2, var3);
      ArrayList var5 = new ArrayList(var4.length);

      for(int var6 = 0; var6 < var4.length; ++var6) {
         if (this.isCellVisible(var4[var6])) {
            var5.add(var4[var6]);
         }
      }

      return var5.toArray();
   }

   public Object[] getConnections(Object var1) {
      return this.getConnections(var1, (Object)null);
   }

   public Object[] getConnections(Object var1, Object var2) {
      return this.getEdges(var1, var2, true, true, false);
   }

   public Object[] getIncomingEdges(Object var1) {
      return this.getIncomingEdges(var1, (Object)null);
   }

   public Object[] getIncomingEdges(Object var1, Object var2) {
      return this.getEdges(var1, var2, true, false, false);
   }

   public Object[] getOutgoingEdges(Object var1) {
      return this.getOutgoingEdges(var1, (Object)null);
   }

   public Object[] getOutgoingEdges(Object var1, Object var2) {
      return this.getEdges(var1, var2, false, true, false);
   }

   public Object[] getEdges(Object var1) {
      return this.getEdges(var1, (Object)null);
   }

   public Object[] getEdges(Object var1, Object var2) {
      return this.getEdges(var1, var2, true, true, true);
   }

   public Object[] getEdges(Object var1, Object var2, boolean var3, boolean var4, boolean var5) {
      boolean var6 = this.isCellCollapsed(var1);
      ArrayList var7 = new ArrayList();
      int var8 = this.model.getChildCount(var1);

      for(int var9 = 0; var9 < var8; ++var9) {
         Object var10 = this.model.getChildAt(var1, var9);
         if (var6 || !this.isCellVisible(var10)) {
            var7.addAll(Arrays.asList(mxGraphModel.getEdges(this.model, var10, var3, var4, var5)));
         }
      }

      var7.addAll(Arrays.asList(mxGraphModel.getEdges(this.model, var1, var3, var4, var5)));
      ArrayList var14 = new ArrayList(var7.size());
      Iterator var15 = var7.iterator();

      while(true) {
         Object var11;
         Object var12;
         Object var13;
         do {
            if (!var15.hasNext()) {
               return var14.toArray();
            }

            var11 = var15.next();
            var12 = this.view.getVisibleTerminal(var11, true);
            var13 = this.view.getVisibleTerminal(var11, false);
         } while(!var5 && (var12 == var13 || (!var3 || var13 != var1 || var2 != null && this.model.getParent(var12) != var2) && (!var4 || var12 != var1 || var2 != null && this.model.getParent(var13) != var2)));

         var14.add(var11);
      }
   }

   public Object[] getOpposites(Object[] var1, Object var2) {
      return this.getOpposites(var1, var2, true, true);
   }

   public Object[] getOpposites(Object[] var1, Object var2, boolean var3, boolean var4) {
      LinkedHashSet var5 = new LinkedHashSet();
      if (var1 != null) {
         for(int var6 = 0; var6 < var1.length; ++var6) {
            Object var7 = this.view.getVisibleTerminal(var1[var6], true);
            Object var8 = this.view.getVisibleTerminal(var1[var6], false);
            if (var4 && var7 == var2 && var8 != null && var8 != var2) {
               var5.add(var8);
            } else if (var3 && var8 == var2 && var7 != null && var7 != var2) {
               var5.add(var7);
            }
         }
      }

      return var5.toArray();
   }

   public Object[] getEdgesBetween(Object var1, Object var2) {
      return this.getEdgesBetween(var1, var2, false);
   }

   public Object[] getEdgesBetween(Object var1, Object var2, boolean var3) {
      Object[] var4 = this.getEdges(var1);
      ArrayList var5 = new ArrayList(var4.length);

      for(int var6 = 0; var6 < var4.length; ++var6) {
         Object var7 = this.view.getVisibleTerminal(var4[var6], true);
         Object var8 = this.view.getVisibleTerminal(var4[var6], false);
         if (var7 == var1 && var8 == var2 || !var3 && var7 == var2 && var8 == var1) {
            var5.add(var4[var6]);
         }
      }

      return var5.toArray();
   }

   public Object[] getCellsBeyond(double var1, double var3, Object var5, boolean var6, boolean var7) {
      if (var5 == null) {
         var5 = this.getDefaultParent();
      }

      int var8 = this.model.getChildCount(var5);
      ArrayList var9 = new ArrayList(var8);
      if ((var6 || var7) && var5 != null) {
         for(int var10 = 0; var10 < var8; ++var10) {
            Object var11 = this.model.getChildAt(var5, var10);
            mxCellState var12 = this.view.getState(var11);
            if (this.isCellVisible(var11) && var12 != null && (!var6 || var12.getX() >= var1) && (!var7 || var12.getY() >= var3)) {
               var9.add(var11);
            }
         }
      }

      return var9.toArray();
   }

   public Object[] findTreeRoots(Object var1) {
      return this.findTreeRoots(var1, false);
   }

   public Object[] findTreeRoots(Object var1, boolean var2) {
      return this.findTreeRoots(var1, var2, false);
   }

   public Object[] findTreeRoots(Object var1, boolean var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      if (var1 != null) {
         int var5 = this.model.getChildCount(var1);
         Object var6 = null;
         int var7 = 0;

         for(int var8 = 0; var8 < var5; ++var8) {
            Object var9 = this.model.getChildAt(var1, var8);
            if (this.model.isVertex(var9) && this.isCellVisible(var9)) {
               Object[] var10 = this.getConnections(var9, var2 ? var1 : null);
               int var11 = 0;
               int var12 = 0;

               int var13;
               for(var13 = 0; var13 < var10.length; ++var13) {
                  Object var14 = this.view.getVisibleTerminal(var10[var13], true);
                  if (var14 == var9) {
                     ++var11;
                  } else {
                     ++var12;
                  }
               }

               if (var3 && var11 == 0 && var12 > 0 || !var3 && var12 == 0 && var11 > 0) {
                  var4.add(var9);
               }

               var13 = var3 ? var12 - var11 : var11 - var12;
               if (var13 > var7) {
                  var7 = var13;
                  var6 = var9;
               }
            }
         }

         if (var4.isEmpty() && var6 != null) {
            var4.add(var6);
         }
      }

      return var4.toArray();
   }

   public void traverse(Object var1, boolean var2, mxICellVisitor var3) {
      this.traverse(var1, var2, var3, (Object)null, (Set)null);
   }

   public void traverse(Object var1, boolean var2, mxICellVisitor var3, Object var4, Set var5) {
      if (var1 != null && var3 != null) {
         if (var5 == null) {
            var5 = new HashSet();
         }

         if (!((Set)var5).contains(var1)) {
            ((Set)var5).add(var1);
            if (var3.visit(var1, var4)) {
               int var6 = this.model.getEdgeCount(var1);
               if (var6 > 0) {
                  for(int var7 = 0; var7 < var6; ++var7) {
                     Object var8 = this.model.getEdgeAt(var1, var7);
                     boolean var9 = this.model.getTerminal(var8, true) == var1;
                     if (!var2 || var9) {
                        Object var10 = this.model.getTerminal(var8, !var9);
                        this.traverse(var10, var2, var3, var8, (Set)var5);
                     }
                  }
               }
            }
         }
      }

   }

   public mxGraphSelectionModel getSelectionModel() {
      return this.selectionModel;
   }

   public int getSelectionCount() {
      return this.selectionModel.size();
   }

   public boolean isCellSelected(Object var1) {
      return this.selectionModel.isSelected(var1);
   }

   public boolean isSelectionEmpty() {
      return this.selectionModel.isEmpty();
   }

   public void clearSelection() {
      this.selectionModel.clear();
   }

   public Object getSelectionCell() {
      return this.selectionModel.getCell();
   }

   public void setSelectionCell(Object var1) {
      this.selectionModel.setCell(var1);
   }

   public Object[] getSelectionCells() {
      return this.selectionModel.getCells();
   }

   public void setSelectionCells(Object[] var1) {
      this.selectionModel.setCells(var1);
   }

   public void setSelectionCells(Collection var1) {
      if (var1 != null) {
         this.setSelectionCells(var1.toArray());
      }

   }

   public void addSelectionCell(Object var1) {
      this.selectionModel.addCell(var1);
   }

   public void addSelectionCells(Object[] var1) {
      this.selectionModel.addCells(var1);
   }

   public void removeSelectionCell(Object var1) {
      this.selectionModel.removeCell(var1);
   }

   public void removeSelectionCells(Object[] var1) {
      this.selectionModel.removeCells(var1);
   }

   public void selectNextCell() {
      this.selectCell(true, false, false);
   }

   public void selectPreviousCell() {
      this.selectCell(false, false, false);
   }

   public void selectParentCell() {
      this.selectCell(false, true, false);
   }

   public void selectChildCell() {
      this.selectCell(false, false, true);
   }

   public void selectCell(boolean var1, boolean var2, boolean var3) {
      Object var4 = this.getSelectionCell();
      if (this.getSelectionCount() > 1) {
         this.clearSelection();
      }

      Object var5 = var4 != null ? this.model.getParent(var4) : this.getDefaultParent();
      int var6 = this.model.getChildCount(var5);
      if (var4 == null && var6 > 0) {
         Object var9 = this.model.getChildAt(var5, 0);
         this.setSelectionCell(var9);
      } else if ((var4 == null || var2) && this.view.getState(var5) != null && this.model.getGeometry(var5) != null) {
         if (this.getCurrentRoot() != var5) {
            this.setSelectionCell(var5);
         }
      } else {
         int var7;
         if (var4 != null && var3) {
            var7 = this.model.getChildCount(var4);
            if (var7 > 0) {
               Object var10 = this.model.getChildAt(var4, 0);
               this.setSelectionCell(var10);
            }
         } else if (var6 > 0) {
            var7 = ((mxICell)var5).getIndex((mxICell)var4);
            if (var1) {
               ++var7;
               this.setSelectionCell(this.model.getChildAt(var5, var7 % var6));
            } else {
               --var7;
               int var8 = var7 < 0 ? var6 - 1 : var7;
               this.setSelectionCell(this.model.getChildAt(var5, var8));
            }
         }
      }

   }

   public void selectVertices() {
      this.selectVertices((Object)null);
   }

   public void selectVertices(Object var1) {
      this.selectCells(true, false, var1);
   }

   public void selectEdges() {
      this.selectEdges((Object)null);
   }

   public void selectEdges(Object var1) {
      this.selectCells(false, true, var1);
   }

   public void selectCells(boolean var1, boolean var2) {
      this.selectCells(var1, var2, (Object)null);
   }

   public void selectCells(final boolean var1, final boolean var2, Object var3) {
      if (var3 == null) {
         var3 = this.getDefaultParent();
      }

      Collection var4 = mxGraphModel.filterDescendants(this.getModel(), new mxGraphModel.Filter() {
         public boolean filter(Object var1x) {
            return mxGraph.this.view.getState(var1x) != null && mxGraph.this.model.getChildCount(var1x) == 0 && (mxGraph.this.model.isVertex(var1x) && var1 || mxGraph.this.model.isEdge(var1x) && var2);
         }
      });
      this.setSelectionCells(var4);
   }

   public void selectAll() {
      this.selectAll((Object)null);
   }

   public void selectAll(Object var1) {
      if (var1 == null) {
         var1 = this.getDefaultParent();
      }

      Object[] var2 = mxGraphModel.getChildren(this.model, var1);
      if (var2 != null) {
         this.setSelectionCells(var2);
      }

   }

   public void drawGraph(mxICanvas var1) {
      this.drawCell(var1, this.getModel().getRoot());
   }

   public void drawCell(mxICanvas var1, Object var2) {
      this.drawState(var1, this.getView().getState(var2), this.getLabel(var2));
      int var3 = this.model.getChildCount(var2);

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = this.model.getChildAt(var2, var4);
         this.drawCell(var1, var5);
      }

   }

   public void drawState(mxICanvas var1, mxCellState var2, String var3) {
      Object var4 = var2 != null ? var2.getCell() : null;
      if (var4 != null && var4 != this.view.getCurrentRoot() && var4 != this.model.getRoot() && (this.model.isVertex(var4) || this.model.isEdge(var4))) {
         Object var5 = var1.drawCell(var2);
         Object var6 = null;
         Shape var7 = null;
         Rectangle var8 = var2.getRectangle();
         Object var9 = this.isLabelClipped(var2.getCell()) ? var1 : null;
         if (var9 instanceof mxImageCanvas) {
            var9 = ((mxImageCanvas)var9).getGraphicsCanvas();
         }

         if (var9 instanceof mxGraphics2DCanvas) {
            Graphics2D var10 = ((mxGraphics2DCanvas)var9).getGraphics();
            var7 = var10.getClip();
            var10.setClip(var8);
         }

         if (var3 != null && var2.getLabelBounds() != null) {
            var6 = var1.drawLabel(var3, var2, this.isHtmlLabel(var4));
         }

         if (var9 instanceof mxGraphics2DCanvas) {
            ((mxGraphics2DCanvas)var9).getGraphics().setClip(var7);
         }

         if (var5 != null) {
            this.cellDrawn(var1, var2, var5, var6);
         }
      }

   }

   protected void cellDrawn(mxICanvas var1, mxCellState var2, Object var3, Object var4) {
      if (var3 instanceof Element) {
         String var5 = this.getLinkForCell(var2.getCell());
         if (var5 != null) {
            String var6 = this.getToolTipForCell(var2.getCell());
            Element var7 = (Element)var3;
            if (var7.getNodeName().startsWith("v:")) {
               var7.setAttribute("href", var5.toString());
               if (var6 != null) {
                  var7.setAttribute("title", var6);
               }
            } else {
               Element var8;
               if (var7.getOwnerDocument().getElementsByTagName("svg").getLength() > 0) {
                  var8 = var7.getOwnerDocument().createElement("a");
                  var8.setAttribute("xlink:href", var5.toString());
                  var7.getParentNode().replaceChild(var8, var7);
                  var8.appendChild(var7);
                  if (var6 != null) {
                     var8.setAttribute("xlink:title", var6);
                  }

                  var7 = var8;
               } else {
                  var8 = var7.getOwnerDocument().createElement("a");
                  var8.setAttribute("href", var5.toString());
                  var8.setAttribute("style", "text-decoration:none;");
                  var7.getParentNode().replaceChild(var8, var7);
                  var8.appendChild(var7);
                  if (var6 != null) {
                     var8.setAttribute("title", var6);
                  }

                  var7 = var8;
               }
            }

            String var9 = this.getTargetForCell(var2.getCell());
            if (var9 != null) {
               var7.setAttribute("target", var9);
            }
         }
      }

   }

   protected String getLinkForCell(Object var1) {
      return null;
   }

   protected String getTargetForCell(Object var1) {
      return null;
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.changeSupport.addPropertyChangeListener(var1);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.changeSupport.addPropertyChangeListener(var1, var2);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.changeSupport.removePropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.changeSupport.removePropertyChangeListener(var1, var2);
   }

   public static void main(String[] var0) {
      System.out.println("mxGraph version \"1.4.0.9\"");
   }

   static {
      try {
         mxResources.add("com.mxgraph.resources.graph");
      } catch (Exception var1) {
      }

   }

   public interface mxICellVisitor {
      boolean visit(Object var1, Object var2);
   }
}

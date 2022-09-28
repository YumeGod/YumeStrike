package com.mxgraph.swing.view;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class mxCellStatePreview {
   protected Map deltas = new LinkedHashMap();
   protected int count = 0;
   protected mxGraphComponent graphComponent;
   protected boolean cloned;
   protected float opacity = 1.0F;
   protected List cellStates;

   public mxCellStatePreview(mxGraphComponent var1, boolean var2) {
      this.graphComponent = var1;
      this.cloned = var2;
   }

   public boolean isCloned() {
      return this.cloned;
   }

   public void setCloned(boolean var1) {
      this.cloned = var1;
   }

   public boolean isEmpty() {
      return this.count == 0;
   }

   public int getCount() {
      return this.count;
   }

   public Map getDeltas() {
      return this.deltas;
   }

   public void setOpacity(float var1) {
      this.opacity = var1;
   }

   public float getOpacity() {
      return this.opacity;
   }

   public mxPoint moveState(mxCellState var1, double var2, double var4) {
      return this.moveState(var1, var2, var4, true, true);
   }

   public mxPoint moveState(mxCellState var1, double var2, double var4, boolean var6, boolean var7) {
      mxPoint var8 = (mxPoint)this.deltas.get(var1);
      if (var8 == null) {
         var8 = new mxPoint(var2, var4);
         this.deltas.put(var1, var8);
         ++this.count;
      } else if (var6) {
         var8.setX(var8.getX() + var2);
         var8.setY(var8.getY() + var4);
      } else {
         var8.setX(var2);
         var8.setY(var4);
      }

      if (var7) {
         this.addEdges(var1);
      }

      return var8;
   }

   public mxRectangle show() {
      mxGraph var1 = this.graphComponent.getGraph();
      mxIGraphModel var2 = var1.getModel();
      LinkedList var3 = null;
      Iterator var4;
      mxCellState var5;
      if (this.isCloned()) {
         var3 = new LinkedList();
         var4 = this.deltas.keySet().iterator();

         while(var4.hasNext()) {
            var5 = (mxCellState)var4.next();
            var3.addAll(this.snapshot(var5));
         }
      }

      var4 = this.deltas.keySet().iterator();

      while(var4.hasNext()) {
         var5 = (mxCellState)var4.next();
         mxPoint var6 = (mxPoint)this.deltas.get(var5);
         mxCellState var7 = var1.getView().getState(var2.getParent(var5.getCell()));
         this.translateState(var7, var5, var6.getX(), var6.getY());
      }

      mxRectangle var10 = null;
      var4 = this.deltas.keySet().iterator();

      mxCellState var11;
      while(var4.hasNext()) {
         var11 = (mxCellState)var4.next();
         mxPoint var12 = (mxPoint)this.deltas.get(var11);
         mxCellState var8 = var1.getView().getState(var2.getParent(var11.getCell()));
         mxRectangle var9 = this.revalidateState(var8, var11, var12.getX(), var12.getY());
         if (var10 != null) {
            var10.add(var9);
         } else {
            var10 = var9;
         }
      }

      if (var3 != null) {
         this.cellStates = new LinkedList();
         var4 = this.deltas.keySet().iterator();

         while(var4.hasNext()) {
            var11 = (mxCellState)var4.next();
            this.cellStates.addAll(this.snapshot(var11));
         }

         this.restore(var3);
      }

      if (var10 != null) {
         var10.grow(2.0);
      }

      return var10;
   }

   public void restore(List var1) {
      mxGraph var2 = this.graphComponent.getGraph();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         mxCellState var4 = (mxCellState)var3.next();
         mxCellState var5 = var2.getView().getState(var4.getCell());
         if (var5 != null && var5 != var4) {
            this.restoreState(var5, var4);
         }
      }

   }

   public void restoreState(mxCellState var1, mxCellState var2) {
      var1.setLabelBounds(var2.getLabelBounds());
      var1.setAbsolutePoints(var2.getAbsolutePoints());
      var1.setOrigin(var2.getOrigin());
      var1.setAbsoluteOffset(var2.getAbsoluteOffset());
      var1.setBoundingBox(var2.getBoundingBox());
      var1.setTerminalDistance(var2.getTerminalDistance());
      var1.setSegments(var2.getSegments());
      var1.setLength(var2.getLength());
      var1.setX(var2.getX());
      var1.setY(var2.getY());
      var1.setWidth(var2.getWidth());
      var1.setHeight(var2.getHeight());
   }

   public List snapshot(mxCellState var1) {
      LinkedList var2 = new LinkedList();
      if (var1 != null) {
         var2.add((mxCellState)var1.clone());
         mxGraph var3 = this.graphComponent.getGraph();
         mxIGraphModel var4 = var3.getModel();
         Object var5 = var1.getCell();
         int var6 = var4.getChildCount(var5);

         for(int var7 = 0; var7 < var6; ++var7) {
            var2.addAll(this.snapshot(var3.getView().getState(var4.getChildAt(var5, var7))));
         }
      }

      return var2;
   }

   protected void translateState(mxCellState var1, mxCellState var2, double var3, double var5) {
      if (var2 != null) {
         mxGraph var7 = this.graphComponent.getGraph();
         mxIGraphModel var8 = var7.getModel();
         Object var9 = var2.getCell();
         if (var8.isVertex(var9)) {
            var2.setInvalid(true);
            var7.getView().validateBounds(var1, var9);
            mxGeometry var10 = var7.getCellGeometry(var9);
            if (var10 != null && (var3 != 0.0 || var5 != 0.0) && (!var10.isRelative() || this.deltas.get(var2) != null)) {
               var2.setX(var2.getX() + var3);
               var2.setY(var2.getY() + var5);
            }
         }

         int var12 = var8.getChildCount(var9);

         for(int var11 = 0; var11 < var12; ++var11) {
            this.translateState(var2, var7.getView().getState(var8.getChildAt(var9, var11)), var3, var5);
         }
      }

   }

   protected mxRectangle revalidateState(mxCellState var1, mxCellState var2, double var3, double var5) {
      mxRectangle var7 = null;
      if (var2 != null) {
         mxGraph var8 = this.graphComponent.getGraph();
         mxIGraphModel var9 = var8.getModel();
         Object var10 = var2.getCell();
         var2.setInvalid(true);
         var7 = var8.getView().validatePoints(var1, var10);
         mxGeometry var11 = var8.getCellGeometry(var10);
         if (var11 != null && var3 != 0.0 && var5 != 0.0 && var9.isVertex(var10) && var11.isRelative() && this.deltas.get(var2) != null) {
            var2.setX(var2.getX() + var3);
            var2.setY(var2.getY() + var5);
            var8.getView().updateLabelBounds(var2);
         }

         int var12 = var9.getChildCount(var10);

         for(int var13 = 0; var13 < var12; ++var13) {
            mxRectangle var14 = this.revalidateState(var2, var8.getView().getState(var9.getChildAt(var10, var13)), var3, var5);
            if (var7 != null) {
               var7.add(var14);
            } else {
               var7 = var14;
            }
         }
      }

      return var7;
   }

   public void addEdges(mxCellState var1) {
      mxGraph var2 = this.graphComponent.getGraph();
      mxIGraphModel var3 = var2.getModel();
      Object var4 = var1.getCell();
      int var5 = var3.getEdgeCount(var4);

      for(int var6 = 0; var6 < var5; ++var6) {
         mxCellState var7 = var2.getView().getState(var3.getEdgeAt(var4, var6));
         if (var7 != null) {
            this.moveState(var7, 0.0, 0.0);
         }
      }

   }

   public void paint(Graphics var1) {
      if (this.cellStates != null && this.cellStates.size() > 0) {
         mxInteractiveCanvas var2 = this.graphComponent.getCanvas();
         if (this.graphComponent.isAntiAlias()) {
            mxUtils.setAntiAlias((Graphics2D)var1, true, true);
         }

         Graphics2D var3 = var2.getGraphics();
         Point var4 = var2.getTranslate();
         double var5 = var2.getScale();

         try {
            var2.setScale(this.graphComponent.getGraph().getView().getScale());
            var2.setTranslate(0, 0);
            var2.setGraphics((Graphics2D)var1);
            this.paintPreview(var2);
         } finally {
            var2.setScale(var5);
            var2.setTranslate(var4.x, var4.y);
            var2.setGraphics(var3);
         }
      }

   }

   protected float getOpacityForCell(Object var1) {
      return this.opacity;
   }

   protected void paintPreview(mxGraphics2DCanvas var1) {
      Composite var2 = var1.getGraphics().getComposite();
      Iterator var3 = this.cellStates.iterator();

      while(var3.hasNext()) {
         mxCellState var4 = (mxCellState)var3.next();
         var1.getGraphics().setComposite(AlphaComposite.getInstance(3, this.getOpacityForCell(var4.getCell())));
         this.paintPreviewState(var1, var4);
      }

      var1.getGraphics().setComposite(var2);
   }

   protected void paintPreviewState(mxGraphics2DCanvas var1, mxCellState var2) {
      String var3 = this.graphComponent.getGraphControl().getDisplayLabelForCell(var2.getCell());
      this.graphComponent.getGraph().drawState(var1, var2, var3);
   }
}

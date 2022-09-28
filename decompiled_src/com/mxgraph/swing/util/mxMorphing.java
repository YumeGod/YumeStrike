package com.mxgraph.swing.util;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxCellStatePreview;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

public class mxMorphing extends mxAnimation {
   protected mxGraphComponent graphComponent;
   protected int steps;
   protected int step;
   protected double ease;
   protected Map origins;
   protected Object[] cells;
   protected transient mxRectangle dirty;
   protected transient mxCellStatePreview preview;

   public mxMorphing(mxGraphComponent var1) {
      this(var1, 6, 1.5, DEFAULT_DELAY);
      var1.addListener("afterPaint", new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            Graphics var3 = (Graphics)var2.getProperty("g");
            mxMorphing.this.paint(var3);
         }
      });
   }

   public mxMorphing(mxGraphComponent var1, int var2, double var3, int var5) {
      super(var5);
      this.origins = new HashMap();
      this.graphComponent = var1;
      this.steps = var2;
      this.ease = var3;
   }

   public int getSteps() {
      return this.steps;
   }

   public void setSteps(int var1) {
      this.steps = var1;
   }

   public double getEase() {
      return this.ease;
   }

   public void setEase(double var1) {
      this.ease = var1;
   }

   public void setCells(Object[] var1) {
      this.cells = var1;
   }

   public void updateAnimation() {
      this.preview = new mxCellStatePreview(this.graphComponent, false);
      if (this.cells != null) {
         Object[] var1 = this.cells;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Object var4 = var1[var3];
            this.animateCell(var4, this.preview, false);
         }
      } else {
         Object var5 = this.graphComponent.getGraph().getModel().getRoot();
         this.animateCell(var5, this.preview, true);
      }

      this.show(this.preview);
      if (this.preview.isEmpty() || this.step++ >= this.steps) {
         this.stopAnimation();
      }

   }

   public void stopAnimation() {
      this.graphComponent.getGraph().getView().revalidate();
      super.stopAnimation();
      this.preview = null;
      if (this.dirty != null) {
         this.graphComponent.getGraphControl().repaint(this.dirty.getRectangle());
      }

   }

   protected void show(mxCellStatePreview var1) {
      if (this.dirty != null) {
         this.graphComponent.getGraphControl().repaint(this.dirty.getRectangle());
      } else {
         this.graphComponent.getGraphControl().repaint();
      }

      this.dirty = var1.show();
      if (this.dirty != null) {
         this.graphComponent.getGraphControl().repaint(this.dirty.getRectangle());
      }

   }

   protected void animateCell(Object var1, mxCellStatePreview var2, boolean var3) {
      mxGraph var4 = this.graphComponent.getGraph();
      mxCellState var5 = var4.getView().getState(var1);
      mxPoint var6 = null;
      if (var5 != null) {
         var6 = this.getDelta(var5);
         if (var4.getModel().isVertex(var1) && (var6.getX() != 0.0 || var6.getY() != 0.0)) {
            mxPoint var7 = var4.getView().getTranslate();
            double var8 = var4.getView().getScale();
            var6.setX(var6.getX() + var7.getX() * var8);
            var6.setY(var6.getY() + var7.getY() * var8);
            var2.moveState(var5, -var6.getX() / this.ease, -var6.getY() / this.ease);
         }
      }

      if (var3 && !this.stopRecursion(var5, var6)) {
         int var10 = var4.getModel().getChildCount(var1);

         for(int var11 = 0; var11 < var10; ++var11) {
            this.animateCell(var4.getModel().getChildAt(var1, var11), var2, var3);
         }
      }

   }

   protected boolean stopRecursion(mxCellState var1, mxPoint var2) {
      return var2 != null && (var2.getX() != 0.0 || var2.getY() != 0.0);
   }

   protected mxPoint getDelta(mxCellState var1) {
      mxGraph var2 = this.graphComponent.getGraph();
      mxPoint var3 = this.getOriginForCell(var1.getCell());
      mxPoint var4 = var2.getView().getTranslate();
      double var5 = var2.getView().getScale();
      mxPoint var7 = new mxPoint(var1.getX() / var5 - var4.getX(), var1.getY() / var5 - var4.getY());
      return new mxPoint((var3.getX() - var7.getX()) * var5, (var3.getY() - var7.getY()) * var5);
   }

   protected mxPoint getOriginForCell(Object var1) {
      mxPoint var2 = (mxPoint)this.origins.get(var1);
      if (var2 == null) {
         mxGraph var3 = this.graphComponent.getGraph();
         if (var1 != null) {
            var2 = new mxPoint(this.getOriginForCell(var3.getModel().getParent(var1)));
            mxGeometry var4 = var3.getCellGeometry(var1);
            if (var4 != null) {
               var2.setX(var2.getX() + var4.getX());
               var2.setY(var2.getY() + var4.getY());
            }
         }

         if (var2 == null) {
            mxPoint var5 = var3.getView().getTranslate();
            var2 = new mxPoint(-var5.getX(), -var5.getY());
         }

         this.origins.put(var1, var2);
      }

      return var2;
   }

   public void paint(Graphics var1) {
      if (this.preview != null) {
         this.preview.paint(var1);
      }

   }
}

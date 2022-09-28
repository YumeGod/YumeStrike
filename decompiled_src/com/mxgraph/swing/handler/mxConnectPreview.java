package com.mxgraph.swing.handler;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class mxConnectPreview extends mxEventSource {
   protected mxGraphComponent graphComponent;
   protected mxCellState previewState;
   protected mxCellState sourceState;
   protected mxPoint startPoint;

   public mxConnectPreview(mxGraphComponent var1) {
      this.graphComponent = var1;
      var1.addListener("afterPaint", new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            Graphics var3 = (Graphics)var2.getProperty("g");
            mxConnectPreview.this.paint(var3);
         }
      });
   }

   protected Object createCell(mxCellState var1, String var2) {
      mxGraph var3 = this.graphComponent.getGraph();
      mxICell var4 = (mxICell)var3.createEdge((Object)null, (String)null, "", (Object)null, (Object)null, var2);
      ((mxICell)var1.getCell()).insertEdge(var4, true);
      return var4;
   }

   public boolean isActive() {
      return this.sourceState != null;
   }

   public mxCellState getSourceState() {
      return this.sourceState;
   }

   public mxCellState getPreviewState() {
      return this.previewState;
   }

   public mxPoint getStartPoint() {
      return this.startPoint;
   }

   public void start(MouseEvent var1, mxCellState var2, String var3) {
      mxGraph var4 = this.graphComponent.getGraph();
      this.sourceState = var2;
      this.startPoint = this.transformScreenPoint(var2.getCenterX(), var2.getCenterY());
      this.previewState = var4.getView().getState(this.createCell(var2, var3), true);
      this.fireEvent(new mxEventObject(mxEvent.START, new Object[]{"event", var1, "state", this.previewState}));
   }

   public void update(MouseEvent var1, mxCellState var2, double var3, double var5) {
      mxGraph var7 = this.graphComponent.getGraph();
      mxICell var8 = (mxICell)this.previewState.getCell();
      mxRectangle var9 = this.graphComponent.getGraph().getPaintBounds(new Object[]{this.previewState.getCell()});
      if (var8.getTerminal(false) != null) {
         var8.getTerminal(false).removeEdge(var8, false);
      }

      if (var2 != null) {
         ((mxICell)var2.getCell()).insertEdge(var8, false);
      }

      mxGeometry var10 = var7.getCellGeometry(this.previewState.getCell());
      var10.setTerminalPoint(this.startPoint, true);
      var10.setTerminalPoint(this.transformScreenPoint(var3, var5), false);
      this.revalidate(var7.getView().getState(var7.getDefaultParent()), this.previewState.getCell());
      this.fireEvent(new mxEventObject(mxEvent.CONTINUE, new Object[]{"event", var1, "x", var3, "y", var5}));
      this.graphComponent.getGraphControl().repaint(this.getDirtyRect(var9));
   }

   protected Rectangle getDirtyRect() {
      return this.getDirtyRect((mxRectangle)null);
   }

   protected Rectangle getDirtyRect(mxRectangle var1) {
      if (this.previewState != null) {
         mxRectangle var2 = this.graphComponent.getGraph().getPaintBounds(new Object[]{this.previewState.getCell()});
         if (var1 != null) {
            var1.add(var2);
         } else {
            var1 = var2;
         }

         if (var1 != null) {
            var1.grow(2.0);
            return var1.getRectangle();
         }
      }

      return null;
   }

   protected mxPoint transformScreenPoint(double var1, double var3) {
      mxGraph var5 = this.graphComponent.getGraph();
      mxPoint var6 = var5.getView().getTranslate();
      double var7 = var5.getView().getScale();
      return new mxPoint(var1 / var7 - var6.getX(), var3 / var7 - var6.getY());
   }

   public void revalidate(mxCellState var1, Object var2) {
      mxGraph var3 = this.graphComponent.getGraph();
      mxCellState var4 = var3.getView().getState(var2);
      var4.setInvalid(true);
      var3.getView().validateBounds(var1, var2);
      var3.getView().validatePoints(var1, var2);
      mxIGraphModel var5 = var3.getModel();
      int var6 = var5.getChildCount(var2);

      for(int var7 = 0; var7 < var6; ++var7) {
         this.revalidate(var4, var5.getChildAt(var2, var7));
      }

   }

   public void paint(Graphics var1) {
      if (this.previewState != null) {
         mxInteractiveCanvas var2 = this.graphComponent.getCanvas();
         if (this.graphComponent.isAntiAlias()) {
            mxUtils.setAntiAlias((Graphics2D)var1, true, false);
         }

         float var3 = this.graphComponent.getPreviewAlpha();
         if (var3 < 1.0F) {
            ((Graphics2D)var1).setComposite(AlphaComposite.getInstance(3, var3));
         }

         Graphics2D var4 = var2.getGraphics();
         Point var5 = var2.getTranslate();
         double var6 = var2.getScale();

         try {
            var2.setScale(this.graphComponent.getGraph().getView().getScale());
            var2.setTranslate(0, 0);
            var2.setGraphics((Graphics2D)var1);
            this.paintPreview(var2);
         } finally {
            var2.setScale(var6);
            var2.setTranslate(var5.x, var5.y);
            var2.setGraphics(var4);
         }
      }

   }

   protected void paintPreview(mxGraphics2DCanvas var1) {
      this.graphComponent.getGraphControl().drawCell(this.graphComponent.getCanvas(), this.previewState.getCell());
   }

   public Object stop(boolean var1) {
      return this.stop(var1, (MouseEvent)null);
   }

   public Object stop(boolean var1, MouseEvent var2) {
      Object var3 = this.sourceState != null ? this.sourceState.getCell() : null;
      if (this.previewState != null) {
         mxGraph var4 = this.graphComponent.getGraph();
         var4.getModel().beginUpdate();

         try {
            mxICell var5 = (mxICell)this.previewState.getCell();
            mxICell var6 = var5.getTerminal(true);
            mxICell var7 = var5.getTerminal(false);
            if (var6 != null) {
               ((mxICell)var6).removeEdge(var5, true);
            }

            if (var7 != null) {
               ((mxICell)var7).removeEdge(var5, false);
            }

            if (var1) {
               var3 = var4.addCell(var5, (Object)null, (Integer)null, var6, var7);
            }

            this.fireEvent(new mxEventObject(mxEvent.STOP, new Object[]{"event", var2, "commit", var1}));
            if (this.previewState != null) {
               Rectangle var8 = this.getDirtyRect();
               var4.getView().clear(var5, false, true);
               this.previewState = null;
               if (!var1 && var8 != null) {
                  this.graphComponent.getGraphControl().repaint(var8);
               }
            }
         } finally {
            var4.getModel().endUpdate();
         }
      }

      this.sourceState = null;
      this.startPoint = null;
      return var3;
   }
}

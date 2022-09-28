package com.mxgraph.swing.handler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxCellStatePreview;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class mxMovePreview extends mxEventSource {
   protected mxGraphComponent graphComponent;
   protected int threshold = 200;
   protected boolean placeholderPreview = false;
   protected boolean clonePreview = true;
   protected boolean contextPreview = true;
   protected boolean hideSelectionHandler = false;
   protected transient mxCellState startState;
   protected transient mxCellState[] previewStates;
   protected transient Object[] movingCells;
   protected transient Rectangle initialPlaceholder;
   protected transient Rectangle placeholder;
   protected transient mxRectangle lastDirty;
   protected transient mxCellStatePreview preview;

   public mxMovePreview(mxGraphComponent var1) {
      this.graphComponent = var1;
      var1.addListener("afterPaint", new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            Graphics var3 = (Graphics)var2.getProperty("g");
            mxMovePreview.this.paint(var3);
         }
      });
   }

   public int getThreshold() {
      return this.threshold;
   }

   public void setThreshold(int var1) {
      this.threshold = var1;
   }

   public boolean isPlaceholderPreview() {
      return this.placeholderPreview;
   }

   public void setPlaceholderPreview(boolean var1) {
      this.placeholderPreview = var1;
   }

   public boolean isClonePreview() {
      return this.clonePreview;
   }

   public void setClonePreview(boolean var1) {
      this.clonePreview = var1;
   }

   public boolean isContextPreview() {
      return this.contextPreview;
   }

   public void setContextPreview(boolean var1) {
      this.contextPreview = var1;
   }

   public boolean isHideSelectionHandler() {
      return this.hideSelectionHandler;
   }

   public void setHideSelectionHandler(boolean var1) {
      this.hideSelectionHandler = var1;
   }

   public boolean isActive() {
      return this.startState != null;
   }

   public Object[] getMovingCells() {
      return this.movingCells;
   }

   public Object[] getCells(mxCellState var1) {
      mxGraph var2 = this.graphComponent.getGraph();
      return var2.getMovableCells(var2.getSelectionCells());
   }

   protected mxCellState[] getPreviewStates() {
      mxGraph var1 = this.graphComponent.getGraph();
      LinkedList var2 = new LinkedList();
      Object[] var3 = this.movingCells;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         mxCellState var7 = var1.getView().getState(var6);
         if (var7 != null) {
            var2.add(var7);
            if (var2.size() >= this.threshold) {
               return null;
            }

            if (this.isContextPreview()) {
               Object[] var8 = var1.getAllEdges(new Object[]{var6});
               Object[] var9 = var8;
               int var10 = var8.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  Object var12 = var9[var11];
                  if (!var1.isCellSelected(var12)) {
                     mxCellState var13 = var1.getView().getState(var12);
                     if (var13 != null) {
                        if (var2.size() >= this.threshold) {
                           return null;
                        }

                        var2.add(var13);
                     }
                  }
               }
            }
         }
      }

      return (mxCellState[])((mxCellState[])var2.toArray(new mxCellState[var2.size()]));
   }

   protected boolean isCellOpaque(Object var1) {
      return this.startState != null && this.startState.getCell() == var1;
   }

   public void start(MouseEvent var1, mxCellState var2) {
      this.startState = var2;
      this.movingCells = this.getCells(var2);
      this.previewStates = !this.placeholderPreview ? this.getPreviewStates() : null;
      if (this.previewStates == null || this.previewStates.length >= this.threshold) {
         this.placeholder = this.getPlaceholderBounds(this.startState).getRectangle();
         this.initialPlaceholder = new Rectangle(this.placeholder);
         this.graphComponent.getGraphControl().repaint(this.placeholder);
      }

      this.fireEvent(new mxEventObject(mxEvent.START, new Object[]{"event", var1, "state", this.startState}));
   }

   protected mxRectangle getPlaceholderBounds(mxCellState var1) {
      mxGraph var2 = this.graphComponent.getGraph();
      return var2.getView().getBounds(var2.getSelectionCells());
   }

   public mxCellStatePreview createCellStatePreview() {
      return new mxCellStatePreview(this.graphComponent, this.isClonePreview()) {
         protected float getOpacityForCell(Object var1) {
            return mxMovePreview.this.isCellOpaque(var1) ? 1.0F : super.getOpacityForCell(var1);
         }
      };
   }

   public void update(MouseEvent var1, double var2, double var4, boolean var6) {
      mxGraph var7 = this.graphComponent.getGraph();
      if (this.placeholder != null) {
         Rectangle var8 = new Rectangle(this.placeholder);
         this.placeholder.x = this.initialPlaceholder.x + (int)var2;
         this.placeholder.y = this.initialPlaceholder.x + (int)var4;
         var8.add(this.placeholder);
         this.graphComponent.getGraphControl().repaint(var8);
      } else if (this.previewStates != null) {
         this.preview = this.createCellStatePreview();
         this.preview.setOpacity(this.graphComponent.getPreviewAlpha());
         mxCellState[] var14 = this.previewStates;
         int var9 = var14.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            mxCellState var11 = var14[var10];
            this.preview.moveState(var11, var2, var4, false, false);
            boolean var12 = true;
            if ((var2 != 0.0 || var4 != 0.0) && var6 && this.isContextPreview()) {
               var12 = false;

               for(Object var13 = var11.getCell(); !var12 && var13 != null; var13 = var7.getModel().getParent(var13)) {
                  var12 = var7.isCellSelected(var13);
               }
            }
         }

         mxRectangle var15 = this.lastDirty;
         this.lastDirty = this.preview.show();
         if (var15 != null) {
            var15.add(this.lastDirty);
         } else {
            var15 = this.lastDirty;
         }

         if (var15 != null) {
            this.repaint(var15);
         }
      }

      if (this.isHideSelectionHandler()) {
         this.graphComponent.getSelectionCellsHandler().setVisible(false);
      }

      this.fireEvent(new mxEventObject(mxEvent.CONTINUE, new Object[]{"event", var1, "dx", var2, "dy", var4}));
   }

   protected void repaint(mxRectangle var1) {
      this.graphComponent.getGraphControl().repaint(var1.getRectangle());
   }

   protected void reset() {
      mxGraph var1 = this.graphComponent.getGraph();
      if (this.placeholder != null) {
         Rectangle var2 = this.placeholder;
         this.placeholder = null;
         this.graphComponent.getGraphControl().repaint(var2);
      }

      if (this.isHideSelectionHandler()) {
         this.graphComponent.getSelectionCellsHandler().setVisible(true);
      }

      if (!this.isClonePreview()) {
         var1.getView().revalidate();
      }

      this.previewStates = null;
      this.movingCells = null;
      this.startState = null;
      this.preview = null;
      if (this.lastDirty != null) {
         this.graphComponent.getGraphControl().repaint(this.lastDirty.getRectangle());
         this.lastDirty = null;
      }

   }

   public Object[] stop(boolean var1, MouseEvent var2, double var3, double var5, boolean var7, Object var8) {
      Object[] var9 = this.movingCells;
      this.reset();
      mxGraph var10 = this.graphComponent.getGraph();
      var10.getModel().beginUpdate();

      try {
         if (var1) {
            double var11 = var10.getView().getScale();
            var9 = var10.moveCells(var9, var3 / var11, var5 / var11, var7, var8, var2.getPoint());
         }

         this.fireEvent(new mxEventObject(mxEvent.STOP, new Object[]{"event", var2, "commit", var1}));
      } finally {
         var10.getModel().endUpdate();
      }

      return var9;
   }

   public void paint(Graphics var1) {
      if (this.placeholder != null) {
         mxConstants.PREVIEW_BORDER.paintBorder(this.graphComponent, var1, this.placeholder.x, this.placeholder.y, this.placeholder.width, this.placeholder.height);
      }

      if (this.preview != null) {
         this.preview.paint(var1);
      }

   }
}

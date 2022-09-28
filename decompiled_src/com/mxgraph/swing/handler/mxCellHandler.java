package com.mxgraph.swing.handler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

public class mxCellHandler {
   protected mxGraphComponent graphComponent;
   protected mxCellState state;
   protected Rectangle[] handles;
   protected boolean handlesVisible = true;
   protected transient Rectangle bounds;
   protected transient JComponent preview;
   protected transient Point first;
   protected transient int index;

   public mxCellHandler(mxGraphComponent var1, mxCellState var2) {
      this.graphComponent = var1;
      this.refresh(var2);
   }

   public boolean isActive() {
      return this.first != null;
   }

   public void refresh(mxCellState var1) {
      this.state = var1;
      this.handles = this.createHandles();
      mxGraph var2 = this.graphComponent.getGraph();
      mxRectangle var3 = var2.getBoundingBox(var1.getCell());
      if (var3 != null) {
         this.bounds = var3.getRectangle();
         if (this.handles != null) {
            for(int var4 = 0; var4 < this.handles.length; ++var4) {
               if (this.isHandleVisible(var4)) {
                  this.bounds.add(this.handles[var4]);
               }
            }
         }
      }

   }

   public mxGraphComponent getGraphComponent() {
      return this.graphComponent;
   }

   public mxCellState getState() {
      return this.state;
   }

   public int getIndex() {
      return this.index;
   }

   public Rectangle getBounds() {
      return this.bounds;
   }

   public boolean isLabelMovable() {
      mxGraph var1 = this.graphComponent.getGraph();
      String var2 = var1.getLabel(this.state.getCell());
      return var1.isLabelMovable(this.state.getCell()) && var2 != null && var2.length() > 0;
   }

   public boolean isHandlesVisible() {
      return this.handlesVisible;
   }

   public void setHandlesVisible(boolean var1) {
      this.handlesVisible = var1;
   }

   public boolean isLabel(int var1) {
      return var1 == this.getHandleCount() - 1;
   }

   protected Rectangle[] createHandles() {
      return null;
   }

   protected int getHandleCount() {
      return this.handles != null ? this.handles.length : 0;
   }

   public String getToolTipText(MouseEvent var1) {
      return null;
   }

   public int getIndexAt(int var1, int var2) {
      if (this.handles != null && this.isHandlesVisible()) {
         int var3 = this.graphComponent.getTolerance();
         Rectangle var4 = new Rectangle(var1 - var3 / 2, var2 - var3 / 2, var3, var3);

         for(int var5 = this.handles.length - 1; var5 >= 0; --var5) {
            if (this.isHandleVisible(var5) && this.handles[var5].intersects(var4)) {
               return var5;
            }
         }
      }

      return -1;
   }

   public void mousePressed(MouseEvent var1) {
      if (!var1.isConsumed()) {
         int var2 = this.getIndexAt(var1.getX(), var1.getY());
         if (!this.isIgnoredEvent(var1) && var2 >= 0 && this.isHandleEnabled(var2)) {
            this.graphComponent.stopEditing(true);
            this.start(var1, var2);
            var1.consume();
         }
      }

   }

   public void mouseMoved(MouseEvent var1) {
      if (!var1.isConsumed() && this.handles != null) {
         int var2 = this.getIndexAt(var1.getX(), var1.getY());
         if (var2 >= 0 && this.isHandleEnabled(var2)) {
            Cursor var3 = this.getCursor(var1, var2);
            if (var3 != null) {
               this.graphComponent.getGraphControl().setCursor(var3);
               var1.consume();
            } else {
               this.graphComponent.getGraphControl().setCursor(new Cursor(12));
            }
         }
      }

   }

   public void mouseDragged(MouseEvent var1) {
   }

   public void mouseReleased(MouseEvent var1) {
      this.reset();
   }

   public void start(MouseEvent var1, int var2) {
      this.index = var2;
      this.first = var1.getPoint();
      this.preview = this.createPreview();
      if (this.preview != null) {
         this.graphComponent.getGraphControl().add(this.preview, 0);
      }

   }

   protected boolean isIgnoredEvent(MouseEvent var1) {
      return this.graphComponent.isEditEvent(var1);
   }

   protected JComponent createPreview() {
      return null;
   }

   public void reset() {
      if (this.preview != null) {
         this.preview.setVisible(false);
         this.preview.getParent().remove(this.preview);
         this.preview = null;
      }

      this.first = null;
   }

   protected Cursor getCursor(MouseEvent var1, int var2) {
      return null;
   }

   public void paint(Graphics var1) {
      if (this.handles != null && this.isHandlesVisible()) {
         for(int var2 = 0; var2 < this.handles.length; ++var2) {
            if (this.isHandleVisible(var2) && var1.hitClip(this.handles[var2].x, this.handles[var2].y, this.handles[var2].width, this.handles[var2].height)) {
               var1.setColor(this.getHandleFillColor(var2));
               var1.fillRect(this.handles[var2].x, this.handles[var2].y, this.handles[var2].width, this.handles[var2].height);
               var1.setColor(this.getHandleBorderColor(var2));
               var1.drawRect(this.handles[var2].x, this.handles[var2].y, this.handles[var2].width - 1, this.handles[var2].height - 1);
            }
         }
      }

   }

   protected boolean isHandleEnabled(int var1) {
      return true;
   }

   protected boolean isHandleVisible(int var1) {
      return !this.isLabel(var1) || this.isLabelMovable();
   }

   protected Color getHandleFillColor(int var1) {
      return this.isLabel(var1) ? mxConstants.LABEL_HANDLE_FILLCOLOR : mxConstants.HANDLE_FILLCOLOR;
   }

   protected Color getHandleBorderColor(int var1) {
      return mxConstants.HANDLE_BORDERCOLOR;
   }
}

package com.mxgraph.swing;

import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraphView;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JScrollBar;

public class mxGraphOutline extends JComponent {
   private static final long serialVersionUID = -2521103946905154267L;
   public static Color DEFAULT_ZOOMHANDLE_FILL = new Color(0, 255, 255);
   protected mxGraphComponent graphComponent;
   protected BufferedImage tripleBuffer;
   protected Graphics2D tripleBufferGraphics;
   protected boolean repaintBuffer = false;
   protected mxRectangle repaintClip = null;
   protected boolean tripleBuffered = true;
   protected Rectangle finderBounds = new Rectangle();
   protected Point zoomHandleLocation = null;
   protected boolean finderVisible = true;
   protected boolean zoomHandleVisible = true;
   protected boolean useScaledInstance = false;
   protected boolean antiAlias = false;
   protected boolean drawLabels = false;
   protected boolean fitPage = true;
   protected int outlineBorder = 10;
   protected MouseTracker tracker = new MouseTracker();
   protected double scale = 1.0;
   protected Point translate = new Point();
   protected transient boolean zoomGesture = false;
   protected mxEventSource.mxIEventListener repaintHandler = new mxEventSource.mxIEventListener() {
      public void invoke(Object var1, mxEventObject var2) {
         mxGraphOutline.this.updateScaleAndTranslate();
         mxRectangle var3 = (mxRectangle)var2.getProperty("region");
         if (var3 != null) {
            mxGraphOutline.this.repaintClip = new mxRectangle(var3);
         } else {
            mxGraphOutline.this.repaintBuffer = true;
         }

         if (var3 != null) {
            mxGraphOutline.this.updateFinder(true);
            var3.grow(1.0 / mxGraphOutline.this.scale);
            var3.setX(var3.getX() * mxGraphOutline.this.scale + (double)mxGraphOutline.this.translate.x);
            var3.setY(var3.getY() * mxGraphOutline.this.scale + (double)mxGraphOutline.this.translate.y);
            var3.setWidth(var3.getWidth() * mxGraphOutline.this.scale);
            var3.setHeight(var3.getHeight() * mxGraphOutline.this.scale);
            mxGraphOutline.this.repaint(var3.getRectangle());
         } else {
            mxGraphOutline.this.updateFinder(false);
            mxGraphOutline.this.repaint();
         }

      }
   };
   protected ComponentListener componentHandler = new ComponentAdapter() {
      public void componentResized(ComponentEvent var1) {
         if (mxGraphOutline.this.updateScaleAndTranslate()) {
            mxGraphOutline.this.repaintBuffer = true;
            mxGraphOutline.this.updateFinder(false);
            mxGraphOutline.this.repaint();
         } else {
            mxGraphOutline.this.updateFinder(true);
         }

      }
   };
   protected AdjustmentListener adjustmentHandler = new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent var1) {
         if (mxGraphOutline.this.updateScaleAndTranslate()) {
            mxGraphOutline.this.repaintBuffer = true;
            mxGraphOutline.this.updateFinder(false);
            mxGraphOutline.this.repaint();
         } else {
            mxGraphOutline.this.updateFinder(true);
         }

      }
   };

   public mxGraphOutline(mxGraphComponent var1) {
      this.addComponentListener(this.componentHandler);
      this.addMouseMotionListener(this.tracker);
      this.addMouseListener(this.tracker);
      this.setGraphComponent(var1);
      this.setEnabled(true);
      this.setOpaque(true);
   }

   public void setTripleBuffered(boolean var1) {
      boolean var2 = this.tripleBuffered;
      this.tripleBuffered = var1;
      if (!var1) {
         this.destroyTripleBuffer();
      }

      this.firePropertyChange("tripleBuffered", var2, var1);
   }

   public boolean isTripleBuffered() {
      return this.tripleBuffered;
   }

   public void setDrawLabels(boolean var1) {
      boolean var2 = this.drawLabels;
      this.drawLabels = var1;
      this.repaintTripleBuffer((Rectangle)null);
      this.firePropertyChange("drawLabels", var2, var1);
   }

   public boolean isDrawLabels() {
      return this.drawLabels;
   }

   public void setAntiAlias(boolean var1) {
      boolean var2 = this.antiAlias;
      this.antiAlias = var1;
      this.repaintTripleBuffer((Rectangle)null);
      this.firePropertyChange("antiAlias", var2, var1);
   }

   public boolean isAntiAlias() {
      return this.antiAlias;
   }

   public void setVisible(boolean var1) {
      super.setVisible(var1);
      if (!var1) {
         this.destroyTripleBuffer();
      }

   }

   public void setFinderVisible(boolean var1) {
      this.finderVisible = var1;
   }

   public void setZoomHandleVisible(boolean var1) {
      this.zoomHandleVisible = var1;
   }

   public void setFitPage(boolean var1) {
      boolean var2 = this.fitPage;
      this.fitPage = var1;
      if (this.updateScaleAndTranslate()) {
         this.repaintBuffer = true;
         this.updateFinder(false);
      }

      this.firePropertyChange("fitPage", var2, var1);
   }

   public boolean isFitPage() {
      return this.fitPage;
   }

   public mxGraphComponent getGraphComponent() {
      return this.graphComponent;
   }

   public void setGraphComponent(mxGraphComponent var1) {
      mxGraphComponent var2 = this.graphComponent;
      if (this.graphComponent != null) {
         this.graphComponent.getGraph().removeListener(this.repaintHandler);
         this.graphComponent.getGraphControl().removeComponentListener(this.componentHandler);
         this.graphComponent.getHorizontalScrollBar().removeAdjustmentListener(this.adjustmentHandler);
         this.graphComponent.getVerticalScrollBar().removeAdjustmentListener(this.adjustmentHandler);
      }

      this.graphComponent = var1;
      if (this.graphComponent != null) {
         this.graphComponent.getGraph().addListener("repaint", this.repaintHandler);
         this.graphComponent.getGraphControl().addComponentListener(this.componentHandler);
         this.graphComponent.getHorizontalScrollBar().addAdjustmentListener(this.adjustmentHandler);
         this.graphComponent.getVerticalScrollBar().addAdjustmentListener(this.adjustmentHandler);
      }

      if (this.updateScaleAndTranslate()) {
         this.repaintBuffer = true;
         this.repaint();
      }

      this.firePropertyChange("graphComponent", var2, var1);
   }

   public void checkTripleBuffer() {
      if (this.tripleBuffer != null && (this.tripleBuffer.getWidth() != this.getWidth() || this.tripleBuffer.getHeight() != this.getHeight())) {
         this.destroyTripleBuffer();
      }

      if (this.tripleBuffer == null) {
         this.createTripleBuffer(this.getWidth(), this.getHeight());
      }

   }

   protected void createTripleBuffer(int var1, int var2) {
      try {
         this.tripleBuffer = mxUtils.createBufferedImage(var1, var2, (Color)null);
         this.tripleBufferGraphics = this.tripleBuffer.createGraphics();
         this.repaintTripleBuffer((Rectangle)null);
      } catch (OutOfMemoryError var4) {
      }

   }

   public void destroyTripleBuffer() {
      if (this.tripleBuffer != null) {
         this.tripleBuffer = null;
         this.tripleBufferGraphics.dispose();
         this.tripleBufferGraphics = null;
      }

   }

   public void repaintTripleBuffer(Rectangle var1) {
      if (this.tripleBuffered && this.tripleBufferGraphics != null) {
         if (var1 == null) {
            var1 = new Rectangle(this.tripleBuffer.getWidth(), this.tripleBuffer.getHeight());
         }

         mxUtils.clearRect(this.tripleBufferGraphics, var1, (Color)null);
         this.tripleBufferGraphics.setClip(var1);
         this.paintGraph(this.tripleBufferGraphics);
         this.tripleBufferGraphics.setClip((Shape)null);
         this.repaintBuffer = false;
         this.repaintClip = null;
      }

   }

   public void updateFinder(boolean var1) {
      Rectangle var2 = this.graphComponent.getViewport().getViewRect();
      int var3 = (int)Math.round((double)var2.x * this.scale);
      int var4 = (int)Math.round((double)var2.y * this.scale);
      int var5 = (int)Math.round((double)(var2.x + var2.width) * this.scale) - var3;
      int var6 = (int)Math.round((double)(var2.y + var2.height) * this.scale) - var4;
      this.updateFinderBounds(new Rectangle(var3 + this.translate.x, var4 + this.translate.y, var5 + 1, var6 + 1), var1);
   }

   public void updateFinderBounds(Rectangle var1, boolean var2) {
      if (var1 != null && !var1.equals(this.finderBounds)) {
         Rectangle var3 = new Rectangle(this.finderBounds);
         this.finderBounds = var1;
         if (var2) {
            var3 = var3.union(this.finderBounds);
            var3.grow(3, 3);
            this.repaint(var3);
         }
      }

   }

   public void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      this.paintBackground(var1);
      if (this.graphComponent != null) {
         if (this.tripleBuffered) {
            this.checkTripleBuffer();
         } else if (this.tripleBuffer != null) {
            this.destroyTripleBuffer();
         }

         if (this.tripleBuffer != null) {
            if (this.repaintBuffer) {
               this.repaintTripleBuffer((Rectangle)null);
            } else if (this.repaintClip != null) {
               this.repaintClip.grow(1.0 / this.scale);
               this.repaintClip.setX(this.repaintClip.getX() * this.scale + (double)this.translate.x);
               this.repaintClip.setY(this.repaintClip.getY() * this.scale + (double)this.translate.y);
               this.repaintClip.setWidth(this.repaintClip.getWidth() * this.scale);
               this.repaintClip.setHeight(this.repaintClip.getHeight() * this.scale);
               this.repaintTripleBuffer(this.repaintClip.getRectangle());
            }

            mxUtils.drawImageClip(var1, this.tripleBuffer, this);
         } else {
            this.paintGraph(var1);
         }

         this.paintForeground(var1);
      }

   }

   protected void paintBackground(Graphics var1) {
      if (this.graphComponent != null) {
         Graphics2D var2 = (Graphics2D)var1;
         AffineTransform var3 = var2.getTransform();

         try {
            var1.setColor(this.graphComponent.getPageBackgroundColor());
            mxUtils.fillClippedRect(var1, 0, 0, this.getWidth(), this.getHeight());
            var2.translate(this.translate.x, this.translate.y);
            var2.scale(this.scale, this.scale);
            if (!this.graphComponent.isPageVisible()) {
               var1.setColor(this.graphComponent.getBackground());
               Dimension var4 = this.graphComponent.getGraphControl().getSize();
               mxUtils.fillClippedRect(var1, 0, 0, var4.width, var4.height);
               var1.setColor(var1.getColor().darker().darker());
               var1.drawRect(0, 0, var4.width, var4.height);
            } else {
               this.graphComponent.paintBackgroundPage(var1);
            }
         } finally {
            var2.setTransform(var3);
         }
      } else {
         var1.setColor(this.getBackground());
         mxUtils.fillClippedRect(var1, 0, 0, this.getWidth(), this.getHeight());
      }

   }

   public void paintGraph(Graphics var1) {
      if (this.graphComponent != null) {
         Graphics2D var2 = (Graphics2D)var1;
         AffineTransform var3 = var2.getTransform();

         try {
            var2.translate(this.translate.x, this.translate.y);
            var2.scale(this.scale, this.scale);
            this.graphComponent.getGraphControl().drawGraph(var2, this.drawLabels);
         } finally {
            var2.setTransform(var3);
         }
      }

   }

   protected void paintForeground(Graphics var1) {
      if (this.graphComponent != null) {
         Graphics2D var2 = (Graphics2D)var1;
         Stroke var3 = var2.getStroke();
         var1.setColor(Color.BLUE);
         var2.setStroke(new BasicStroke(3.0F));
         var1.drawRect(this.finderBounds.x, this.finderBounds.y, this.finderBounds.width, this.finderBounds.height);
         var2.setStroke(var3);
         var1.setColor(DEFAULT_ZOOMHANDLE_FILL);
         var1.fillRect(this.finderBounds.x + this.finderBounds.width - 6, this.finderBounds.y + this.finderBounds.height - 6, 8, 8);
         var1.setColor(Color.BLACK);
         var1.drawRect(this.finderBounds.x + this.finderBounds.width - 6, this.finderBounds.y + this.finderBounds.height - 6, 8, 8);
      }

   }

   public boolean updateScaleAndTranslate() {
      double var1 = 1.0;
      int var3 = 0;
      int var4 = 0;
      if (this.graphComponent != null) {
         Dimension var5 = this.graphComponent.getGraphControl().getSize();
         Dimension var6 = this.getSize();
         int var7 = (int)var5.getWidth();
         int var8 = (int)var5.getHeight();
         if (var7 > 0 && var8 > 0) {
            boolean var9 = this.graphComponent.isPageVisible() && this.isFitPage() && this.graphComponent.getHorizontalScrollBar().isVisible() && this.graphComponent.getVerticalScrollBar().isVisible();
            double var10 = this.graphComponent.getGraph().getView().getScale();
            mxPoint var12 = this.graphComponent.getGraph().getView().getTranslate();
            int var13 = (int)var6.getWidth() - 2 * this.outlineBorder;
            int var14 = (int)var6.getHeight() - 2 * this.outlineBorder;
            if (var9) {
               var7 = (int)((long)var7 - 2L * Math.round(var12.getX() * var10));
               var8 = (int)((long)var8 - 2L * Math.round(var12.getY() * var10));
            }

            var1 = Math.min((double)var13 / (double)var7, (double)var14 / (double)var8);
            var3 += (int)Math.round((var6.getWidth() - (double)var7 * var1) / 2.0);
            var4 += (int)Math.round((var6.getHeight() - (double)var8 * var1) / 2.0);
            if (var9) {
               var3 = (int)((long)var3 - Math.round(var12.getX() * var1 * var10));
               var4 = (int)((long)var4 - Math.round(var12.getY() * var1 * var10));
            }
         }
      }

      if (var1 == this.scale && this.translate.x == var3 && this.translate.y == var4) {
         return false;
      } else {
         this.scale = var1;
         this.translate.setLocation(var3, var4);
         return true;
      }
   }

   public class MouseTracker implements MouseListener, MouseMotionListener {
      protected Point start = null;

      public void mousePressed(MouseEvent var1) {
         mxGraphOutline.this.zoomGesture = this.hitZoomHandle(var1.getX(), var1.getY());
         if (mxGraphOutline.this.graphComponent != null && !var1.isConsumed() && !var1.isPopupTrigger() && (mxGraphOutline.this.finderBounds.contains(var1.getPoint()) || mxGraphOutline.this.zoomGesture)) {
            this.start = var1.getPoint();
         }

      }

      public void mouseDragged(MouseEvent var1) {
         if (mxGraphOutline.this.isEnabled() && this.start != null) {
            if (mxGraphOutline.this.zoomGesture) {
               Rectangle var2 = mxGraphOutline.this.graphComponent.getViewport().getViewRect();
               double var3 = var2.getWidth() / var2.getHeight();
               var2 = new Rectangle(mxGraphOutline.this.finderBounds);
               var2.width = (int)Math.max(0.0, (double)var1.getX() - var2.getX());
               var2.height = (int)Math.max(0.0, var2.getWidth() / var3);
               mxGraphOutline.this.updateFinderBounds(var2, true);
            } else {
               int var5 = (int)(((double)var1.getX() - this.start.getX()) / mxGraphOutline.this.scale);
               int var6 = (int)(((double)var1.getY() - this.start.getY()) / mxGraphOutline.this.scale);
               this.start = var1.getPoint();
               mxGraphOutline.this.graphComponent.getHorizontalScrollBar().setValue(mxGraphOutline.this.graphComponent.getHorizontalScrollBar().getValue() + var5);
               mxGraphOutline.this.graphComponent.getVerticalScrollBar().setValue(mxGraphOutline.this.graphComponent.getVerticalScrollBar().getValue() + var6);
            }
         }

      }

      public void mouseReleased(MouseEvent var1) {
         if (this.start != null) {
            if (mxGraphOutline.this.zoomGesture) {
               double var2 = (double)var1.getX() - this.start.getX();
               double var4 = mxGraphOutline.this.finderBounds.getWidth();
               JScrollBar var6 = mxGraphOutline.this.graphComponent.getHorizontalScrollBar();
               double var7;
               if (var6 != null) {
                  var7 = (double)var6.getValue() / (double)var6.getMaximum();
               } else {
                  var7 = 0.0;
               }

               JScrollBar var9 = mxGraphOutline.this.graphComponent.getVerticalScrollBar();
               double var10;
               if (var9 != null) {
                  var10 = (double)var9.getValue() / (double)var9.getMaximum();
               } else {
                  var10 = 0.0;
               }

               mxGraphView var12 = mxGraphOutline.this.graphComponent.getGraph().getView();
               double var13 = var12.getScale();
               double var15 = var13 - var2 * var13 / var4;
               double var17 = var15 / var13;
               var12.setScale(var15);
               if (var6 != null) {
                  var6.setValue((int)(var7 * (double)var6.getMaximum() * var17));
               }

               if (var9 != null) {
                  var9.setValue((int)(var10 * (double)var9.getMaximum() * var17));
               }
            }

            mxGraphOutline.this.zoomGesture = false;
            this.start = null;
         }

      }

      public boolean hitZoomHandle(int var1, int var2) {
         return (new Rectangle(mxGraphOutline.this.finderBounds.x + mxGraphOutline.this.finderBounds.width - 6, mxGraphOutline.this.finderBounds.y + mxGraphOutline.this.finderBounds.height - 6, 8, 8)).contains(var1, var2);
      }

      public void mouseMoved(MouseEvent var1) {
         if (this.hitZoomHandle(var1.getX(), var1.getY())) {
            mxGraphOutline.this.setCursor(new Cursor(12));
         } else if (mxGraphOutline.this.finderBounds.contains(var1.getPoint())) {
            mxGraphOutline.this.setCursor(new Cursor(13));
         } else {
            mxGraphOutline.this.setCursor(new Cursor(0));
         }

      }

      public void mouseClicked(MouseEvent var1) {
      }

      public void mouseEntered(MouseEvent var1) {
      }

      public void mouseExited(MouseEvent var1) {
      }
   }
}

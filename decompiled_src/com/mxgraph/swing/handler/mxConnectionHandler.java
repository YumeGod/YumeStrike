package com.mxgraph.swing.handler;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMouseAdapter;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class mxConnectionHandler extends mxMouseAdapter {
   private static final long serialVersionUID = -2543899557644889853L;
   public static Cursor CONNECT_CURSOR = new Cursor(12);
   protected mxGraphComponent graphComponent;
   protected mxConnectPreview connectPreview;
   protected ImageIcon connectIcon = null;
   protected int handleSize;
   protected boolean handleEnabled;
   protected boolean select;
   protected boolean createTarget;
   protected boolean keepOnTop;
   protected transient Point first;
   protected transient Rectangle bounds;
   protected transient mxCellState source;
   protected transient mxCellMarker marker;
   protected transient String error;
   protected boolean enabled;
   protected transient mxEventSource.mxIEventListener resetHandler;

   public mxConnectionHandler(mxGraphComponent var1) {
      this.handleSize = mxConstants.CONNECT_HANDLE_SIZE;
      this.handleEnabled = mxConstants.CONNECT_HANDLE_ENABLED;
      this.select = true;
      this.createTarget = false;
      this.keepOnTop = true;
      this.enabled = true;
      this.resetHandler = new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            mxConnectionHandler.this.reset();
         }
      };
      this.graphComponent = var1;
      var1.addListener("afterPaint", new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            Graphics var3 = (Graphics)var2.getProperty("g");
            mxConnectionHandler.this.paint(var3);
         }
      });
      this.connectPreview = this.createConnectPreview();
      mxGraphComponent.mxGraphControl var2 = var1.getGraphControl();
      var2.addMouseListener(this);
      var2.addMouseMotionListener(this);
      mxGraphView var3 = var1.getGraph().getView();
      var3.addListener("scale", this.resetHandler);
      var3.addListener("translate", this.resetHandler);
      var3.addListener("scaleAndTranslate", this.resetHandler);
      mxIGraphModel var4 = var1.getGraph().getModel();
      var4.addListener("change", this.resetHandler);
      this.marker = new mxCellMarker(var1) {
         private static final long serialVersionUID = 103433247310526381L;

         protected Object getCell(MouseEvent var1) {
            Object var2 = super.getCell(var1);
            if (mxConnectionHandler.this.isConnecting()) {
               if (mxConnectionHandler.this.source != null) {
                  mxConnectionHandler.this.error = mxConnectionHandler.this.validateConnection(mxConnectionHandler.this.source.getCell(), var2);
                  if (mxConnectionHandler.this.error != null && mxConnectionHandler.this.error.length() == 0) {
                     var2 = null;
                     if (mxConnectionHandler.this.createTarget) {
                        mxConnectionHandler.this.error = null;
                     }
                  }
               }
            } else if (!mxConnectionHandler.this.isValidSource(var2)) {
               var2 = null;
            }

            return var2;
         }

         protected boolean isValidState(mxCellState var1) {
            if (mxConnectionHandler.this.isConnecting()) {
               return mxConnectionHandler.this.error == null;
            } else {
               return super.isValidState(var1);
            }
         }

         protected Color getMarkerColor(MouseEvent var1, mxCellState var2, boolean var3) {
            return !mxConnectionHandler.this.isHighlighting() && !mxConnectionHandler.this.isConnecting() ? null : super.getMarkerColor(var1, var2, var3);
         }

         protected boolean intersects(mxCellState var1, MouseEvent var2) {
            return mxConnectionHandler.this.isHighlighting() && !mxConnectionHandler.this.isConnecting() ? super.intersects(var1, var2) : true;
         }
      };
      this.marker.setHotspotEnabled(true);
   }

   protected mxConnectPreview createConnectPreview() {
      return new mxConnectPreview(this.graphComponent);
   }

   public mxConnectPreview getConnectPreview() {
      return this.connectPreview;
   }

   public boolean isConnecting() {
      return this.connectPreview.isActive();
   }

   public boolean isHighlighting() {
      return this.connectIcon == null && !this.handleEnabled;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public boolean isKeepOnTop() {
      return this.keepOnTop;
   }

   public void setKeepOnTop(boolean var1) {
      this.keepOnTop = var1;
   }

   public void setConnectIcon(ImageIcon var1) {
      this.connectIcon = var1;
   }

   public ImageIcon getConnecIcon() {
      return this.connectIcon;
   }

   public void setHandleEnabled(boolean var1) {
      this.handleEnabled = var1;
   }

   public boolean isHandleEnabled() {
      return this.handleEnabled;
   }

   public void setHandleSize(int var1) {
      this.handleSize = var1;
   }

   public int getHandleSize() {
      return this.handleSize;
   }

   public mxCellMarker getMarker() {
      return this.marker;
   }

   public void setMarker(mxCellMarker var1) {
      this.marker = var1;
   }

   public void setCreateTarget(boolean var1) {
      this.createTarget = var1;
   }

   public boolean isCreateTarget() {
      return this.createTarget;
   }

   public void setSelect(boolean var1) {
      this.select = var1;
   }

   public boolean isSelect() {
      return this.select;
   }

   public void reset() {
      this.connectPreview.stop(false);
      this.setBounds((Rectangle)null);
      this.marker.reset();
      this.source = null;
      this.first = null;
      this.error = null;
   }

   public Object createTargetVertex(MouseEvent var1, Object var2) {
      mxGraph var3 = this.graphComponent.getGraph();
      Object var4 = var3.cloneCells(new Object[]{var2})[0];
      mxIGraphModel var5 = var3.getModel();
      mxGeometry var6 = var5.getGeometry(var4);
      if (var6 != null) {
         mxPoint var7 = this.graphComponent.getPointForEvent(var1);
         var6.setX(var3.snap(var7.getX() - var6.getWidth() / 2.0));
         var6.setY(var3.snap(var7.getY() - var6.getHeight() / 2.0));
      }

      return var4;
   }

   public boolean isValidSource(Object var1) {
      return this.graphComponent.getGraph().isValidSource(var1);
   }

   public boolean isValidTarget(Object var1) {
      return true;
   }

   public String validateConnection(Object var1, Object var2) {
      if (var2 == null && this.createTarget) {
         return null;
      } else {
         return !this.isValidTarget(var2) ? "" : this.graphComponent.getGraph().getEdgeValidationError(this.connectPreview.getPreviewState().getCell(), var1, var2);
      }
   }

   public void mousePressed(MouseEvent var1) {
      if (!this.graphComponent.isForceMarqueeEvent(var1) && !this.graphComponent.isPanningEvent(var1) && !var1.isPopupTrigger() && this.graphComponent.isEnabled() && this.isEnabled() && !var1.isConsumed() && (this.isHighlighting() && this.marker.hasValidState() || !this.isHighlighting() && this.bounds != null && this.bounds.contains(var1.getPoint()))) {
         this.start(var1, this.marker.getValidState());
         var1.consume();
      }

   }

   public void start(MouseEvent var1, mxCellState var2) {
      this.first = var1.getPoint();
      this.connectPreview.start(var1, var2, "");
   }

   public void mouseMoved(MouseEvent var1) {
      this.mouseDragged(var1);
      if (this.isHighlighting() && !this.marker.hasValidState()) {
         this.source = null;
      }

      if (!this.isHighlighting() && this.source != null) {
         int var2 = this.handleSize;
         int var3 = this.handleSize;
         if (this.connectIcon != null) {
            var2 = this.connectIcon.getIconWidth();
            var3 = this.connectIcon.getIconHeight();
         }

         int var4 = (int)this.source.getCenterX() - var2 / 2;
         int var5 = (int)this.source.getCenterY() - var3 / 2;
         if (this.graphComponent.getGraph().isSwimlane(this.source.getCell())) {
            mxRectangle var6 = this.graphComponent.getGraph().getStartSize(this.source.getCell());
            if (var6.getWidth() > 0.0) {
               var4 = (int)(this.source.getX() + var6.getWidth() / 2.0 - (double)(var2 / 2));
            } else {
               var5 = (int)(this.source.getY() + var6.getHeight() / 2.0 - (double)(var3 / 2));
            }
         }

         this.setBounds(new Rectangle(var4, var5, var2, var3));
      } else {
         this.setBounds((Rectangle)null);
      }

      if (this.source != null && (this.bounds == null || this.bounds.contains(var1.getPoint()))) {
         this.graphComponent.getGraphControl().setCursor(CONNECT_CURSOR);
         var1.consume();
      }

   }

   public void mouseDragged(MouseEvent var1) {
      if (!var1.isConsumed() && this.graphComponent.isEnabled() && this.isEnabled() && (var1.getButton() == 0 || this.connectPreview.isActive())) {
         mxCellState var2 = this.marker.process(var1);
         if (this.connectPreview.isActive()) {
            mxGraph var3 = this.graphComponent.getGraph();
            double var4 = var3.snap((double)var1.getX());
            double var6 = var3.snap((double)var1.getY());
            this.connectPreview.update(var1, this.marker.getValidState(), var4, var6);
            this.setBounds((Rectangle)null);
            var1.consume();
         } else {
            this.source = var2;
         }
      }

   }

   public void mouseReleased(MouseEvent var1) {
      if (this.error != null) {
         if (this.error.length() > 0) {
            JOptionPane.showMessageDialog(this.graphComponent, this.error);
         }
      } else if (this.first != null) {
         mxGraph var2 = this.graphComponent.getGraph();
         double var3 = this.first.getX() - (double)var1.getX();
         double var5 = this.first.getY() - (double)var1.getY();
         if (this.connectPreview.isActive() && (this.marker.hasValidState() || this.isCreateTarget() || var2.isAllowDanglingEdges())) {
            var2.getModel().beginUpdate();

            try {
               Object var7;
               if (!this.marker.hasValidState() && this.isCreateTarget()) {
                  var7 = this.createTargetVertex(var1, this.source.getCell());
                  Object var8 = var2.getDropTarget(new Object[]{var7}, var1.getPoint(), this.graphComponent.getCellAt(var1.getX(), var1.getY()));
                  if (var7 != null) {
                     if (var8 != null && var2.getModel().isEdge(var8)) {
                        var8 = var2.getDefaultParent();
                     } else {
                        mxCellState var9 = var2.getView().getState(var8);
                        if (var9 != null) {
                           mxGeometry var10 = var2.getModel().getGeometry(var7);
                           mxPoint var11 = var9.getOrigin();
                           var10.setX(var10.getX() - var11.getX());
                           var10.setY(var10.getY() - var11.getY());
                        }
                     }

                     var2.addCells(new Object[]{var7}, var8);
                  }

                  double var17 = var2.snap((double)var1.getX());
                  double var18 = var2.snap((double)var1.getY());
                  mxCellState var13 = var2.getView().getState(var7, true);
                  this.connectPreview.update(var1, var13, var17, var18);
               }

               var7 = this.connectPreview.stop(this.graphComponent.isSignificant(var3, var5), var1);
               if (var7 != null) {
                  this.graphComponent.getGraph().setSelectionCell(var7);
               }

               var1.consume();
            } finally {
               var2.getModel().endUpdate();
            }
         }
      }

      this.reset();
   }

   public void setBounds(Rectangle var1) {
      if (this.bounds == null && var1 != null || this.bounds != null && var1 == null || this.bounds != null && var1 != null && !this.bounds.equals(var1)) {
         Rectangle var2 = this.bounds;
         if (var2 != null) {
            if (var1 != null) {
               var2.add(var1);
            }
         } else {
            var2 = var1;
         }

         this.bounds = var1;
         if (var2 != null) {
            this.graphComponent.getGraphControl().repaint(var2);
         }
      }

   }

   public void paint(Graphics var1) {
      if (this.bounds != null) {
         if (this.connectIcon != null) {
            var1.drawImage(this.connectIcon.getImage(), this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, (ImageObserver)null);
         } else if (this.handleEnabled) {
            var1.setColor(Color.BLACK);
            var1.draw3DRect(this.bounds.x, this.bounds.y, this.bounds.width - 1, this.bounds.height - 1, true);
            var1.setColor(Color.GREEN);
            var1.fill3DRect(this.bounds.x + 1, this.bounds.y + 1, this.bounds.width - 2, this.bounds.height - 2, true);
            var1.setColor(Color.BLUE);
            var1.drawRect(this.bounds.x + this.bounds.width / 2 - 1, this.bounds.y + this.bounds.height / 2 - 1, 1, 1);
         }
      }

   }
}

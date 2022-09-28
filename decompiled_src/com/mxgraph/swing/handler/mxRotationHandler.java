package com.mxgraph.swing.handler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMouseAdapter;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class mxRotationHandler extends mxMouseAdapter {
   public static ImageIcon ROTATE_ICON = null;
   private static double PI4;
   protected mxGraphComponent graphComponent;
   protected boolean enabled = true;
   protected JComponent handle;
   protected mxCellState currentState;
   protected double currentAngle;
   protected Point first;

   public mxRotationHandler(mxGraphComponent var1) {
      this.graphComponent = var1;
      var1.addMouseListener(this);
      this.handle = this.createHandle();
      var1.addListener("afterPaint", new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            Graphics var3 = (Graphics)var2.getProperty("g");
            mxRotationHandler.this.paint(var3);
         }
      });
      var1.getGraphControl().addMouseListener(this);
      var1.getGraphControl().addMouseMotionListener(this);
      this.handle.addMouseListener(this);
      this.handle.addMouseMotionListener(this);
   }

   public mxGraphComponent getGraphComponent() {
      return this.graphComponent;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   protected JComponent createHandle() {
      JLabel var1 = new JLabel(ROTATE_ICON);
      var1.setSize(ROTATE_ICON.getIconWidth(), ROTATE_ICON.getIconHeight());
      var1.setOpaque(false);
      return var1;
   }

   public boolean isStateHandled(mxCellState var1) {
      return this.graphComponent.getGraph().getModel().isVertex(var1.getCell());
   }

   public void mousePressed(MouseEvent var1) {
      if (this.currentState != null && this.handle.getParent() != null && var1.getSource() == this.handle) {
         this.start(var1);
         var1.consume();
      }

   }

   public void start(MouseEvent var1) {
      this.first = SwingUtilities.convertPoint(var1.getComponent(), var1.getPoint(), this.graphComponent.getGraphControl());
      if (!this.graphComponent.getGraph().isCellSelected(this.currentState.getCell())) {
         this.graphComponent.selectCellForEvent(this.currentState.getCell(), var1);
      }

   }

   public void mouseMoved(MouseEvent var1) {
      if (this.graphComponent.isEnabled() && this.isEnabled() && !var1.isConsumed()) {
         if (this.handle.getParent() != null && var1.getSource() == this.handle) {
            this.graphComponent.getGraphControl().setCursor(new Cursor(12));
            var1.consume();
         } else if (this.currentState == null || !this.currentState.getRectangle().contains(var1.getPoint())) {
            mxCellState var2 = this.graphComponent.getGraph().getView().getState(this.graphComponent.getCellAt(var1.getX(), var1.getY(), false));
            mxCellState var3 = null;
            if (var2 != null && this.isStateHandled(var2)) {
               var3 = var2;
            }

            if (this.currentState != var3) {
               this.currentState = var3;
               if (this.currentState == null && this.handle.getParent() != null) {
                  this.handle.setVisible(false);
                  this.handle.getParent().remove(this.handle);
               } else if (this.currentState != null) {
                  if (this.handle.getParent() == null) {
                     this.graphComponent.getGraphControl().add(this.handle, 0);
                     this.handle.setVisible(true);
                  }

                  this.handle.setLocation((int)(this.currentState.getX() + this.currentState.getWidth() - (double)this.handle.getWidth() - 4.0), (int)(this.currentState.getY() + this.currentState.getHeight() - (double)this.handle.getWidth() - 4.0));
               }
            }
         }
      }

   }

   public void mouseDragged(MouseEvent var1) {
      if (this.graphComponent.isEnabled() && this.isEnabled() && !var1.isConsumed() && this.first != null) {
         mxRectangle var2 = mxUtils.getBoundingBox(this.currentState, this.currentAngle * mxConstants.DEG_PER_RAD);
         Point var3 = SwingUtilities.convertPoint(var1.getComponent(), var1.getPoint(), this.graphComponent.getGraphControl());
         double var4 = this.currentState.getCenterX();
         double var6 = this.currentState.getCenterY();
         double var8 = var3.getX() - var4;
         double var10 = var3.getY() - var6;
         double var12 = Math.sqrt(var8 * var8 + var10 * var10);
         this.currentAngle = (double)(var3.getX() > var4 ? -1 : 1) * Math.acos(var10 / var12) + PI4;
         var2.add(mxUtils.getBoundingBox(this.currentState, this.currentAngle * mxConstants.DEG_PER_RAD));
         var2.grow(1.0);
         this.graphComponent.getGraphControl().repaint(var2.getRectangle());
         var1.consume();
      } else if (this.handle.getParent() != null) {
         this.handle.getParent().remove(this.handle);
      }

   }

   public void mouseReleased(MouseEvent var1) {
      if (this.graphComponent.isEnabled() && this.isEnabled() && !var1.isConsumed() && this.first != null) {
         double var2 = 0.0;
         Object var4 = null;
         if (this.currentState != null) {
            var4 = this.currentState.getCell();
            var2 = mxUtils.getDouble(this.currentState.getStyle(), mxConstants.STYLE_ROTATION);
         }

         var2 += this.currentAngle * mxConstants.DEG_PER_RAD;
         boolean var5 = var4 != null && this.first != null;
         this.reset();
         if (this.graphComponent.isEnabled() && this.isEnabled() && !var1.isConsumed() && var5) {
            this.graphComponent.getGraph().setCellStyles(mxConstants.STYLE_ROTATION, String.valueOf(var2), new Object[]{var4});
            this.graphComponent.getGraphControl().repaint();
            var1.consume();
         }
      }

   }

   public void reset() {
      if (this.handle.getParent() != null) {
         this.handle.getParent().remove(this.handle);
      }

      mxRectangle var1 = null;
      if (this.currentState != null && this.first != null) {
         var1 = mxUtils.getBoundingBox(this.currentState, this.currentAngle * mxConstants.DEG_PER_RAD);
         var1.grow(1.0);
      }

      this.currentState = null;
      this.currentAngle = 0.0;
      this.first = null;
      if (var1 != null) {
         this.graphComponent.getGraphControl().repaint(var1.getRectangle());
      }

   }

   public void paint(Graphics var1) {
      if (this.currentState != null && this.first != null) {
         Rectangle var2 = this.currentState.getRectangle();
         double var3 = this.currentAngle * mxConstants.DEG_PER_RAD;
         if (var3 != 0.0) {
            ((Graphics2D)var1).rotate(Math.toRadians(var3), this.currentState.getCenterX(), this.currentState.getCenterY());
         }

         mxUtils.setAntiAlias((Graphics2D)var1, true, false);
         var1.drawRect(var2.x, var2.y, var2.width, var2.height);
      }

   }

   static {
      ROTATE_ICON = new ImageIcon(mxRotationHandler.class.getResource("/com/mxgraph/swing/images/rotate.gif"));
      PI4 = 0.7853981633974483;
   }
}

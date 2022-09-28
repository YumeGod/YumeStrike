package com.mxgraph.swing.handler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMouseAdapter;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class mxPanningHandler extends mxMouseAdapter {
   private static final long serialVersionUID = 7969814728058376339L;
   protected mxGraphComponent graphComponent;
   protected boolean enabled = true;
   protected transient Point start;

   public mxPanningHandler(mxGraphComponent var1) {
      this.graphComponent = var1;
      var1.getGraphControl().addMouseListener(this);
      var1.getGraphControl().addMouseMotionListener(this);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public void mousePressed(MouseEvent var1) {
      if (this.isEnabled() && !var1.isConsumed() && this.graphComponent.isPanningEvent(var1) && !var1.isPopupTrigger()) {
         this.start = var1.getPoint();
      }

   }

   public void mouseDragged(MouseEvent var1) {
      if (!var1.isConsumed() && this.start != null) {
         int var2 = var1.getX() - this.start.x;
         int var3 = var1.getY() - this.start.y;
         Rectangle var4 = this.graphComponent.getViewport().getViewRect();
         int var5 = var4.x + (var2 > 0 ? 0 : var4.width) - var2;
         int var6 = var4.y + (var3 > 0 ? 0 : var4.height) - var3;
         this.graphComponent.getGraphControl().scrollRectToVisible(new Rectangle(var5, var6, 1, 1));
         var1.consume();
      }

   }

   public void mouseReleased(MouseEvent var1) {
      if (!var1.isConsumed() && this.start != null) {
         int var2 = Math.abs(this.start.x - var1.getX());
         int var3 = Math.abs(this.start.y - var1.getY());
         if (this.graphComponent.isSignificant((double)var2, (double)var3)) {
            var1.consume();
         }
      }

      this.start = null;
   }
}

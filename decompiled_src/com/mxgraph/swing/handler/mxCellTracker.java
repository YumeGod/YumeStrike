package com.mxgraph.swing.handler;

import com.mxgraph.swing.mxGraphComponent;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class mxCellTracker extends mxCellMarker implements MouseListener, MouseMotionListener {
   private static final long serialVersionUID = 7372144804885125688L;

   public mxCellTracker(mxGraphComponent var1, Color var2) {
      super(var1, var2);
      var1.getGraphControl().addMouseListener(this);
      var1.getGraphControl().addMouseMotionListener(this);
   }

   public void destroy() {
      this.graphComponent.getGraphControl().removeMouseListener(this);
      this.graphComponent.getGraphControl().removeMouseMotionListener(this);
   }

   public void mouseClicked(MouseEvent var1) {
   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseExited(MouseEvent var1) {
   }

   public void mousePressed(MouseEvent var1) {
   }

   public void mouseReleased(MouseEvent var1) {
      this.reset();
   }

   public void mouseDragged(MouseEvent var1) {
   }

   public void mouseMoved(MouseEvent var1) {
      this.process(var1);
   }
}

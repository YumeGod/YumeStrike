package org.apache.batik.swing.gvt;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class AbstractResetTransformInteractor implements Interactor {
   protected boolean finished = true;

   public boolean endInteraction() {
      return this.finished;
   }

   public void keyTyped(KeyEvent var1) {
      this.resetTransform(var1);
   }

   public void keyPressed(KeyEvent var1) {
      this.resetTransform(var1);
   }

   public void keyReleased(KeyEvent var1) {
      this.resetTransform(var1);
   }

   public void mouseClicked(MouseEvent var1) {
      this.resetTransform(var1);
   }

   public void mousePressed(MouseEvent var1) {
      this.resetTransform(var1);
   }

   public void mouseReleased(MouseEvent var1) {
      this.resetTransform(var1);
   }

   public void mouseEntered(MouseEvent var1) {
      this.resetTransform(var1);
   }

   public void mouseExited(MouseEvent var1) {
      this.resetTransform(var1);
   }

   public void mouseDragged(MouseEvent var1) {
      this.resetTransform(var1);
   }

   public void mouseMoved(MouseEvent var1) {
      this.resetTransform(var1);
   }

   protected void resetTransform(InputEvent var1) {
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      var2.resetRenderingTransform();
      this.finished = true;
   }
}

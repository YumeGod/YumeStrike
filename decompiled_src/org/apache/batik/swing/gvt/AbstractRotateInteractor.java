package org.apache.batik.swing.gvt;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class AbstractRotateInteractor extends InteractorAdapter {
   protected boolean finished;
   protected double initialRotation;

   public boolean endInteraction() {
      return this.finished;
   }

   public void mousePressed(MouseEvent var1) {
      this.finished = false;
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      Dimension var3 = var2.getSize();
      double var4 = (double)(var1.getX() - var3.width / 2);
      double var6 = (double)(var1.getY() - var3.height / 2);
      double var8 = -var6 / Math.sqrt(var4 * var4 + var6 * var6);
      this.initialRotation = var4 > 0.0 ? Math.acos(var8) : -Math.acos(var8);
   }

   public void mouseReleased(MouseEvent var1) {
      this.finished = true;
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      AffineTransform var3 = this.rotateTransform(var2.getSize(), var1.getX(), var1.getY());
      var3.concatenate(var2.getRenderingTransform());
      var2.setRenderingTransform(var3);
   }

   public void mouseExited(MouseEvent var1) {
      this.finished = true;
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      var2.setPaintingTransform((AffineTransform)null);
   }

   public void mouseDragged(MouseEvent var1) {
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      var2.setPaintingTransform(this.rotateTransform(var2.getSize(), var1.getX(), var1.getY()));
   }

   protected AffineTransform rotateTransform(Dimension var1, int var2, int var3) {
      double var4 = (double)(var2 - var1.width / 2);
      double var6 = (double)(var3 - var1.height / 2);
      double var8 = -var6 / Math.sqrt(var4 * var4 + var6 * var6);
      double var10 = var4 > 0.0 ? Math.acos(var8) : -Math.acos(var8);
      var10 -= this.initialRotation;
      return AffineTransform.getRotateInstance(var10, (double)(var1.width / 2), (double)(var1.height / 2));
   }
}

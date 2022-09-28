package org.apache.batik.swing.gvt;

import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class AbstractImageZoomInteractor extends InteractorAdapter {
   protected boolean finished = true;
   protected int xStart;
   protected int yStart;
   protected int xCurrent;
   protected int yCurrent;

   public boolean endInteraction() {
      return this.finished;
   }

   public void mousePressed(MouseEvent var1) {
      if (!this.finished) {
         JGVTComponent var2 = (JGVTComponent)var1.getSource();
         var2.setPaintingTransform((AffineTransform)null);
      } else {
         this.finished = false;
         this.xStart = var1.getX();
         this.yStart = var1.getY();
      }
   }

   public void mouseReleased(MouseEvent var1) {
      this.finished = true;
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      AffineTransform var3 = var2.getPaintingTransform();
      if (var3 != null) {
         AffineTransform var4 = (AffineTransform)var2.getRenderingTransform().clone();
         var4.preConcatenate(var3);
         var2.setRenderingTransform(var4);
      }

   }

   public void mouseDragged(MouseEvent var1) {
      JGVTComponent var3 = (JGVTComponent)var1.getSource();
      this.xCurrent = var1.getX();
      this.yCurrent = var1.getY();
      AffineTransform var2 = AffineTransform.getTranslateInstance((double)this.xStart, (double)this.yStart);
      int var4 = this.yCurrent - this.yStart;
      double var5;
      if (var4 < 0) {
         var4 -= 10;
         var5 = var4 > -15 ? 1.0 : -15.0 / (double)var4;
      } else {
         var4 += 10;
         var5 = var4 < 15 ? 1.0 : (double)var4 / 15.0;
      }

      var2.scale(var5, var5);
      var2.translate((double)(-this.xStart), (double)(-this.yStart));
      var3.setPaintingTransform(var2);
   }
}

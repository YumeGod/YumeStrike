package org.apache.batik.swing.gvt;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public abstract class AbstractPanInteractor extends InteractorAdapter {
   public static final Cursor PAN_CURSOR = new Cursor(13);
   protected boolean finished = true;
   protected int xStart;
   protected int yStart;
   protected int xCurrent;
   protected int yCurrent;
   protected Cursor previousCursor;

   public boolean endInteraction() {
      return this.finished;
   }

   public void mousePressed(MouseEvent var1) {
      if (!this.finished) {
         this.mouseExited(var1);
      } else {
         this.finished = false;
         this.xStart = var1.getX();
         this.yStart = var1.getY();
         JGVTComponent var2 = (JGVTComponent)var1.getSource();
         this.previousCursor = var2.getCursor();
         var2.setCursor(PAN_CURSOR);
      }
   }

   public void mouseReleased(MouseEvent var1) {
      if (!this.finished) {
         this.finished = true;
         JGVTComponent var2 = (JGVTComponent)var1.getSource();
         this.xCurrent = var1.getX();
         this.yCurrent = var1.getY();
         AffineTransform var3 = AffineTransform.getTranslateInstance((double)(this.xCurrent - this.xStart), (double)(this.yCurrent - this.yStart));
         AffineTransform var4 = (AffineTransform)var2.getRenderingTransform().clone();
         var4.preConcatenate(var3);
         var2.setRenderingTransform(var4);
         if (var2.getCursor() == PAN_CURSOR) {
            var2.setCursor(this.previousCursor);
         }

      }
   }

   public void mouseExited(MouseEvent var1) {
      this.finished = true;
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      var2.setPaintingTransform((AffineTransform)null);
      if (var2.getCursor() == PAN_CURSOR) {
         var2.setCursor(this.previousCursor);
      }

   }

   public void mouseDragged(MouseEvent var1) {
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      this.xCurrent = var1.getX();
      this.yCurrent = var1.getY();
      AffineTransform var3 = AffineTransform.getTranslateInstance((double)(this.xCurrent - this.xStart), (double)(this.yCurrent - this.yStart));
      var2.setPaintingTransform(var3);
   }
}

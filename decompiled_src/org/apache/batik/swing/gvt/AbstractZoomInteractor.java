package org.apache.batik.swing.gvt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class AbstractZoomInteractor extends InteractorAdapter {
   protected boolean finished = true;
   protected int xStart;
   protected int yStart;
   protected int xCurrent;
   protected int yCurrent;
   protected Line2D markerTop;
   protected Line2D markerLeft;
   protected Line2D markerBottom;
   protected Line2D markerRight;
   protected Overlay overlay = new ZoomOverlay();
   protected BasicStroke markerStroke = new BasicStroke(1.0F, 2, 0, 10.0F, new float[]{4.0F, 4.0F}, 0.0F);

   public boolean endInteraction() {
      return this.finished;
   }

   public void mousePressed(MouseEvent var1) {
      if (!this.finished) {
         this.mouseExited(var1);
      } else {
         this.finished = false;
         this.markerTop = null;
         this.markerLeft = null;
         this.markerBottom = null;
         this.markerRight = null;
         this.xStart = var1.getX();
         this.yStart = var1.getY();
         JGVTComponent var2 = (JGVTComponent)var1.getSource();
         var2.getOverlays().add(this.overlay);
      }
   }

   public void mouseReleased(MouseEvent var1) {
      this.finished = true;
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      var2.getOverlays().remove(this.overlay);
      this.overlay.paint(var2.getGraphics());
      this.xCurrent = var1.getX();
      this.yCurrent = var1.getY();
      if (this.xCurrent - this.xStart != 0 && this.yCurrent - this.yStart != 0) {
         int var3 = this.xCurrent - this.xStart;
         int var4 = this.yCurrent - this.yStart;
         if (var3 < 0) {
            var3 = -var3;
            this.xStart = this.xCurrent;
         }

         if (var4 < 0) {
            var4 = -var4;
            this.yStart = this.yCurrent;
         }

         Dimension var5 = var2.getSize();
         float var6 = (float)var5.width / (float)var3;
         float var7 = (float)var5.height / (float)var4;
         float var8 = var6 < var7 ? var6 : var7;
         AffineTransform var9 = new AffineTransform();
         var9.scale((double)var8, (double)var8);
         var9.translate((double)(-this.xStart), (double)(-this.yStart));
         var9.concatenate(var2.getRenderingTransform());
         var2.setRenderingTransform(var9);
      }

   }

   public void mouseExited(MouseEvent var1) {
      this.finished = true;
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      var2.getOverlays().remove(this.overlay);
      this.overlay.paint(var2.getGraphics());
   }

   public void mouseDragged(MouseEvent var1) {
      JGVTComponent var2 = (JGVTComponent)var1.getSource();
      this.overlay.paint(var2.getGraphics());
      this.xCurrent = var1.getX();
      this.yCurrent = var1.getY();
      float var3;
      float var5;
      if (this.xStart < this.xCurrent) {
         var3 = (float)this.xStart;
         var5 = (float)(this.xCurrent - this.xStart);
      } else {
         var3 = (float)this.xCurrent;
         var5 = (float)(this.xStart - this.xCurrent);
      }

      float var4;
      float var6;
      if (this.yStart < this.yCurrent) {
         var4 = (float)this.yStart;
         var6 = (float)(this.yCurrent - this.yStart);
      } else {
         var4 = (float)this.yCurrent;
         var6 = (float)(this.yStart - this.yCurrent);
      }

      Dimension var7 = var2.getSize();
      float var8 = (float)var7.width / (float)var7.height;
      if (var8 > var5 / var6) {
         var5 = var8 * var6;
      } else {
         var6 = var5 / var8;
      }

      this.markerTop = new Line2D.Float(var3, var4, var3 + var5, var4);
      this.markerLeft = new Line2D.Float(var3, var4, var3, var4 + var6);
      this.markerBottom = new Line2D.Float(var3, var4 + var6, var3 + var5, var4 + var6);
      this.markerRight = new Line2D.Float(var3 + var5, var4, var3 + var5, var4 + var6);
      this.overlay.paint(var2.getGraphics());
   }

   protected class ZoomOverlay implements Overlay {
      public void paint(Graphics var1) {
         if (AbstractZoomInteractor.this.markerTop != null) {
            Graphics2D var2 = (Graphics2D)var1;
            var2.setXORMode(Color.white);
            var2.setColor(Color.black);
            var2.setStroke(AbstractZoomInteractor.this.markerStroke);
            var2.draw(AbstractZoomInteractor.this.markerTop);
            var2.draw(AbstractZoomInteractor.this.markerLeft);
            var2.draw(AbstractZoomInteractor.this.markerBottom);
            var2.draw(AbstractZoomInteractor.this.markerRight);
         }

      }
   }
}

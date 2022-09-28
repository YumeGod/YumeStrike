package org.apache.batik.svggen;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.util.Hashtable;

class NullOp implements BufferedImageOp {
   public BufferedImage filter(BufferedImage var1, BufferedImage var2) {
      Graphics2D var3 = var2.createGraphics();
      var3.drawImage(var1, 0, 0, (ImageObserver)null);
      var3.dispose();
      return var2;
   }

   public Rectangle2D getBounds2D(BufferedImage var1) {
      return new Rectangle(0, 0, var1.getWidth(), var1.getHeight());
   }

   public BufferedImage createCompatibleDestImage(BufferedImage var1, ColorModel var2) {
      BufferedImage var3 = null;
      if (var2 == null) {
         var2 = var1.getColorModel();
      }

      var3 = new BufferedImage(var2, var2.createCompatibleWritableRaster(var1.getWidth(), var1.getHeight()), var2.isAlphaPremultiplied(), (Hashtable)null);
      return var3;
   }

   public Point2D getPoint2D(Point2D var1, Point2D var2) {
      if (var2 == null) {
         var2 = new Point2D.Double();
      }

      ((Point2D)var2).setLocation(var1.getX(), var1.getY());
      return (Point2D)var2;
   }

   public RenderingHints getRenderingHints() {
      return null;
   }
}

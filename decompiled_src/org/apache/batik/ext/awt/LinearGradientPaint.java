package org.apache.batik.ext.awt;

import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

public final class LinearGradientPaint extends MultipleGradientPaint {
   private Point2D start;
   private Point2D end;

   public LinearGradientPaint(float var1, float var2, float var3, float var4, float[] var5, Color[] var6) {
      this(new Point2D.Float(var1, var2), new Point2D.Float(var3, var4), var5, var6, NO_CYCLE, SRGB);
   }

   public LinearGradientPaint(float var1, float var2, float var3, float var4, float[] var5, Color[] var6, MultipleGradientPaint.CycleMethodEnum var7) {
      this(new Point2D.Float(var1, var2), new Point2D.Float(var3, var4), var5, var6, var7, SRGB);
   }

   public LinearGradientPaint(Point2D var1, Point2D var2, float[] var3, Color[] var4) {
      this(var1, var2, var3, var4, NO_CYCLE, SRGB);
   }

   public LinearGradientPaint(Point2D var1, Point2D var2, float[] var3, Color[] var4, MultipleGradientPaint.CycleMethodEnum var5, MultipleGradientPaint.ColorSpaceEnum var6) {
      this(var1, var2, var3, var4, var5, var6, new AffineTransform());
   }

   public LinearGradientPaint(Point2D var1, Point2D var2, float[] var3, Color[] var4, MultipleGradientPaint.CycleMethodEnum var5, MultipleGradientPaint.ColorSpaceEnum var6, AffineTransform var7) {
      super(var3, var4, var5, var6, var7);
      if (var1 != null && var2 != null) {
         if (var1.equals(var2)) {
            throw new IllegalArgumentException("Start point cannot equalendpoint");
         } else {
            this.start = (Point2D)var1.clone();
            this.end = (Point2D)var2.clone();
         }
      } else {
         throw new NullPointerException("Start and end points must benon-null");
      }
   }

   public PaintContext createContext(ColorModel var1, Rectangle var2, Rectangle2D var3, AffineTransform var4, RenderingHints var5) {
      var4 = new AffineTransform(var4);
      var4.concatenate(this.gradientTransform);

      try {
         return new LinearGradientPaintContext(var1, var2, var3, var4, var5, this.start, this.end, this.fractions, this.getColors(), this.cycleMethod, this.colorSpace);
      } catch (NoninvertibleTransformException var7) {
         var7.printStackTrace();
         throw new IllegalArgumentException("transform should beinvertible");
      }
   }

   public Point2D getStartPoint() {
      return new Point2D.Double(this.start.getX(), this.start.getY());
   }

   public Point2D getEndPoint() {
      return new Point2D.Double(this.end.getX(), this.end.getY());
   }
}

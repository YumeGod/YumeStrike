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

public final class RadialGradientPaint extends MultipleGradientPaint {
   private Point2D focus;
   private Point2D center;
   private float radius;

   public RadialGradientPaint(float var1, float var2, float var3, float[] var4, Color[] var5) {
      this(var1, var2, var3, var1, var2, var4, var5);
   }

   public RadialGradientPaint(Point2D var1, float var2, float[] var3, Color[] var4) {
      this(var1, var2, var1, var3, var4);
   }

   public RadialGradientPaint(float var1, float var2, float var3, float var4, float var5, float[] var6, Color[] var7) {
      this(new Point2D.Float(var1, var2), var3, new Point2D.Float(var4, var5), var6, var7, NO_CYCLE, SRGB);
   }

   public RadialGradientPaint(Point2D var1, float var2, Point2D var3, float[] var4, Color[] var5) {
      this(var1, var2, var3, var4, var5, NO_CYCLE, SRGB);
   }

   public RadialGradientPaint(Point2D var1, float var2, Point2D var3, float[] var4, Color[] var5, MultipleGradientPaint.CycleMethodEnum var6, MultipleGradientPaint.ColorSpaceEnum var7) {
      this(var1, var2, var3, var4, var5, var6, var7, new AffineTransform());
   }

   public RadialGradientPaint(Point2D var1, float var2, Point2D var3, float[] var4, Color[] var5, MultipleGradientPaint.CycleMethodEnum var6, MultipleGradientPaint.ColorSpaceEnum var7, AffineTransform var8) {
      super(var4, var5, var6, var7, var8);
      if (var1 == null) {
         throw new NullPointerException("Center point should not be null.");
      } else if (var3 == null) {
         throw new NullPointerException("Focus point should not be null.");
      } else if (var2 <= 0.0F) {
         throw new IllegalArgumentException("radius should be greater than zero");
      } else {
         this.center = (Point2D)var1.clone();
         this.focus = (Point2D)var3.clone();
         this.radius = var2;
      }
   }

   public RadialGradientPaint(Rectangle2D var1, float[] var2, Color[] var3) {
      this((float)var1.getX() + (float)var1.getWidth() / 2.0F, (float)var1.getY() + (float)var1.getWidth() / 2.0F, (float)var1.getWidth() / 2.0F, var2, var3);
   }

   public PaintContext createContext(ColorModel var1, Rectangle var2, Rectangle2D var3, AffineTransform var4, RenderingHints var5) {
      var4 = new AffineTransform(var4);
      var4.concatenate(this.gradientTransform);

      try {
         return new RadialGradientPaintContext(var1, var2, var3, var4, var5, (float)this.center.getX(), (float)this.center.getY(), this.radius, (float)this.focus.getX(), (float)this.focus.getY(), this.fractions, this.colors, this.cycleMethod, this.colorSpace);
      } catch (NoninvertibleTransformException var7) {
         throw new IllegalArgumentException("transform should be invertible");
      }
   }

   public Point2D getCenterPoint() {
      return new Point2D.Double(this.center.getX(), this.center.getY());
   }

   public Point2D getFocusPoint() {
      return new Point2D.Double(this.focus.getX(), this.focus.getY());
   }

   public float getRadius() {
      return this.radius;
   }
}

package org.apache.fop.area;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.apache.fop.datatypes.FODimension;

public class CTM implements Serializable {
   private double a;
   private double b;
   private double c;
   private double d;
   private double e;
   private double f;
   private static final CTM CTM_LRTB = new CTM(1.0, 0.0, 0.0, 1.0, 0.0, 0.0);
   private static final CTM CTM_RLTB = new CTM(-1.0, 0.0, 0.0, 1.0, 0.0, 0.0);
   private static final CTM CTM_TBRL = new CTM(0.0, 1.0, -1.0, 0.0, 0.0, 0.0);

   public CTM() {
      this.a = 1.0;
      this.b = 0.0;
      this.c = 0.0;
      this.d = 1.0;
      this.e = 0.0;
      this.f = 0.0;
   }

   public CTM(double a, double b, double c, double d, double e, double f) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.d = d;
      this.e = e;
      this.f = f;
   }

   public CTM(double x, double y) {
      this.a = 1.0;
      this.b = 0.0;
      this.c = 0.0;
      this.d = 1.0;
      this.e = x;
      this.f = y;
   }

   protected CTM(CTM ctm) {
      this.a = ctm.a;
      this.b = ctm.b;
      this.c = ctm.c;
      this.d = ctm.d;
      this.e = ctm.e;
      this.f = ctm.f;
   }

   public CTM(AffineTransform at) {
      double[] matrix = new double[6];
      at.getMatrix(matrix);
      this.a = matrix[0];
      this.b = matrix[1];
      this.c = matrix[2];
      this.d = matrix[3];
      this.e = matrix[4];
      this.f = matrix[5];
   }

   public static CTM getWMctm(int wm, int ipd, int bpd) {
      CTM wmctm;
      switch (wm) {
         case 79:
            return new CTM(CTM_LRTB);
         case 121:
            wmctm = new CTM(CTM_RLTB);
            wmctm.e = (double)ipd;
            return wmctm;
         case 140:
            wmctm = new CTM(CTM_TBRL);
            wmctm.e = (double)bpd;
            return wmctm;
         default:
            return null;
      }
   }

   public CTM multiply(CTM premult) {
      CTM result = new CTM(premult.a * this.a + premult.b * this.c, premult.a * this.b + premult.b * this.d, premult.c * this.a + premult.d * this.c, premult.c * this.b + premult.d * this.d, premult.e * this.a + premult.f * this.c + this.e, premult.e * this.b + premult.f * this.d + this.f);
      return result;
   }

   public CTM rotate(double angle) {
      double cos;
      double sin;
      if (angle != 90.0 && angle != -270.0) {
         if (angle != 270.0 && angle != -90.0) {
            if (angle != 180.0 && angle != -180.0) {
               double rad = Math.toRadians(angle);
               cos = Math.cos(rad);
               sin = Math.sin(rad);
            } else {
               cos = -1.0;
               sin = 0.0;
            }
         } else {
            cos = 0.0;
            sin = -1.0;
         }
      } else {
         cos = 0.0;
         sin = 1.0;
      }

      CTM rotate = new CTM(cos, -sin, sin, cos, 0.0, 0.0);
      return this.multiply(rotate);
   }

   public CTM translate(double x, double y) {
      CTM translate = new CTM(1.0, 0.0, 0.0, 1.0, x, y);
      return this.multiply(translate);
   }

   public CTM scale(double x, double y) {
      CTM scale = new CTM(x, 0.0, 0.0, y, 0.0, 0.0);
      return this.multiply(scale);
   }

   public Rectangle2D transform(Rectangle2D inRect) {
      int x1t = (int)(inRect.getX() * this.a + inRect.getY() * this.c + this.e);
      int y1t = (int)(inRect.getX() * this.b + inRect.getY() * this.d + this.f);
      int x2t = (int)((inRect.getX() + inRect.getWidth()) * this.a + (inRect.getY() + inRect.getHeight()) * this.c + this.e);
      int y2t = (int)((inRect.getX() + inRect.getWidth()) * this.b + (inRect.getY() + inRect.getHeight()) * this.d + this.f);
      int tmp;
      if (x1t > x2t) {
         tmp = x2t;
         x2t = x1t;
         x1t = tmp;
      }

      if (y1t > y2t) {
         tmp = y2t;
         y2t = y1t;
         y1t = tmp;
      }

      return new Rectangle(x1t, y1t, x2t - x1t, y2t - y1t);
   }

   public String toString() {
      return "[" + this.a + " " + this.b + " " + this.c + " " + this.d + " " + this.e + " " + this.f + "]";
   }

   public double[] toArray() {
      return new double[]{this.a, this.b, this.c, this.d, this.e, this.f};
   }

   public AffineTransform toAffineTransform() {
      return new AffineTransform(this.toArray());
   }

   public static CTM getCTMandRelDims(int absRefOrient, int writingMode, Rectangle2D absVPrect, FODimension reldims) {
      int width;
      int height;
      if (absRefOrient % 180 == 0) {
         width = (int)absVPrect.getWidth();
         height = (int)absVPrect.getHeight();
      } else {
         height = (int)absVPrect.getWidth();
         width = (int)absVPrect.getHeight();
      }

      CTM ctm = new CTM(absVPrect.getX(), absVPrect.getY());
      if (absRefOrient != 0) {
         switch (absRefOrient) {
            case -270:
            case 90:
               ctm = ctm.translate(0.0, (double)width);
               break;
            case -180:
            case 180:
               ctm = ctm.translate((double)width, (double)height);
               break;
            case -90:
            case 270:
               ctm = ctm.translate((double)height, 0.0);
               break;
            default:
               throw new RuntimeException();
         }

         ctm = ctm.rotate((double)absRefOrient);
      }

      if (writingMode != 79 && writingMode != 121) {
         reldims.ipd = height;
         reldims.bpd = width;
      } else {
         reldims.ipd = width;
         reldims.bpd = height;
      }

      return ctm.multiply(getWMctm(writingMode, reldims.ipd, reldims.bpd));
   }
}

package org.apache.batik.dom.svg;

import java.awt.geom.Point2D;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGPoint;

public class SVGPathSupport {
   public static float getTotalLength(SVGOMPathElement var0) {
      SVGPathContext var1 = (SVGPathContext)var0.getSVGContext();
      return var1.getTotalLength();
   }

   public static int getPathSegAtLength(SVGOMPathElement var0, float var1) {
      SVGPathContext var2 = (SVGPathContext)var0.getSVGContext();
      return var2.getPathSegAtLength(var1);
   }

   public static SVGPoint getPointAtLength(final SVGOMPathElement var0, final float var1) {
      final SVGPathContext var2 = (SVGPathContext)var0.getSVGContext();
      return var2 == null ? null : new SVGPoint() {
         public float getX() {
            Point2D var1x = var2.getPointAtLength(var1);
            return (float)var1x.getX();
         }

         public float getY() {
            Point2D var1x = var2.getPointAtLength(var1);
            return (float)var1x.getY();
         }

         public void setX(float var1x) throws DOMException {
            throw var0.createDOMException((short)7, "readonly.point", (Object[])null);
         }

         public void setY(float var1x) throws DOMException {
            throw var0.createDOMException((short)7, "readonly.point", (Object[])null);
         }

         public SVGPoint matrixTransform(SVGMatrix var1x) {
            throw var0.createDOMException((short)7, "readonly.point", (Object[])null);
         }
      };
   }
}

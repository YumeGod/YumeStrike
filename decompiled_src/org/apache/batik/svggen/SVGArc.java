package org.apache.batik.svggen;

import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import org.w3c.dom.Element;

public class SVGArc extends SVGGraphicObjectConverter {
   private SVGLine svgLine;
   private SVGEllipse svgEllipse;

   public SVGArc(SVGGeneratorContext var1) {
      super(var1);
   }

   public Element toSVG(Arc2D var1) {
      double var2 = var1.getAngleExtent();
      double var4 = var1.getWidth();
      double var6 = var1.getHeight();
      if (var4 != 0.0 && var6 != 0.0) {
         if (!(var2 >= 360.0) && !(var2 <= -360.0)) {
            Element var18 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "path");
            StringBuffer var9 = new StringBuffer(64);
            Point2D var10 = var1.getStartPoint();
            Point2D var11 = var1.getEndPoint();
            int var12 = var1.getArcType();
            var9.append("M");
            var9.append(this.doubleString(var10.getX()));
            var9.append(" ");
            var9.append(this.doubleString(var10.getY()));
            var9.append(" ");
            var9.append("A");
            var9.append(this.doubleString(var4 / 2.0));
            var9.append(" ");
            var9.append(this.doubleString(var6 / 2.0));
            var9.append(" ");
            var9.append('0');
            var9.append(" ");
            if (var2 > 0.0) {
               if (var2 > 180.0) {
                  var9.append('1');
               } else {
                  var9.append('0');
               }

               var9.append(" ");
               var9.append('0');
            } else {
               if (var2 < -180.0) {
                  var9.append('1');
               } else {
                  var9.append('0');
               }

               var9.append(" ");
               var9.append('1');
            }

            var9.append(" ");
            var9.append(this.doubleString(var11.getX()));
            var9.append(" ");
            var9.append(this.doubleString(var11.getY()));
            if (var12 == 1) {
               var9.append("Z");
            } else if (var12 == 2) {
               double var13 = var1.getX() + var4 / 2.0;
               double var15 = var1.getY() + var6 / 2.0;
               var9.append("L");
               var9.append(" ");
               var9.append(this.doubleString(var13));
               var9.append(" ");
               var9.append(this.doubleString(var15));
               var9.append(" ");
               var9.append("Z");
            }

            var18.setAttributeNS((String)null, "d", var9.toString());
            return var18;
         } else {
            Ellipse2D.Double var17 = new Ellipse2D.Double(var1.getX(), var1.getY(), var4, var6);
            if (this.svgEllipse == null) {
               this.svgEllipse = new SVGEllipse(this.generatorContext);
            }

            return this.svgEllipse.toSVG(var17);
         }
      } else {
         Line2D.Double var8 = new Line2D.Double(var1.getX(), var1.getY(), var1.getX() + var4, var1.getY() + var6);
         if (this.svgLine == null) {
            this.svgLine = new SVGLine(this.generatorContext);
         }

         return this.svgLine.toSVG(var8);
      }
   }
}

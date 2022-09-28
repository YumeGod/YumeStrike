package org.apache.batik.svggen;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import org.w3c.dom.Element;

public class SVGRectangle extends SVGGraphicObjectConverter {
   private SVGLine svgLine;

   public SVGRectangle(SVGGeneratorContext var1) {
      super(var1);
      this.svgLine = new SVGLine(var1);
   }

   public Element toSVG(Rectangle2D var1) {
      return this.toSVG((RectangularShape)var1);
   }

   public Element toSVG(RoundRectangle2D var1) {
      Element var2 = this.toSVG((RectangularShape)var1);
      if (var2 != null && var2.getTagName() == "rect") {
         var2.setAttributeNS((String)null, "rx", this.doubleString(Math.abs(var1.getArcWidth() / 2.0)));
         var2.setAttributeNS((String)null, "ry", this.doubleString(Math.abs(var1.getArcHeight() / 2.0)));
      }

      return var2;
   }

   private Element toSVG(RectangularShape var1) {
      if (var1.getWidth() > 0.0 && var1.getHeight() > 0.0) {
         Element var3 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "rect");
         var3.setAttributeNS((String)null, "x", this.doubleString(var1.getX()));
         var3.setAttributeNS((String)null, "y", this.doubleString(var1.getY()));
         var3.setAttributeNS((String)null, "width", this.doubleString(var1.getWidth()));
         var3.setAttributeNS((String)null, "height", this.doubleString(var1.getHeight()));
         return var3;
      } else {
         Line2D.Double var2;
         if (var1.getWidth() == 0.0 && var1.getHeight() > 0.0) {
            var2 = new Line2D.Double(var1.getX(), var1.getY(), var1.getX(), var1.getY() + var1.getHeight());
            return this.svgLine.toSVG(var2);
         } else if (var1.getWidth() > 0.0 && var1.getHeight() == 0.0) {
            var2 = new Line2D.Double(var1.getX(), var1.getY(), var1.getX() + var1.getWidth(), var1.getY());
            return this.svgLine.toSVG(var2);
         } else {
            return null;
         }
      }
   }
}

package org.apache.batik.svggen;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import org.w3c.dom.Element;

public class SVGEllipse extends SVGGraphicObjectConverter {
   private SVGLine svgLine;

   public SVGEllipse(SVGGeneratorContext var1) {
      super(var1);
   }

   public Element toSVG(Ellipse2D var1) {
      if (!(var1.getWidth() < 0.0) && !(var1.getHeight() < 0.0)) {
         return var1.getWidth() == var1.getHeight() ? this.toSVGCircle(var1) : this.toSVGEllipse(var1);
      } else {
         return null;
      }
   }

   private Element toSVGCircle(Ellipse2D var1) {
      Element var2 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "circle");
      var2.setAttributeNS((String)null, "cx", this.doubleString(var1.getX() + var1.getWidth() / 2.0));
      var2.setAttributeNS((String)null, "cy", this.doubleString(var1.getY() + var1.getHeight() / 2.0));
      var2.setAttributeNS((String)null, "r", this.doubleString(var1.getWidth() / 2.0));
      return var2;
   }

   private Element toSVGEllipse(Ellipse2D var1) {
      if (var1.getWidth() > 0.0 && var1.getHeight() > 0.0) {
         Element var3 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "ellipse");
         var3.setAttributeNS((String)null, "cx", this.doubleString(var1.getX() + var1.getWidth() / 2.0));
         var3.setAttributeNS((String)null, "cy", this.doubleString(var1.getY() + var1.getHeight() / 2.0));
         var3.setAttributeNS((String)null, "rx", this.doubleString(var1.getWidth() / 2.0));
         var3.setAttributeNS((String)null, "ry", this.doubleString(var1.getHeight() / 2.0));
         return var3;
      } else {
         Line2D.Double var2;
         if (var1.getWidth() == 0.0 && var1.getHeight() > 0.0) {
            var2 = new Line2D.Double(var1.getX(), var1.getY(), var1.getX(), var1.getY() + var1.getHeight());
            if (this.svgLine == null) {
               this.svgLine = new SVGLine(this.generatorContext);
            }

            return this.svgLine.toSVG(var2);
         } else if (var1.getWidth() > 0.0 && var1.getHeight() == 0.0) {
            var2 = new Line2D.Double(var1.getX(), var1.getY(), var1.getX() + var1.getWidth(), var1.getY());
            if (this.svgLine == null) {
               this.svgLine = new SVGLine(this.generatorContext);
            }

            return this.svgLine.toSVG(var2);
         } else {
            return null;
         }
      }
   }
}

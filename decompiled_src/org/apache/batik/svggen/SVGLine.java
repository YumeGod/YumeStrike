package org.apache.batik.svggen;

import java.awt.geom.Line2D;
import org.w3c.dom.Element;

public class SVGLine extends SVGGraphicObjectConverter {
   public SVGLine(SVGGeneratorContext var1) {
      super(var1);
   }

   public Element toSVG(Line2D var1) {
      Element var2 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "line");
      var2.setAttributeNS((String)null, "x1", this.doubleString(var1.getX1()));
      var2.setAttributeNS((String)null, "y1", this.doubleString(var1.getY1()));
      var2.setAttributeNS((String)null, "x2", this.doubleString(var1.getX2()));
      var2.setAttributeNS((String)null, "y2", this.doubleString(var1.getY2()));
      return var2;
   }
}

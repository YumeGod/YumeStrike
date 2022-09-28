package org.apache.batik.svggen;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import org.w3c.dom.Element;

public class SVGShape extends SVGGraphicObjectConverter {
   private SVGArc svgArc;
   private SVGEllipse svgEllipse;
   private SVGLine svgLine;
   private SVGPath svgPath;
   private SVGPolygon svgPolygon;
   private SVGRectangle svgRectangle;

   public SVGShape(SVGGeneratorContext var1) {
      super(var1);
      this.svgArc = new SVGArc(var1);
      this.svgEllipse = new SVGEllipse(var1);
      this.svgLine = new SVGLine(var1);
      this.svgPath = new SVGPath(var1);
      this.svgPolygon = new SVGPolygon(var1);
      this.svgRectangle = new SVGRectangle(var1);
   }

   public Element toSVG(Shape var1) {
      if (var1 instanceof Polygon) {
         return this.svgPolygon.toSVG((Polygon)var1);
      } else if (var1 instanceof Rectangle2D) {
         return this.svgRectangle.toSVG((Rectangle2D)var1);
      } else if (var1 instanceof RoundRectangle2D) {
         return this.svgRectangle.toSVG((RoundRectangle2D)var1);
      } else if (var1 instanceof Ellipse2D) {
         return this.svgEllipse.toSVG((Ellipse2D)var1);
      } else if (var1 instanceof Line2D) {
         return this.svgLine.toSVG((Line2D)var1);
      } else {
         return var1 instanceof Arc2D ? this.svgArc.toSVG((Arc2D)var1) : this.svgPath.toSVG(var1);
      }
   }
}

package org.apache.batik.svggen;

import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import org.w3c.dom.Element;

public class SVGPolygon extends SVGGraphicObjectConverter {
   public SVGPolygon(SVGGeneratorContext var1) {
      super(var1);
   }

   public Element toSVG(Polygon var1) {
      Element var2 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "polygon");
      StringBuffer var3 = new StringBuffer(" ");
      PathIterator var4 = var1.getPathIterator((AffineTransform)null);

      for(float[] var5 = new float[6]; !var4.isDone(); var4.next()) {
         int var6 = var4.currentSegment(var5);
         switch (var6) {
            case 0:
               this.appendPoint(var3, var5[0], var5[1]);
               break;
            case 1:
               this.appendPoint(var3, var5[0], var5[1]);
               break;
            case 2:
            case 3:
            default:
               throw new Error("invalid segmentType:" + var6);
            case 4:
         }
      }

      var2.setAttributeNS((String)null, "points", var3.substring(0, var3.length() - 1));
      return var2;
   }

   private void appendPoint(StringBuffer var1, float var2, float var3) {
      var1.append(this.doubleString((double)var2));
      var1.append(" ");
      var1.append(this.doubleString((double)var3));
      var1.append(" ");
   }
}

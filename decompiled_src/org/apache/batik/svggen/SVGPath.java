package org.apache.batik.svggen;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import org.w3c.dom.Element;

public class SVGPath extends SVGGraphicObjectConverter {
   public SVGPath(SVGGeneratorContext var1) {
      super(var1);
   }

   public Element toSVG(Shape var1) {
      String var2 = toSVGPathData(var1, this.generatorContext);
      if (var2 != null && var2.length() != 0) {
         Element var3 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "path");
         var3.setAttributeNS((String)null, "d", var2);
         if (var1.getPathIterator((AffineTransform)null).getWindingRule() == 0) {
            var3.setAttributeNS((String)null, "fill-rule", "evenodd");
         }

         return var3;
      } else {
         return null;
      }
   }

   public static String toSVGPathData(Shape var0, SVGGeneratorContext var1) {
      StringBuffer var2 = new StringBuffer(40);
      PathIterator var3 = var0.getPathIterator((AffineTransform)null);
      float[] var4 = new float[6];

      for(boolean var5 = false; !var3.isDone(); var3.next()) {
         int var6 = var3.currentSegment(var4);
         switch (var6) {
            case 0:
               var2.append("M");
               appendPoint(var2, var4[0], var4[1], var1);
               break;
            case 1:
               var2.append("L");
               appendPoint(var2, var4[0], var4[1], var1);
               break;
            case 2:
               var2.append("Q");
               appendPoint(var2, var4[0], var4[1], var1);
               appendPoint(var2, var4[2], var4[3], var1);
               break;
            case 3:
               var2.append("C");
               appendPoint(var2, var4[0], var4[1], var1);
               appendPoint(var2, var4[2], var4[3], var1);
               appendPoint(var2, var4[4], var4[5], var1);
               break;
            case 4:
               var2.append("Z");
               break;
            default:
               throw new Error("invalid segmentType:" + var6);
         }
      }

      if (var2.length() > 0) {
         return var2.toString().trim();
      } else {
         return "";
      }
   }

   private static void appendPoint(StringBuffer var0, float var1, float var2, SVGGeneratorContext var3) {
      var0.append(var3.doubleString((double)var1));
      var0.append(" ");
      var0.append(var3.doubleString((double)var2));
      var0.append(" ");
   }
}

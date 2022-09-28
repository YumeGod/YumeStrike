package org.apache.batik.svggen;

import java.awt.BasicStroke;
import org.apache.batik.ext.awt.g2d.GraphicContext;

public class SVGBasicStroke extends AbstractSVGConverter {
   public SVGBasicStroke(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      return var1.getStroke() instanceof BasicStroke ? this.toSVG((BasicStroke)var1.getStroke()) : null;
   }

   public final SVGStrokeDescriptor toSVG(BasicStroke var1) {
      String var2 = this.doubleString((double)var1.getLineWidth());
      String var3 = endCapToSVG(var1.getEndCap());
      String var4 = joinToSVG(var1.getLineJoin());
      String var5 = this.doubleString((double)var1.getMiterLimit());
      float[] var6 = var1.getDashArray();
      String var7 = null;
      if (var6 != null) {
         var7 = this.dashArrayToSVG(var6);
      } else {
         var7 = "none";
      }

      String var8 = this.doubleString((double)var1.getDashPhase());
      return new SVGStrokeDescriptor(var2, var3, var4, var5, var7, var8);
   }

   private final String dashArrayToSVG(float[] var1) {
      StringBuffer var2 = new StringBuffer(var1.length * 8);
      if (var1.length > 0) {
         var2.append(this.doubleString((double)var1[0]));
      }

      for(int var3 = 1; var3 < var1.length; ++var3) {
         var2.append(",");
         var2.append(this.doubleString((double)var1[var3]));
      }

      return var2.toString();
   }

   private static String joinToSVG(int var0) {
      switch (var0) {
         case 0:
         default:
            return "miter";
         case 1:
            return "round";
         case 2:
            return "bevel";
      }
   }

   private static String endCapToSVG(int var0) {
      switch (var0) {
         case 0:
            return "butt";
         case 1:
            return "round";
         case 2:
         default:
            return "square";
      }
   }
}

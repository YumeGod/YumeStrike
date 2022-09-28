package org.apache.batik.svggen;

import java.awt.RenderingHints;
import org.apache.batik.ext.awt.g2d.GraphicContext;

public class SVGRenderingHints extends AbstractSVGConverter {
   public SVGRenderingHints(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      return toSVG(var1.getRenderingHints());
   }

   public static SVGHintsDescriptor toSVG(RenderingHints var0) {
      String var1 = "auto";
      String var2 = "auto";
      String var3 = "auto";
      String var4 = "auto";
      String var5 = "auto";
      if (var0 != null) {
         Object var6 = var0.get(RenderingHints.KEY_RENDERING);
         if (var6 == RenderingHints.VALUE_RENDER_DEFAULT) {
            var1 = "auto";
            var2 = "auto";
            var3 = "auto";
            var4 = "auto";
            var5 = "auto";
         } else if (var6 == RenderingHints.VALUE_RENDER_SPEED) {
            var1 = "sRGB";
            var2 = "optimizeSpeed";
            var3 = "optimizeSpeed";
            var4 = "geometricPrecision";
            var5 = "optimizeSpeed";
         } else if (var6 == RenderingHints.VALUE_RENDER_QUALITY) {
            var1 = "linearRGB";
            var2 = "optimizeQuality";
            var3 = "optimizeQuality";
            var4 = "geometricPrecision";
            var5 = "optimizeQuality";
         }

         Object var7 = var0.get(RenderingHints.KEY_FRACTIONALMETRICS);
         if (var7 == RenderingHints.VALUE_FRACTIONALMETRICS_ON) {
            var3 = "optimizeQuality";
            var4 = "geometricPrecision";
         } else if (var7 == RenderingHints.VALUE_FRACTIONALMETRICS_OFF) {
            var3 = "optimizeSpeed";
            var4 = "optimizeSpeed";
         } else if (var7 == RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT) {
            var3 = "auto";
            var4 = "auto";
         }

         Object var8 = var0.get(RenderingHints.KEY_ANTIALIASING);
         if (var8 == RenderingHints.VALUE_ANTIALIAS_ON) {
            var3 = "optimizeLegibility";
            var4 = "auto";
         } else if (var8 == RenderingHints.VALUE_ANTIALIAS_OFF) {
            var3 = "geometricPrecision";
            var4 = "crispEdges";
         } else if (var8 == RenderingHints.VALUE_ANTIALIAS_DEFAULT) {
            var3 = "auto";
            var4 = "auto";
         }

         Object var9 = var0.get(RenderingHints.KEY_TEXT_ANTIALIASING);
         if (var9 == RenderingHints.VALUE_TEXT_ANTIALIAS_ON) {
            var3 = "geometricPrecision";
         } else if (var9 == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) {
            var3 = "optimizeSpeed";
         } else if (var9 == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
            var3 = "auto";
         }

         Object var10 = var0.get(RenderingHints.KEY_COLOR_RENDERING);
         if (var10 == RenderingHints.VALUE_COLOR_RENDER_DEFAULT) {
            var2 = "auto";
         } else if (var10 == RenderingHints.VALUE_COLOR_RENDER_QUALITY) {
            var2 = "optimizeQuality";
         } else if (var10 == RenderingHints.VALUE_COLOR_RENDER_SPEED) {
            var2 = "optimizeSpeed";
         }

         Object var11 = var0.get(RenderingHints.KEY_INTERPOLATION);
         if (var11 == RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR) {
            var5 = "optimizeSpeed";
         } else if (var11 == RenderingHints.VALUE_INTERPOLATION_BICUBIC || var11 == RenderingHints.VALUE_INTERPOLATION_BILINEAR) {
            var5 = "optimizeQuality";
         }
      }

      return new SVGHintsDescriptor(var1, var2, var3, var4, var5);
   }
}

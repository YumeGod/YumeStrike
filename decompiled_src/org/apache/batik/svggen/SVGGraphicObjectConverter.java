package org.apache.batik.svggen;

public abstract class SVGGraphicObjectConverter implements SVGSyntax {
   protected SVGGeneratorContext generatorContext;

   public SVGGraphicObjectConverter(SVGGeneratorContext var1) {
      if (var1 == null) {
         throw new SVGGraphics2DRuntimeException("generatorContext should not be null");
      } else {
         this.generatorContext = var1;
      }
   }

   public final String doubleString(double var1) {
      return this.generatorContext.doubleString(var1);
   }
}

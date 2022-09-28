package org.apache.batik.svggen;

import java.awt.Rectangle;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import java.util.LinkedList;
import java.util.List;

public class SVGBufferedImageOp extends AbstractSVGFilterConverter {
   private SVGLookupOp svgLookupOp;
   private SVGRescaleOp svgRescaleOp;
   private SVGConvolveOp svgConvolveOp;
   private SVGCustomBufferedImageOp svgCustomBufferedImageOp;

   public SVGBufferedImageOp(SVGGeneratorContext var1) {
      super(var1);
      this.svgLookupOp = new SVGLookupOp(var1);
      this.svgRescaleOp = new SVGRescaleOp(var1);
      this.svgConvolveOp = new SVGConvolveOp(var1);
      this.svgCustomBufferedImageOp = new SVGCustomBufferedImageOp(var1);
   }

   public List getDefinitionSet() {
      LinkedList var1 = new LinkedList(this.svgLookupOp.getDefinitionSet());
      var1.addAll(this.svgRescaleOp.getDefinitionSet());
      var1.addAll(this.svgConvolveOp.getDefinitionSet());
      var1.addAll(this.svgCustomBufferedImageOp.getDefinitionSet());
      return var1;
   }

   public SVGLookupOp getLookupOpConverter() {
      return this.svgLookupOp;
   }

   public SVGRescaleOp getRescaleOpConverter() {
      return this.svgRescaleOp;
   }

   public SVGConvolveOp getConvolveOpConverter() {
      return this.svgConvolveOp;
   }

   public SVGCustomBufferedImageOp getCustomBufferedImageOpConverter() {
      return this.svgCustomBufferedImageOp;
   }

   public SVGFilterDescriptor toSVG(BufferedImageOp var1, Rectangle var2) {
      SVGFilterDescriptor var3 = this.svgCustomBufferedImageOp.toSVG(var1, var2);
      if (var3 == null) {
         if (var1 instanceof LookupOp) {
            var3 = this.svgLookupOp.toSVG(var1, var2);
         } else if (var1 instanceof RescaleOp) {
            var3 = this.svgRescaleOp.toSVG(var1, var2);
         } else if (var1 instanceof ConvolveOp) {
            var3 = this.svgConvolveOp.toSVG(var1, var2);
         }
      }

      return var3;
   }
}

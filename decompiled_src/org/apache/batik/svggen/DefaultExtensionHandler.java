package org.apache.batik.svggen;

import java.awt.Composite;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.image.BufferedImageOp;

public class DefaultExtensionHandler implements ExtensionHandler {
   public SVGPaintDescriptor handlePaint(Paint var1, SVGGeneratorContext var2) {
      return null;
   }

   public SVGCompositeDescriptor handleComposite(Composite var1, SVGGeneratorContext var2) {
      return null;
   }

   public SVGFilterDescriptor handleFilter(BufferedImageOp var1, Rectangle var2, SVGGeneratorContext var3) {
      return null;
   }
}

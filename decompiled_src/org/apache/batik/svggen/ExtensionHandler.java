package org.apache.batik.svggen;

import java.awt.Composite;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.image.BufferedImageOp;

public interface ExtensionHandler {
   SVGPaintDescriptor handlePaint(Paint var1, SVGGeneratorContext var2);

   SVGCompositeDescriptor handleComposite(Composite var1, SVGGeneratorContext var2);

   SVGFilterDescriptor handleFilter(BufferedImageOp var1, Rectangle var2, SVGGeneratorContext var3);
}

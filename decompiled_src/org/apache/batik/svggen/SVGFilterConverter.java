package org.apache.batik.svggen;

import java.awt.Rectangle;
import java.awt.image.BufferedImageOp;
import java.util.List;

public interface SVGFilterConverter extends SVGSyntax {
   SVGFilterDescriptor toSVG(BufferedImageOp var1, Rectangle var2);

   List getDefinitionSet();
}

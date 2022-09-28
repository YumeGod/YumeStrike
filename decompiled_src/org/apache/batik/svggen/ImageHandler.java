package org.apache.batik.svggen;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import org.w3c.dom.Element;

public interface ImageHandler extends SVGSyntax {
   void handleImage(Image var1, Element var2, SVGGeneratorContext var3);

   void handleImage(RenderedImage var1, Element var2, SVGGeneratorContext var3);

   void handleImage(RenderableImage var1, Element var2, SVGGeneratorContext var3);
}

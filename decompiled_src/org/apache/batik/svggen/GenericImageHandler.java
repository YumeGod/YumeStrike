package org.apache.batik.svggen;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import org.w3c.dom.Element;

public interface GenericImageHandler {
   void setDOMTreeManager(DOMTreeManager var1);

   Element createElement(SVGGeneratorContext var1);

   AffineTransform handleImage(Image var1, Element var2, int var3, int var4, int var5, int var6, SVGGeneratorContext var7);

   AffineTransform handleImage(RenderedImage var1, Element var2, int var3, int var4, int var5, int var6, SVGGeneratorContext var7);

   AffineTransform handleImage(RenderableImage var1, Element var2, double var3, double var5, double var7, double var9, SVGGeneratorContext var11);
}

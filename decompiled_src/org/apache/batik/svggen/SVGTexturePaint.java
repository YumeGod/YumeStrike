package org.apache.batik.svggen;

import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGTexturePaint extends AbstractSVGConverter {
   public SVGTexturePaint(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      return this.toSVG((TexturePaint)var1.getPaint());
   }

   public SVGPaintDescriptor toSVG(TexturePaint var1) {
      SVGPaintDescriptor var2 = (SVGPaintDescriptor)this.descMap.get(var1);
      Document var3 = this.generatorContext.domFactory;
      if (var2 == null) {
         Rectangle2D var4 = var1.getAnchorRect();
         Element var5 = var3.createElementNS("http://www.w3.org/2000/svg", "pattern");
         var5.setAttributeNS((String)null, "patternUnits", "userSpaceOnUse");
         var5.setAttributeNS((String)null, "x", this.doubleString(var4.getX()));
         var5.setAttributeNS((String)null, "y", this.doubleString(var4.getY()));
         var5.setAttributeNS((String)null, "width", this.doubleString(var4.getWidth()));
         var5.setAttributeNS((String)null, "height", this.doubleString(var4.getHeight()));
         BufferedImage var6 = var1.getImage();
         if (var6.getWidth() > 0 && var6.getHeight() > 0 && ((double)var6.getWidth() != var4.getWidth() || (double)var6.getHeight() != var4.getHeight()) && var4.getWidth() > 0.0 && var4.getHeight() > 0.0) {
            double var7 = var4.getWidth() / (double)var6.getWidth();
            double var9 = var4.getHeight() / (double)var6.getHeight();
            BufferedImage var11 = new BufferedImage((int)(var7 * (double)var6.getWidth()), (int)(var9 * (double)var6.getHeight()), 2);
            Graphics2D var12 = var11.createGraphics();
            var12.scale(var7, var9);
            var12.drawImage(var6, 0, 0, (ImageObserver)null);
            var12.dispose();
            var6 = var11;
         }

         Element var13 = this.generatorContext.genericImageHandler.createElement(this.generatorContext);
         this.generatorContext.genericImageHandler.handleImage((RenderedImage)var6, var13, 0, 0, var6.getWidth(), var6.getHeight(), this.generatorContext);
         var5.appendChild(var13);
         var5.setAttributeNS((String)null, "id", this.generatorContext.idGenerator.generateID("pattern"));
         String var8 = "url(#" + var5.getAttributeNS((String)null, "id") + ")";
         var2 = new SVGPaintDescriptor(var8, "1", var5);
         this.descMap.put(var1, var2);
         this.defSet.add(var5);
      }

      return var2;
   }
}

package org.apache.batik.svggen;

import java.awt.Paint;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.w3c.dom.Element;

public class SVGCustomPaint extends AbstractSVGConverter {
   public SVGCustomPaint(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      return this.toSVG(var1.getPaint());
   }

   public SVGPaintDescriptor toSVG(Paint var1) {
      SVGPaintDescriptor var2 = (SVGPaintDescriptor)this.descMap.get(var1);
      if (var2 == null) {
         var2 = this.generatorContext.extensionHandler.handlePaint(var1, this.generatorContext);
         if (var2 != null) {
            Element var3 = var2.getDef();
            if (var3 != null) {
               this.defSet.add(var3);
            }

            this.descMap.put(var1, var2);
         }
      }

      return var2;
   }
}

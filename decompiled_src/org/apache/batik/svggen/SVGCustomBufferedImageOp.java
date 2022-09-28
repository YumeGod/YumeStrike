package org.apache.batik.svggen;

import java.awt.Rectangle;
import java.awt.image.BufferedImageOp;
import org.w3c.dom.Element;

public class SVGCustomBufferedImageOp extends AbstractSVGFilterConverter {
   private static final String ERROR_EXTENSION = "SVGCustomBufferedImageOp:: ExtensionHandler could not convert filter";

   public SVGCustomBufferedImageOp(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGFilterDescriptor toSVG(BufferedImageOp var1, Rectangle var2) {
      SVGFilterDescriptor var3 = (SVGFilterDescriptor)this.descMap.get(var1);
      if (var3 == null) {
         var3 = this.generatorContext.extensionHandler.handleFilter(var1, var2, this.generatorContext);
         if (var3 != null) {
            Element var4 = var3.getDef();
            if (var4 != null) {
               this.defSet.add(var4);
            }

            this.descMap.put(var1, var3);
         } else {
            System.err.println("SVGCustomBufferedImageOp:: ExtensionHandler could not convert filter");
         }
      }

      return var3;
   }
}

package org.apache.batik.svggen;

import java.awt.Composite;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.w3c.dom.Element;

public class SVGCustomComposite extends AbstractSVGConverter {
   public SVGCustomComposite(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      return this.toSVG(var1.getComposite());
   }

   public SVGCompositeDescriptor toSVG(Composite var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         SVGCompositeDescriptor var2 = (SVGCompositeDescriptor)this.descMap.get(var1);
         if (var2 == null) {
            SVGCompositeDescriptor var3 = this.generatorContext.extensionHandler.handleComposite(var1, this.generatorContext);
            if (var3 != null) {
               Element var4 = var3.getDef();
               if (var4 != null) {
                  this.defSet.add(var4);
               }

               this.descMap.put(var1, var3);
            }
         }

         return var2;
      }
   }
}

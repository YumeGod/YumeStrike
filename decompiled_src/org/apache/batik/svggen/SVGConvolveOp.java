package org.apache.batik.svggen;

import java.awt.Rectangle;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGConvolveOp extends AbstractSVGFilterConverter {
   public SVGConvolveOp(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGFilterDescriptor toSVG(BufferedImageOp var1, Rectangle var2) {
      return var1 instanceof ConvolveOp ? this.toSVG((ConvolveOp)var1) : null;
   }

   public SVGFilterDescriptor toSVG(ConvolveOp var1) {
      SVGFilterDescriptor var2 = (SVGFilterDescriptor)this.descMap.get(var1);
      Document var3 = this.generatorContext.domFactory;
      if (var2 == null) {
         Kernel var4 = var1.getKernel();
         Element var5 = var3.createElementNS("http://www.w3.org/2000/svg", "filter");
         Element var6 = var3.createElementNS("http://www.w3.org/2000/svg", "feConvolveMatrix");
         var6.setAttributeNS((String)null, "order", var4.getWidth() + " " + var4.getHeight());
         float[] var7 = var4.getKernelData((float[])null);
         StringBuffer var8 = new StringBuffer(var7.length * 8);

         for(int var9 = 0; var9 < var7.length; ++var9) {
            var8.append(this.doubleString((double)var7[var9]));
            var8.append(" ");
         }

         var6.setAttributeNS((String)null, "kernelMatrix", var8.toString().trim());
         var5.appendChild(var6);
         var5.setAttributeNS((String)null, "id", this.generatorContext.idGenerator.generateID("convolve"));
         if (var1.getEdgeCondition() == 1) {
            var6.setAttributeNS((String)null, "edgeMode", "duplicate");
         } else {
            var6.setAttributeNS((String)null, "edgeMode", "none");
         }

         StringBuffer var10 = new StringBuffer("url(");
         var10.append("#");
         var10.append(var5.getAttributeNS((String)null, "id"));
         var10.append(")");
         var2 = new SVGFilterDescriptor(var10.toString(), var5);
         this.defSet.add(var5);
         this.descMap.put(var1, var2);
      }

      return var2;
   }
}

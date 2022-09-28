package org.apache.batik.svggen;

import java.awt.Rectangle;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGRescaleOp extends AbstractSVGFilterConverter {
   public SVGRescaleOp(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGFilterDescriptor toSVG(BufferedImageOp var1, Rectangle var2) {
      return var1 instanceof RescaleOp ? this.toSVG((RescaleOp)var1) : null;
   }

   public SVGFilterDescriptor toSVG(RescaleOp var1) {
      SVGFilterDescriptor var2 = (SVGFilterDescriptor)this.descMap.get(var1);
      Document var3 = this.generatorContext.domFactory;
      if (var2 == null) {
         Element var4 = var3.createElementNS("http://www.w3.org/2000/svg", "filter");
         Element var5 = var3.createElementNS("http://www.w3.org/2000/svg", "feComponentTransfer");
         float[] var6 = var1.getOffsets((float[])null);
         float[] var7 = var1.getScaleFactors((float[])null);
         if (var6.length != var7.length) {
            throw new SVGGraphics2DRuntimeException("RescapeOp offsets and scaleFactor array length do not match");
         }

         if (var6.length != 1 && var6.length != 3 && var6.length != 4) {
            throw new SVGGraphics2DRuntimeException("BufferedImage RescaleOp should have 1, 3 or 4 scale factors");
         }

         Element var8 = var3.createElementNS("http://www.w3.org/2000/svg", "feFuncR");
         Element var9 = var3.createElementNS("http://www.w3.org/2000/svg", "feFuncG");
         Element var10 = var3.createElementNS("http://www.w3.org/2000/svg", "feFuncB");
         Element var11 = null;
         String var12 = "linear";
         String var13;
         if (var6.length == 1) {
            var13 = this.doubleString((double)var7[0]);
            String var14 = this.doubleString((double)var6[0]);
            var8.setAttributeNS((String)null, "type", var12);
            var9.setAttributeNS((String)null, "type", var12);
            var10.setAttributeNS((String)null, "type", var12);
            var8.setAttributeNS((String)null, "slope", var13);
            var9.setAttributeNS((String)null, "slope", var13);
            var10.setAttributeNS((String)null, "slope", var13);
            var8.setAttributeNS((String)null, "intercept", var14);
            var9.setAttributeNS((String)null, "intercept", var14);
            var10.setAttributeNS((String)null, "intercept", var14);
         } else if (var6.length >= 3) {
            var8.setAttributeNS((String)null, "type", var12);
            var9.setAttributeNS((String)null, "type", var12);
            var10.setAttributeNS((String)null, "type", var12);
            var8.setAttributeNS((String)null, "slope", this.doubleString((double)var7[0]));
            var9.setAttributeNS((String)null, "slope", this.doubleString((double)var7[1]));
            var10.setAttributeNS((String)null, "slope", this.doubleString((double)var7[2]));
            var8.setAttributeNS((String)null, "intercept", this.doubleString((double)var6[0]));
            var9.setAttributeNS((String)null, "intercept", this.doubleString((double)var6[1]));
            var10.setAttributeNS((String)null, "intercept", this.doubleString((double)var6[2]));
            if (var6.length == 4) {
               var11 = var3.createElementNS("http://www.w3.org/2000/svg", "feFuncA");
               var11.setAttributeNS((String)null, "type", var12);
               var11.setAttributeNS((String)null, "slope", this.doubleString((double)var7[3]));
               var11.setAttributeNS((String)null, "intercept", this.doubleString((double)var6[3]));
            }
         }

         var5.appendChild(var8);
         var5.appendChild(var9);
         var5.appendChild(var10);
         if (var11 != null) {
            var5.appendChild(var11);
         }

         var4.appendChild(var5);
         var4.setAttributeNS((String)null, "id", this.generatorContext.idGenerator.generateID("componentTransfer"));
         var13 = "url(#" + var4.getAttributeNS((String)null, "id") + ")";
         var2 = new SVGFilterDescriptor(var13, var4);
         this.defSet.add(var4);
         this.descMap.put(var1, var2);
      }

      return var2;
   }
}

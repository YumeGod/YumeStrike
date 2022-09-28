package org.apache.batik.svggen;

import java.awt.Rectangle;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.util.Arrays;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGLookupOp extends AbstractSVGFilterConverter {
   private static final double GAMMA = 0.4166666666666667;
   private static final int[] linearToSRGBLut = new int[256];
   private static final int[] sRGBToLinear = new int[256];

   public SVGLookupOp(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGFilterDescriptor toSVG(BufferedImageOp var1, Rectangle var2) {
      return var1 instanceof LookupOp ? this.toSVG((LookupOp)var1) : null;
   }

   public SVGFilterDescriptor toSVG(LookupOp var1) {
      SVGFilterDescriptor var2 = (SVGFilterDescriptor)this.descMap.get(var1);
      Document var3 = this.generatorContext.domFactory;
      if (var2 == null) {
         Element var4 = var3.createElementNS("http://www.w3.org/2000/svg", "filter");
         Element var5 = var3.createElementNS("http://www.w3.org/2000/svg", "feComponentTransfer");
         String[] var6 = this.convertLookupTables(var1);
         Element var7 = var3.createElementNS("http://www.w3.org/2000/svg", "feFuncR");
         Element var8 = var3.createElementNS("http://www.w3.org/2000/svg", "feFuncG");
         Element var9 = var3.createElementNS("http://www.w3.org/2000/svg", "feFuncB");
         Element var10 = null;
         String var11 = "table";
         if (var6.length == 1) {
            var7.setAttributeNS((String)null, "type", var11);
            var8.setAttributeNS((String)null, "type", var11);
            var9.setAttributeNS((String)null, "type", var11);
            var7.setAttributeNS((String)null, "tableValues", var6[0]);
            var8.setAttributeNS((String)null, "tableValues", var6[0]);
            var9.setAttributeNS((String)null, "tableValues", var6[0]);
         } else if (var6.length >= 3) {
            var7.setAttributeNS((String)null, "type", var11);
            var8.setAttributeNS((String)null, "type", var11);
            var9.setAttributeNS((String)null, "type", var11);
            var7.setAttributeNS((String)null, "tableValues", var6[0]);
            var8.setAttributeNS((String)null, "tableValues", var6[1]);
            var9.setAttributeNS((String)null, "tableValues", var6[2]);
            if (var6.length == 4) {
               var10 = var3.createElementNS("http://www.w3.org/2000/svg", "feFuncA");
               var10.setAttributeNS((String)null, "type", var11);
               var10.setAttributeNS((String)null, "tableValues", var6[3]);
            }
         }

         var5.appendChild(var7);
         var5.appendChild(var8);
         var5.appendChild(var9);
         if (var10 != null) {
            var5.appendChild(var10);
         }

         var4.appendChild(var5);
         var4.setAttributeNS((String)null, "id", this.generatorContext.idGenerator.generateID("componentTransfer"));
         String var12 = "url(#" + var4.getAttributeNS((String)null, "id") + ")";
         var2 = new SVGFilterDescriptor(var12, var4);
         this.defSet.add(var4);
         this.descMap.put(var1, var2);
      }

      return var2;
   }

   private String[] convertLookupTables(LookupOp var1) {
      LookupTable var2 = var1.getTable();
      int var3 = var2.getNumComponents();
      if (var3 != 1 && var3 != 3 && var3 != 4) {
         throw new SVGGraphics2DRuntimeException("BufferedImage LookupOp should have 1, 3 or 4 lookup arrays");
      } else {
         StringBuffer[] var4 = new StringBuffer[var3];

         for(int var5 = 0; var5 < var3; ++var5) {
            var4[var5] = new StringBuffer();
         }

         int var7;
         int var8;
         int var9;
         if (!(var2 instanceof ByteLookupTable)) {
            int[] var10 = new int[var3];
            int[] var6 = new int[var3];
            var7 = var2.getOffset();

            for(var8 = 0; var8 < var7; ++var8) {
               for(var9 = 0; var9 < var3; ++var9) {
                  var4[var9].append(this.doubleString((double)var8 / 255.0)).append(" ");
               }
            }

            for(var8 = var7; var8 <= 255; ++var8) {
               Arrays.fill(var10, var8);
               var2.lookupPixel(var10, var6);

               for(var9 = 0; var9 < var3; ++var9) {
                  var4[var9].append(this.doubleString((double)var6[var9] / 255.0)).append(" ");
               }
            }
         } else {
            byte[] var11 = new byte[var3];
            byte[] var12 = new byte[var3];
            var7 = var2.getOffset();

            for(var8 = 0; var8 < var7; ++var8) {
               for(var9 = 0; var9 < var3; ++var9) {
                  var4[var9].append(this.doubleString((double)var8 / 255.0)).append(" ");
               }
            }

            for(var8 = 0; var8 <= 255; ++var8) {
               Arrays.fill(var11, (byte)(255 & var8));
               ((ByteLookupTable)var2).lookupPixel(var11, var12);

               for(var9 = 0; var9 < var3; ++var9) {
                  var4[var9].append(this.doubleString((double)(255 & var12[var9]) / 255.0)).append(" ");
               }
            }
         }

         String[] var13 = new String[var3];

         for(int var14 = 0; var14 < var3; ++var14) {
            var13[var14] = var4[var14].toString().trim();
         }

         return var13;
      }
   }

   static {
      for(int var0 = 0; var0 < 256; ++var0) {
         float var1 = (float)var0 / 255.0F;
         if ((double)var1 <= 0.0031308) {
            var1 *= 12.92F;
         } else {
            var1 = 1.055F * (float)Math.pow((double)var1, 0.4166666666666667) - 0.055F;
         }

         linearToSRGBLut[var0] = Math.round(var1 * 255.0F);
         var1 = (float)var0 / 255.0F;
         if ((double)var1 <= 0.04045) {
            var1 /= 12.92F;
         } else {
            var1 = (float)Math.pow((double)((var1 + 0.055F) / 1.055F), 2.4);
         }

         sRGBToLinear[var0] = Math.round(var1 * 255.0F);
      }

   }
}

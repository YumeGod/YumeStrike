package org.apache.batik.bridge;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.Kernel;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.ConvolveMatrixRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeConvolveMatrixElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feConvolveMatrix";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      int[] var8 = convertOrder(var2, var1);
      float[] var9 = convertKernelMatrix(var2, var8, var1);
      float var10 = convertDivisor(var2, var9, var1);
      float var11 = convertNumber(var2, "bias", 0.0F, var1);
      int[] var12 = convertTarget(var2, var8, var1);
      PadMode var13 = convertEdgeMode(var2, var1);
      double[] var14 = convertKernelUnitLength(var2, var1);
      boolean var15 = convertPreserveAlpha(var2, var1);
      Filter var16 = getIn(var2, var3, var4, var5, var7, var1);
      if (var16 == null) {
         return null;
      } else {
         Rectangle2D var17 = var16.getBounds2D();
         Rectangle2D var18 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var17, var6, var1);
         PadRable8Bit var19 = new PadRable8Bit(var16, var18, PadMode.ZERO_PAD);
         ConvolveMatrixRable8Bit var20 = new ConvolveMatrixRable8Bit(var19);

         for(int var21 = 0; var21 < var9.length; ++var21) {
            var9[var21] /= var10;
         }

         var20.setKernel(new Kernel(var8[0], var8[1], var9));
         var20.setTarget(new Point(var12[0], var12[1]));
         var20.setBias((double)var11);
         var20.setEdgeMode(var13);
         var20.setKernelUnitLength(var14);
         var20.setPreserveAlpha(var15);
         handleColorInterpolationFilters(var20, var2);
         PadRable8Bit var22 = new PadRable8Bit(var20, var18, PadMode.ZERO_PAD);
         updateFilterMap(var2, var22, var7);
         return var22;
      }
   }

   protected static int[] convertOrder(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "order");
      if (var2.length() == 0) {
         return new int[]{3, 3};
      } else {
         int[] var3 = new int[2];
         StringTokenizer var4 = new StringTokenizer(var2, " ,");

         try {
            var3[0] = SVGUtilities.convertSVGInteger(var4.nextToken());
            if (var4.hasMoreTokens()) {
               var3[1] = SVGUtilities.convertSVGInteger(var4.nextToken());
            } else {
               var3[1] = var3[0];
            }
         } catch (NumberFormatException var6) {
            throw new BridgeException(var1, var0, var6, "attribute.malformed", new Object[]{"order", var2, var6});
         }

         if (!var4.hasMoreTokens() && var3[0] > 0 && var3[1] > 0) {
            return var3;
         } else {
            throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"order", var2});
         }
      }
   }

   protected static float[] convertKernelMatrix(Element var0, int[] var1, BridgeContext var2) {
      String var3 = var0.getAttributeNS((String)null, "kernelMatrix");
      if (var3.length() == 0) {
         throw new BridgeException(var2, var0, "attribute.missing", new Object[]{"kernelMatrix"});
      } else {
         int var4 = var1[0] * var1[1];
         float[] var5 = new float[var4];
         StringTokenizer var6 = new StringTokenizer(var3, " ,");
         int var7 = 0;

         try {
            while(var6.hasMoreTokens() && var7 < var4) {
               var5[var7++] = SVGUtilities.convertSVGNumber(var6.nextToken());
            }
         } catch (NumberFormatException var9) {
            throw new BridgeException(var2, var0, var9, "attribute.malformed", new Object[]{"kernelMatrix", var3, var9});
         }

         if (var7 != var4) {
            throw new BridgeException(var2, var0, "attribute.malformed", new Object[]{"kernelMatrix", var3});
         } else {
            return var5;
         }
      }
   }

   protected static float convertDivisor(Element var0, float[] var1, BridgeContext var2) {
      String var3 = var0.getAttributeNS((String)null, "divisor");
      if (var3.length() != 0) {
         try {
            return SVGUtilities.convertSVGNumber(var3);
         } catch (NumberFormatException var6) {
            throw new BridgeException(var2, var0, var6, "attribute.malformed", new Object[]{"divisor", var3, var6});
         }
      } else {
         float var4 = 0.0F;

         for(int var5 = 0; var5 < var1.length; ++var5) {
            var4 += var1[var5];
         }

         return var4 == 0.0F ? 1.0F : var4;
      }
   }

   protected static int[] convertTarget(Element var0, int[] var1, BridgeContext var2) {
      int[] var3 = new int[2];
      String var4 = var0.getAttributeNS((String)null, "targetX");
      int var5;
      if (var4.length() == 0) {
         var3[0] = var1[0] / 2;
      } else {
         try {
            var5 = SVGUtilities.convertSVGInteger(var4);
            if (var5 < 0 || var5 >= var1[0]) {
               throw new BridgeException(var2, var0, "attribute.malformed", new Object[]{"targetX", var4});
            }

            var3[0] = var5;
         } catch (NumberFormatException var7) {
            throw new BridgeException(var2, var0, var7, "attribute.malformed", new Object[]{"targetX", var4, var7});
         }
      }

      var4 = var0.getAttributeNS((String)null, "targetY");
      if (var4.length() == 0) {
         var3[1] = var1[1] / 2;
      } else {
         try {
            var5 = SVGUtilities.convertSVGInteger(var4);
            if (var5 < 0 || var5 >= var1[1]) {
               throw new BridgeException(var2, var0, "attribute.malformed", new Object[]{"targetY", var4});
            }

            var3[1] = var5;
         } catch (NumberFormatException var6) {
            throw new BridgeException(var2, var0, var6, "attribute.malformed", new Object[]{"targetY", var4, var6});
         }
      }

      return var3;
   }

   protected static double[] convertKernelUnitLength(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "kernelUnitLength");
      if (var2.length() == 0) {
         return null;
      } else {
         double[] var3 = new double[2];
         StringTokenizer var4 = new StringTokenizer(var2, " ,");

         try {
            var3[0] = (double)SVGUtilities.convertSVGNumber(var4.nextToken());
            if (var4.hasMoreTokens()) {
               var3[1] = (double)SVGUtilities.convertSVGNumber(var4.nextToken());
            } else {
               var3[1] = var3[0];
            }
         } catch (NumberFormatException var6) {
            throw new BridgeException(var1, var0, var6, "attribute.malformed", new Object[]{"kernelUnitLength", var2});
         }

         if (!var4.hasMoreTokens() && !(var3[0] <= 0.0) && !(var3[1] <= 0.0)) {
            return var3;
         } else {
            throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"kernelUnitLength", var2});
         }
      }
   }

   protected static PadMode convertEdgeMode(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "edgeMode");
      if (var2.length() == 0) {
         return PadMode.REPLICATE;
      } else if ("duplicate".equals(var2)) {
         return PadMode.REPLICATE;
      } else if ("wrap".equals(var2)) {
         return PadMode.WRAP;
      } else if ("none".equals(var2)) {
         return PadMode.ZERO_PAD;
      } else {
         throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"edgeMode", var2});
      }
   }

   protected static boolean convertPreserveAlpha(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "preserveAlpha");
      if (var2.length() == 0) {
         return false;
      } else if ("true".equals(var2)) {
         return true;
      } else if ("false".equals(var2)) {
         return false;
      } else {
         throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"preserveAlpha", var2});
      }
   }
}

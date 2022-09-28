package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.ColorMatrixRable;
import org.apache.batik.ext.awt.image.renderable.ColorMatrixRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeColorMatrixElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feColorMatrix";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      Filter var8 = getIn(var2, var3, var4, var5, var7, var1);
      if (var8 == null) {
         return null;
      } else {
         Rectangle2D var9 = var8.getBounds2D();
         Rectangle2D var10 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var9, var6, var1);
         int var11 = convertType(var2, var1);
         ColorMatrixRable var12;
         switch (var11) {
            case 0:
               float[][] var14 = convertValuesToMatrix(var2, var1);
               var12 = ColorMatrixRable8Bit.buildMatrix(var14);
               break;
            case 1:
               float var15 = convertValuesToSaturate(var2, var1);
               var12 = ColorMatrixRable8Bit.buildSaturate(var15);
               break;
            case 2:
               float var13 = convertValuesToHueRotate(var2, var1);
               var12 = ColorMatrixRable8Bit.buildHueRotate(var13);
               break;
            case 3:
               var12 = ColorMatrixRable8Bit.buildLuminanceToAlpha();
               break;
            default:
               throw new Error("invalid convertType:" + var11);
         }

         var12.setSource(var8);
         handleColorInterpolationFilters(var12, var2);
         PadRable8Bit var16 = new PadRable8Bit(var12, var10, PadMode.ZERO_PAD);
         updateFilterMap(var2, var16, var7);
         return var16;
      }
   }

   protected static float[][] convertValuesToMatrix(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "values");
      float[][] var3 = new float[4][5];
      if (var2.length() == 0) {
         var3[0][0] = 1.0F;
         var3[1][1] = 1.0F;
         var3[2][2] = 1.0F;
         var3[3][3] = 1.0F;
         return var3;
      } else {
         StringTokenizer var4 = new StringTokenizer(var2, " ,");
         int var5 = 0;

         try {
            while(var5 < 20 && var4.hasMoreTokens()) {
               var3[var5 / 5][var5 % 5] = SVGUtilities.convertSVGNumber(var4.nextToken());
               ++var5;
            }
         } catch (NumberFormatException var7) {
            throw new BridgeException(var1, var0, var7, "attribute.malformed", new Object[]{"values", var2, var7});
         }

         if (var5 == 20 && !var4.hasMoreTokens()) {
            for(int var6 = 0; var6 < 4; ++var6) {
               var3[var6][4] *= 255.0F;
            }

            return var3;
         } else {
            throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"values", var2});
         }
      }
   }

   protected static float convertValuesToSaturate(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "values");
      if (var2.length() == 0) {
         return 1.0F;
      } else {
         try {
            return SVGUtilities.convertSVGNumber(var2);
         } catch (NumberFormatException var4) {
            throw new BridgeException(var1, var0, var4, "attribute.malformed", new Object[]{"values", var2});
         }
      }
   }

   protected static float convertValuesToHueRotate(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "values");
      if (var2.length() == 0) {
         return 0.0F;
      } else {
         try {
            return (float)Math.toRadians((double)SVGUtilities.convertSVGNumber(var2));
         } catch (NumberFormatException var4) {
            throw new BridgeException(var1, var0, var4, "attribute.malformed", new Object[]{"values", var2});
         }
      }
   }

   protected static int convertType(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "type");
      if (var2.length() == 0) {
         return 0;
      } else if ("hueRotate".equals(var2)) {
         return 2;
      } else if ("luminanceToAlpha".equals(var2)) {
         return 3;
      } else if ("matrix".equals(var2)) {
         return 0;
      } else if ("saturate".equals(var2)) {
         return 1;
      } else {
         throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"type", var2});
      }
   }
}

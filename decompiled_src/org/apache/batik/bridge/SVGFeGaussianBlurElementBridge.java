package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.GaussianBlurRable8Bit;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeGaussianBlurElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feGaussianBlur";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      float[] var8 = convertStdDeviation(var2, var1);
      if (!(var8[0] < 0.0F) && !(var8[1] < 0.0F)) {
         Filter var9 = getIn(var2, var3, var4, var5, var7, var1);
         if (var9 == null) {
            return null;
         } else {
            Rectangle2D var10 = var9.getBounds2D();
            Rectangle2D var11 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var10, var6, var1);
            PadRable8Bit var12 = new PadRable8Bit(var9, var11, PadMode.ZERO_PAD);
            GaussianBlurRable8Bit var13 = new GaussianBlurRable8Bit(var12, (double)var8[0], (double)var8[1]);
            handleColorInterpolationFilters(var13, var2);
            PadRable8Bit var14 = new PadRable8Bit(var13, var11, PadMode.ZERO_PAD);
            updateFilterMap(var2, var14, var7);
            return var14;
         }
      } else {
         throw new BridgeException(var1, var2, "attribute.malformed", new Object[]{"stdDeviation", String.valueOf(var8[0]) + var8[1]});
      }
   }

   protected static float[] convertStdDeviation(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "stdDeviation");
      if (var2.length() == 0) {
         return new float[]{0.0F, 0.0F};
      } else {
         float[] var3 = new float[2];
         StringTokenizer var4 = new StringTokenizer(var2, " ,");

         try {
            var3[0] = SVGUtilities.convertSVGNumber(var4.nextToken());
            if (var4.hasMoreTokens()) {
               var3[1] = SVGUtilities.convertSVGNumber(var4.nextToken());
            } else {
               var3[1] = var3[0];
            }
         } catch (NumberFormatException var6) {
            throw new BridgeException(var1, var0, var6, "attribute.malformed", new Object[]{"stdDeviation", var2, var6});
         }

         if (var4.hasMoreTokens()) {
            throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"stdDeviation", var2});
         } else {
            return var3;
         }
      }
   }
}

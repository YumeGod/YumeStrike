package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.MorphologyRable8Bit;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeMorphologyElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feMorphology";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      float[] var8 = convertRadius(var2, var1);
      if (var8[0] != 0.0F && var8[1] != 0.0F) {
         boolean var9 = convertOperator(var2, var1);
         Filter var10 = getIn(var2, var3, var4, var5, var7, var1);
         if (var10 == null) {
            return null;
         } else {
            Rectangle2D var11 = var10.getBounds2D();
            Rectangle2D var12 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var11, var6, var1);
            PadRable8Bit var13 = new PadRable8Bit(var10, var12, PadMode.ZERO_PAD);
            MorphologyRable8Bit var14 = new MorphologyRable8Bit(var13, (double)var8[0], (double)var8[1], var9);
            handleColorInterpolationFilters(var14, var2);
            PadRable8Bit var15 = new PadRable8Bit(var14, var12, PadMode.ZERO_PAD);
            updateFilterMap(var2, var15, var7);
            return var15;
         }
      } else {
         return null;
      }
   }

   protected static float[] convertRadius(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "radius");
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
            throw new BridgeException(var1, var0, var6, "attribute.malformed", new Object[]{"radius", var2, var6});
         }

         if (!var4.hasMoreTokens() && !(var3[0] < 0.0F) && !(var3[1] < 0.0F)) {
            return var3;
         } else {
            throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"radius", var2});
         }
      }
   }

   protected static boolean convertOperator(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "operator");
      if (var2.length() == 0) {
         return false;
      } else if ("erode".equals(var2)) {
         return false;
      } else if ("dilate".equals(var2)) {
         return true;
      } else {
         throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"operator", var2});
      }
   }
}

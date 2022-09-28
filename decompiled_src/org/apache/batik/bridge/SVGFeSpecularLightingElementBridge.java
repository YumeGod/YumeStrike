package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.batik.ext.awt.image.Light;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.SpecularLightingRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeSpecularLightingElementBridge extends AbstractSVGLightingElementBridge {
   public String getLocalName() {
      return "feSpecularLighting";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      float var8 = convertNumber(var2, "surfaceScale", 1.0F, var1);
      float var9 = convertNumber(var2, "specularConstant", 1.0F, var1);
      float var10 = convertSpecularExponent(var2, var1);
      Light var11 = extractLight(var2, var1);
      double[] var12 = convertKernelUnitLength(var2, var1);
      Filter var13 = getIn(var2, var3, var4, var5, var7, var1);
      if (var13 == null) {
         return null;
      } else {
         Rectangle2D var14 = var13.getBounds2D();
         Rectangle2D var15 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var14, var6, var1);
         SpecularLightingRable8Bit var16 = new SpecularLightingRable8Bit(var13, var15, var11, (double)var9, (double)var10, (double)var8, var12);
         handleColorInterpolationFilters(var16, var2);
         updateFilterMap(var2, var16, var7);
         return var16;
      }
   }

   protected static float convertSpecularExponent(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "specularExponent");
      if (var2.length() == 0) {
         return 1.0F;
      } else {
         try {
            float var3 = SVGUtilities.convertSVGNumber(var2);
            if (!(var3 < 1.0F) && !(var3 > 128.0F)) {
               return var3;
            } else {
               throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"specularConstant", var2});
            }
         } catch (NumberFormatException var4) {
            throw new BridgeException(var1, var0, var4, "attribute.malformed", new Object[]{"specularConstant", var2, var4});
         }
      }
   }
}

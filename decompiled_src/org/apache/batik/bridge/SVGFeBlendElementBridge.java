package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;
import org.apache.batik.ext.awt.image.CompositeRule;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.CompositeRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeBlendElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feBlend";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      CompositeRule var8 = convertMode(var2, var1);
      Filter var9 = getIn(var2, var3, var4, var5, var7, var1);
      if (var9 == null) {
         return null;
      } else {
         Filter var10 = getIn2(var2, var3, var4, var5, var7, var1);
         if (var10 == null) {
            return null;
         } else {
            Rectangle2D var11 = (Rectangle2D)var9.getBounds2D().clone();
            var11.add(var10.getBounds2D());
            Rectangle2D var12 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var11, var6, var1);
            ArrayList var13 = new ArrayList(2);
            var13.add(var10);
            var13.add(var9);
            CompositeRable8Bit var14 = new CompositeRable8Bit(var13, var8, true);
            handleColorInterpolationFilters(var14, var2);
            PadRable8Bit var15 = new PadRable8Bit(var14, var12, PadMode.ZERO_PAD);
            updateFilterMap(var2, var15, var7);
            return var15;
         }
      }
   }

   protected static CompositeRule convertMode(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "mode");
      if (var2.length() == 0) {
         return CompositeRule.OVER;
      } else if ("normal".equals(var2)) {
         return CompositeRule.OVER;
      } else if ("multiply".equals(var2)) {
         return CompositeRule.MULTIPLY;
      } else if ("screen".equals(var2)) {
         return CompositeRule.SCREEN;
      } else if ("darken".equals(var2)) {
         return CompositeRule.DARKEN;
      } else if ("lighten".equals(var2)) {
         return CompositeRule.LIGHTEN;
      } else {
         throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"mode", var2});
      }
   }
}

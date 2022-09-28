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

public class SVGFeCompositeElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feComposite";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      CompositeRule var8 = convertOperator(var2, var1);
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

   protected static CompositeRule convertOperator(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "operator");
      if (var2.length() == 0) {
         return CompositeRule.OVER;
      } else if ("atop".equals(var2)) {
         return CompositeRule.ATOP;
      } else if ("in".equals(var2)) {
         return CompositeRule.IN;
      } else if ("over".equals(var2)) {
         return CompositeRule.OVER;
      } else if ("out".equals(var2)) {
         return CompositeRule.OUT;
      } else if ("xor".equals(var2)) {
         return CompositeRule.XOR;
      } else if ("arithmetic".equals(var2)) {
         float var3 = convertNumber(var0, "k1", 0.0F, var1);
         float var4 = convertNumber(var0, "k2", 0.0F, var1);
         float var5 = convertNumber(var0, "k3", 0.0F, var1);
         float var6 = convertNumber(var0, "k4", 0.0F, var1);
         return CompositeRule.ARITHMETIC(var3, var4, var5, var6);
      } else {
         throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"operator", var2});
      }
   }
}

package org.apache.batik.bridge;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.AffineRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeOffsetElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feOffset";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      Filter var8 = getIn(var2, var3, var4, var5, var7, var1);
      if (var8 == null) {
         return null;
      } else {
         Rectangle2D var9 = var8.getBounds2D();
         Rectangle2D var10 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var9, var6, var1);
         float var11 = convertNumber(var2, "dx", 0.0F, var1);
         float var12 = convertNumber(var2, "dy", 0.0F, var1);
         AffineTransform var13 = AffineTransform.getTranslateInstance((double)var11, (double)var12);
         PadRable8Bit var14 = new PadRable8Bit(var8, var10, PadMode.ZERO_PAD);
         AffineRable8Bit var15 = new AffineRable8Bit(var14, var13);
         PadRable8Bit var16 = new PadRable8Bit(var15, var10, PadMode.ZERO_PAD);
         handleColorInterpolationFilters(var16, var2);
         updateFilterMap(var2, var16, var7);
         return var16;
      }
   }
}

package org.apache.batik.bridge;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.FloodRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeFloodElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feFlood";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      Rectangle2D var8 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var6, var6, var1);
      Color var9 = CSSUtilities.convertFloodColor(var2, var1);
      FloodRable8Bit var10 = new FloodRable8Bit(var8, var9);
      handleColorInterpolationFilters(var10, var2);
      updateFilterMap(var2, var10, var7);
      return var10;
   }
}

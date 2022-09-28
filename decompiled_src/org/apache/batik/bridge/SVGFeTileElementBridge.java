package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.TileRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeTileElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feTile";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      Rectangle2D var9 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var6, var6, var1);
      Filter var10 = getIn(var2, var3, var4, var5, var7, var1);
      if (var10 == null) {
         return null;
      } else {
         TileRable8Bit var11 = new TileRable8Bit(var10, var9, var10.getBounds2D(), false);
         handleColorInterpolationFilters(var11, var2);
         updateFilterMap(var2, var11, var7);
         return var11;
      }
   }
}

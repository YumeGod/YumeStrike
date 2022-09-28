package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.batik.ext.awt.image.Light;
import org.apache.batik.ext.awt.image.renderable.DiffuseLightingRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeDiffuseLightingElementBridge extends AbstractSVGLightingElementBridge {
   public String getLocalName() {
      return "feDiffuseLighting";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      float var8 = convertNumber(var2, "surfaceScale", 1.0F, var1);
      float var9 = convertNumber(var2, "diffuseConstant", 1.0F, var1);
      Light var10 = extractLight(var2, var1);
      double[] var11 = convertKernelUnitLength(var2, var1);
      Filter var12 = getIn(var2, var3, var4, var5, var7, var1);
      if (var12 == null) {
         return null;
      } else {
         Rectangle2D var13 = var12.getBounds2D();
         Rectangle2D var14 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var13, var6, var1);
         DiffuseLightingRable8Bit var15 = new DiffuseLightingRable8Bit(var12, var14, var10, (double)var9, (double)var8, var11);
         handleColorInterpolationFilters(var15, var2);
         updateFilterMap(var2, var15, var7);
         return var15;
      }
   }
}

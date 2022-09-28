package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;
import org.apache.batik.ext.awt.image.ARGBChannel;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.DisplacementMapRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeDisplacementMapElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feDisplacementMap";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      float var8 = convertNumber(var2, "scale", 0.0F, var1);
      ARGBChannel var9 = convertChannelSelector(var2, "xChannelSelector", ARGBChannel.A, var1);
      ARGBChannel var10 = convertChannelSelector(var2, "yChannelSelector", ARGBChannel.A, var1);
      Filter var11 = getIn(var2, var3, var4, var5, var7, var1);
      if (var11 == null) {
         return null;
      } else {
         Filter var12 = getIn2(var2, var3, var4, var5, var7, var1);
         if (var12 == null) {
            return null;
         } else {
            Rectangle2D var13 = (Rectangle2D)var11.getBounds2D().clone();
            var13.add(var12.getBounds2D());
            Rectangle2D var14 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var13, var6, var1);
            PadRable8Bit var15 = new PadRable8Bit(var11, var14, PadMode.ZERO_PAD);
            ArrayList var16 = new ArrayList(2);
            var16.add(var15);
            var16.add(var12);
            DisplacementMapRable8Bit var17 = new DisplacementMapRable8Bit(var16, (double)var8, var9, var10);
            handleColorInterpolationFilters(var17, var2);
            PadRable8Bit var18 = new PadRable8Bit(var17, var14, PadMode.ZERO_PAD);
            updateFilterMap(var2, var18, var7);
            return var18;
         }
      }
   }

   protected static ARGBChannel convertChannelSelector(Element var0, String var1, ARGBChannel var2, BridgeContext var3) {
      String var4 = var0.getAttributeNS((String)null, var1);
      if (var4.length() == 0) {
         return var2;
      } else if ("A".equals(var4)) {
         return ARGBChannel.A;
      } else if ("R".equals(var4)) {
         return ARGBChannel.R;
      } else if ("G".equals(var4)) {
         return ARGBChannel.G;
      } else if ("B".equals(var4)) {
         return ARGBChannel.B;
      } else {
         throw new BridgeException(var3, var0, "attribute.malformed", new Object[]{var1, var4});
      }
   }
}

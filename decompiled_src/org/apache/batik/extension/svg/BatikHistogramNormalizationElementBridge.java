package org.apache.batik.extension.svg;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.batik.bridge.AbstractSVGFilterPrimitiveElementBridge;
import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.SVGUtilities;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class BatikHistogramNormalizationElementBridge extends AbstractSVGFilterPrimitiveElementBridge implements BatikExtConstants {
   public String getNamespaceURI() {
      return "http://xml.apache.org/batik/ext";
   }

   public String getLocalName() {
      return "histogramNormalization";
   }

   public Bridge getInstance() {
      return new BatikHistogramNormalizationElementBridge();
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      Filter var8 = getIn(var2, var3, var4, var5, var7, var1);
      if (var8 == null) {
         return null;
      } else {
         Filter var9 = (Filter)var7.get("SourceGraphic");
         Rectangle2D var10;
         if (var8 == var9) {
            var10 = var6;
         } else {
            var10 = var8.getBounds2D();
         }

         Rectangle2D var11 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var10, var6, var1);
         float var12 = 1.0F;
         String var13 = var2.getAttributeNS((String)null, "trim");
         if (var13.length() != 0) {
            try {
               var12 = SVGUtilities.convertSVGNumber(var13);
            } catch (NumberFormatException var15) {
               throw new BridgeException(var1, var2, var15, "attribute.malformed", new Object[]{"trim", var13});
            }
         }

         if (var12 < 0.0F) {
            var12 = 0.0F;
         } else if (var12 > 100.0F) {
            var12 = 100.0F;
         }

         BatikHistogramNormalizationFilter8Bit var14 = new BatikHistogramNormalizationFilter8Bit(var8, var12 / 100.0F);
         PadRable8Bit var16 = new PadRable8Bit(var14, var11, PadMode.ZERO_PAD);
         updateFilterMap(var2, var16, var7);
         handleColorInterpolationFilters(var16, var2);
         return var16;
      }
   }

   protected static int convertSides(Element var0, String var1, int var2, BridgeContext var3) {
      String var4 = var0.getAttributeNS((String)null, var1);
      if (var4.length() == 0) {
         return var2;
      } else {
         boolean var5 = false;

         int var8;
         try {
            var8 = SVGUtilities.convertSVGInteger(var4);
         } catch (NumberFormatException var7) {
            throw new BridgeException(var3, var0, var7, "attribute.malformed", new Object[]{var1, var4});
         }

         if (var8 < 3) {
            throw new BridgeException(var3, var0, "attribute.malformed", new Object[]{var1, var4});
         } else {
            return var8;
         }
      }
   }
}

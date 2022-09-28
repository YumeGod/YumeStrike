package org.apache.batik.bridge;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.FilterAlphaRable;
import org.apache.batik.ext.awt.image.renderable.FilterColorInterpolation;
import org.apache.batik.ext.awt.image.renderable.FloodRable8Bit;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.filter.BackgroundRable8Bit;
import org.w3c.dom.Element;

public abstract class AbstractSVGFilterPrimitiveElementBridge extends AnimatableGenericSVGBridge implements FilterPrimitiveBridge, ErrorConstants {
   static final Rectangle2D INFINITE_FILTER_REGION = new Rectangle2D.Float(-1.7014117E38F, -1.7014117E38F, Float.MAX_VALUE, Float.MAX_VALUE);

   protected AbstractSVGFilterPrimitiveElementBridge() {
   }

   protected static Filter getIn(Element var0, Element var1, GraphicsNode var2, Filter var3, Map var4, BridgeContext var5) {
      String var6 = var0.getAttributeNS((String)null, "in");
      return var6.length() == 0 ? var3 : getFilterSource(var0, var6, var1, var2, var4, var5);
   }

   protected static Filter getIn2(Element var0, Element var1, GraphicsNode var2, Filter var3, Map var4, BridgeContext var5) {
      String var6 = var0.getAttributeNS((String)null, "in2");
      if (var6.length() == 0) {
         throw new BridgeException(var5, var0, "attribute.missing", new Object[]{"in2"});
      } else {
         return getFilterSource(var0, var6, var1, var2, var4, var5);
      }
   }

   protected static void updateFilterMap(Element var0, Filter var1, Map var2) {
      String var3 = var0.getAttributeNS((String)null, "result");
      if (var3.length() != 0 && var3.trim().length() != 0) {
         var2.put(var3, var1);
      }

   }

   protected static void handleColorInterpolationFilters(Filter var0, Element var1) {
      if (var0 instanceof FilterColorInterpolation) {
         boolean var2 = CSSUtilities.convertColorInterpolationFilters(var1);
         ((FilterColorInterpolation)var0).setColorSpaceLinear(var2);
      }

   }

   static Filter getFilterSource(Element var0, String var1, Element var2, GraphicsNode var3, Map var4, BridgeContext var5) {
      Filter var6 = (Filter)var4.get("SourceGraphic");
      Rectangle2D var7 = var6.getBounds2D();
      int var8 = var1.length();
      Object var9 = null;
      switch (var8) {
         case 9:
            if ("FillPaint".equals(var1)) {
               Object var12 = PaintServer.convertFillPaint(var2, var3, var5);
               if (var12 == null) {
                  var12 = new Color(0, 0, 0, 0);
               }

               var9 = new FloodRable8Bit(var7, (Paint)var12);
            }
         case 10:
         case 12:
         case 14:
         default:
            break;
         case 11:
            if (var1.charAt(1) == "SourceAlpha".charAt(1)) {
               if ("SourceAlpha".equals(var1)) {
                  var9 = new FilterAlphaRable(var6);
               }
            } else if ("StrokePaint".equals(var1)) {
               Paint var10 = PaintServer.convertStrokePaint(var2, var3, var5);
               var9 = new FloodRable8Bit(var7, var10);
            }
            break;
         case 13:
            if ("SourceGraphic".equals(var1)) {
               var9 = var6;
            }
            break;
         case 15:
            BackgroundRable8Bit var11;
            if (var1.charAt(10) == "BackgroundImage".charAt(10)) {
               if ("BackgroundImage".equals(var1)) {
                  var11 = new BackgroundRable8Bit(var3);
                  var9 = new PadRable8Bit(var11, var7, PadMode.ZERO_PAD);
               }
            } else if ("BackgroundAlpha".equals(var1)) {
               var11 = new BackgroundRable8Bit(var3);
               FilterAlphaRable var13 = new FilterAlphaRable(var11);
               var9 = new PadRable8Bit(var13, var7, PadMode.ZERO_PAD);
            }
      }

      if (var9 == null) {
         var9 = (Filter)var4.get(var1);
      }

      return (Filter)var9;
   }

   protected static int convertInteger(Element var0, String var1, int var2, BridgeContext var3) {
      String var4 = var0.getAttributeNS((String)null, var1);
      if (var4.length() == 0) {
         return var2;
      } else {
         try {
            return SVGUtilities.convertSVGInteger(var4);
         } catch (NumberFormatException var6) {
            throw new BridgeException(var3, var0, var6, "attribute.malformed", new Object[]{var1, var4});
         }
      }
   }

   protected static float convertNumber(Element var0, String var1, float var2, BridgeContext var3) {
      String var4 = var0.getAttributeNS((String)null, var1);
      if (var4.length() == 0) {
         return var2;
      } else {
         try {
            return SVGUtilities.convertSVGNumber(var4);
         } catch (NumberFormatException var6) {
            throw new BridgeException(var3, var0, var6, "attribute.malformed", new Object[]{var1, var4, var6});
         }
      }
   }
}

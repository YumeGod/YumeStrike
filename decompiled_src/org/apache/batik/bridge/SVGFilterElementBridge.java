package org.apache.batik.bridge;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.FilterChainRable8Bit;
import org.apache.batik.ext.awt.image.renderable.FloodRable8Bit;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGFilterElementBridge extends AnimatableGenericSVGBridge implements FilterBridge, ErrorConstants {
   protected static final Color TRANSPARENT_BLACK = new Color(0, true);

   public String getLocalName() {
      return "filter";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4) {
      Rectangle2D var5 = SVGUtilities.convertFilterChainRegion(var2, var3, var4, var1);
      if (var5 == null) {
         return null;
      } else {
         Filter var6 = var4.getGraphicsNodeRable(true);
         PadRable8Bit var11 = new PadRable8Bit(var6, var5, PadMode.ZERO_PAD);
         FilterChainRable8Bit var7 = new FilterChainRable8Bit(var11, var5);
         float[] var8 = SVGUtilities.convertFilterRes(var2, var1);
         var7.setFilterResolutionX((int)var8[0]);
         var7.setFilterResolutionY((int)var8[1]);
         HashMap var9 = new HashMap(11);
         var9.put("SourceGraphic", var11);
         Filter var10 = buildFilterPrimitives(var2, var5, var3, var4, var11, var9, var1);
         if (var10 == null) {
            return null;
         } else {
            if (var10 == var11) {
               var10 = createEmptyFilter(var2, var5, var3, var4, var1);
            }

            var7.setSource(var10);
            return var7;
         }
      }
   }

   protected static Filter createEmptyFilter(Element var0, Rectangle2D var1, Element var2, GraphicsNode var3, BridgeContext var4) {
      Rectangle2D var5 = SVGUtilities.convertFilterPrimitiveRegion((Element)null, var0, var2, var3, var1, var1, var4);
      return new FloodRable8Bit(var5, TRANSPARENT_BLACK);
   }

   protected static Filter buildFilterPrimitives(Element var0, Rectangle2D var1, Element var2, GraphicsNode var3, Filter var4, Map var5, BridgeContext var6) {
      LinkedList var7 = new LinkedList();

      while(true) {
         Filter var8 = buildLocalFilterPrimitives(var0, var1, var2, var3, var4, var5, var6);
         if (var8 != var4) {
            return var8;
         }

         String var9 = XLinkSupport.getXLinkHref(var0);
         if (var9.length() == 0) {
            return var4;
         }

         SVGOMDocument var10 = (SVGOMDocument)var0.getOwnerDocument();
         ParsedURL var11 = new ParsedURL(var10.getURLObject(), var9);
         if (var7.contains(var11)) {
            throw new BridgeException(var6, var0, "xlink.href.circularDependencies", new Object[]{var9});
         }

         var7.add(var11);
         var0 = var6.getReferencedElement(var0, var9);
      }
   }

   protected static Filter buildLocalFilterPrimitives(Element var0, Rectangle2D var1, Element var2, GraphicsNode var3, Filter var4, Map var5, BridgeContext var6) {
      for(Node var7 = var0.getFirstChild(); var7 != null; var7 = var7.getNextSibling()) {
         if (var7.getNodeType() == 1) {
            Element var8 = (Element)var7;
            Bridge var9 = var6.getBridge(var8);
            if (var9 != null && var9 instanceof FilterPrimitiveBridge) {
               FilterPrimitiveBridge var10 = (FilterPrimitiveBridge)var9;
               Filter var11 = var10.createFilter(var6, var8, var2, var3, var4, var1, var5);
               if (var11 == null) {
                  return null;
               }

               var4 = var11;
            }
         }
      }

      return var4;
   }
}

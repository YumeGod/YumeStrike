package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.batik.ext.awt.image.CompositeRule;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.CompositeRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGFeMergeElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feMerge";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      List var8 = extractFeMergeNode(var2, var3, var4, var5, var7, var1);
      if (var8 == null) {
         return null;
      } else if (var8.size() == 0) {
         return null;
      } else {
         Iterator var9 = var8.iterator();
         Rectangle2D var10 = (Rectangle2D)((Filter)var9.next()).getBounds2D().clone();

         while(var9.hasNext()) {
            var10.add(((Filter)var9.next()).getBounds2D());
         }

         Rectangle2D var11 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var10, var6, var1);
         CompositeRable8Bit var12 = new CompositeRable8Bit(var8, CompositeRule.OVER, true);
         handleColorInterpolationFilters(var12, var2);
         PadRable8Bit var13 = new PadRable8Bit(var12, var11, PadMode.ZERO_PAD);
         updateFilterMap(var2, var13, var7);
         return var13;
      }
   }

   protected static List extractFeMergeNode(Element var0, Element var1, GraphicsNode var2, Filter var3, Map var4, BridgeContext var5) {
      LinkedList var6 = null;

      for(Node var7 = var0.getFirstChild(); var7 != null; var7 = var7.getNextSibling()) {
         if (var7.getNodeType() == 1) {
            Element var8 = (Element)var7;
            Bridge var9 = var5.getBridge(var8);
            if (var9 != null && var9 instanceof SVGFeMergeNodeElementBridge) {
               Filter var10 = ((SVGFeMergeNodeElementBridge)var9).createFilter(var5, var8, var1, var2, var3, var4);
               if (var10 != null) {
                  if (var6 == null) {
                     var6 = new LinkedList();
                  }

                  var6.add(var10);
               }
            }
         }
      }

      return var6;
   }

   public static class SVGFeMergeNodeElementBridge extends AnimatableGenericSVGBridge {
      public String getLocalName() {
         return "feMergeNode";
      }

      public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Map var6) {
         return AbstractSVGFilterPrimitiveElementBridge.getIn(var2, var3, var4, var5, var6, var1);
      }
   }
}

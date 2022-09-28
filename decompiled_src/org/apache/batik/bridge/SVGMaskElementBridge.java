package org.apache.batik.bridge;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.filter.Mask;
import org.apache.batik.gvt.filter.MaskRable8Bit;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGMaskElementBridge extends AnimatableGenericSVGBridge implements MaskBridge {
   public String getLocalName() {
      return "mask";
   }

   public Mask createMask(BridgeContext var1, Element var2, Element var3, GraphicsNode var4) {
      Rectangle2D var6 = SVGUtilities.convertMaskRegion(var2, var3, var4, var1);
      GVTBuilder var7 = var1.getGVTBuilder();
      CompositeGraphicsNode var8 = new CompositeGraphicsNode();
      CompositeGraphicsNode var9 = new CompositeGraphicsNode();
      var8.getChildren().add(var9);
      boolean var10 = false;

      for(Node var11 = var2.getFirstChild(); var11 != null; var11 = var11.getNextSibling()) {
         if (var11.getNodeType() == 1) {
            Element var12 = (Element)var11;
            GraphicsNode var13 = var7.build(var1, var12);
            if (var13 != null) {
               var10 = true;
               var9.getChildren().add(var13);
            }
         }
      }

      if (!var10) {
         return null;
      } else {
         String var5 = var2.getAttributeNS((String)null, "transform");
         AffineTransform var14;
         if (var5.length() != 0) {
            var14 = SVGUtilities.convertTransform(var2, "transform", var5, var1);
         } else {
            var14 = new AffineTransform();
         }

         var5 = var2.getAttributeNS((String)null, "maskContentUnits");
         short var15;
         if (var5.length() == 0) {
            var15 = 1;
         } else {
            var15 = SVGUtilities.parseCoordinateSystem(var2, "maskContentUnits", var5, var1);
         }

         if (var15 == 2) {
            var14 = SVGUtilities.toObjectBBox(var14, var4);
         }

         var9.setTransform(var14);
         Filter var16 = var4.getFilter();
         if (var16 == null) {
            var16 = var4.getGraphicsNodeRable(true);
         }

         return new MaskRable8Bit(var16, var8, var6);
      }
   }
}

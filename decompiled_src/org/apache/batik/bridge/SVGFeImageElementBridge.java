package org.apache.batik.bridge;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.AffineRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGFeImageElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feImage";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      String var8 = XLinkSupport.getXLinkHref(var2);
      if (var8.length() == 0) {
         throw new BridgeException(var1, var2, "attribute.missing", new Object[]{"xlink:href"});
      } else {
         Document var9 = var2.getOwnerDocument();
         boolean var10 = var8.indexOf(35) != -1;
         Element var11 = null;
         if (var10) {
            var11 = var9.createElementNS("http://www.w3.org/2000/svg", "use");
         } else {
            var11 = var9.createElementNS("http://www.w3.org/2000/svg", "image");
         }

         var11.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", var8);
         Element var12 = var9.createElementNS("http://www.w3.org/2000/svg", "g");
         var12.appendChild(var11);
         Element var14 = (Element)var2.getParentNode();
         Rectangle2D var15 = SVGUtilities.getBaseFilterPrimitiveRegion(var2, var3, var4, var6, var1);
         var11.setAttributeNS((String)null, "x", String.valueOf(var15.getX()));
         var11.setAttributeNS((String)null, "y", String.valueOf(var15.getY()));
         var11.setAttributeNS((String)null, "width", String.valueOf(var15.getWidth()));
         var11.setAttributeNS((String)null, "height", String.valueOf(var15.getHeight()));
         GraphicsNode var16 = var1.getGVTBuilder().build(var1, var12);
         Filter var17 = var16.getGraphicsNodeRable(true);
         String var19 = SVGUtilities.getChainableAttributeNS(var14, (String)null, "primitiveUnits", var1);
         short var18;
         if (var19.length() == 0) {
            var18 = 1;
         } else {
            var18 = SVGUtilities.parseCoordinateSystem(var14, "primitiveUnits", var19, var1);
         }

         AffineTransform var20 = new AffineTransform();
         if (var18 == 2) {
            var20 = SVGUtilities.toObjectBBox(var20, var4);
         }

         AffineRable8Bit var22 = new AffineRable8Bit(var17, var20);
         handleColorInterpolationFilters(var22, var2);
         Rectangle2D var21 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var6, var6, var1);
         PadRable8Bit var23 = new PadRable8Bit(var22, var21, PadMode.ZERO_PAD);
         updateFilterMap(var2, var23, var7);
         return var23;
      }
   }

   protected static Filter createSVGFeImage(BridgeContext var0, Rectangle2D var1, Element var2, boolean var3, Element var4, GraphicsNode var5) {
      GraphicsNode var6 = var0.getGVTBuilder().build(var0, var2);
      Filter var7 = var6.getGraphicsNodeRable(true);
      AffineTransform var8 = new AffineTransform();
      if (var3) {
         Element var10 = (Element)var4.getParentNode();
         String var11 = SVGUtilities.getChainableAttributeNS(var10, (String)null, "primitiveUnits", var0);
         short var9;
         if (var11.length() == 0) {
            var9 = 1;
         } else {
            var9 = SVGUtilities.parseCoordinateSystem(var10, "primitiveUnits", var11, var0);
         }

         if (var9 == 2) {
            var8 = SVGUtilities.toObjectBBox(var8, var5);
         }

         Rectangle2D var12 = var5.getGeometryBounds();
         var8.preConcatenate(AffineTransform.getTranslateInstance(var1.getX() - var12.getX(), var1.getY() - var12.getY()));
      } else {
         var8.translate(var1.getX(), var1.getY());
      }

      return new AffineRable8Bit(var7, var8);
   }

   protected static Filter createRasterFeImage(BridgeContext var0, Rectangle2D var1, ParsedURL var2) {
      Filter var3 = ImageTagRegistry.getRegistry().readURL(var2);
      Rectangle2D var4 = var3.getBounds2D();
      AffineTransform var5 = new AffineTransform();
      var5.translate(var1.getX(), var1.getY());
      var5.scale(var1.getWidth() / (var4.getWidth() - 1.0), var1.getHeight() / (var4.getHeight() - 1.0));
      var5.translate(-var4.getX(), -var4.getY());
      return new AffineRable8Bit(var3, var5);
   }
}

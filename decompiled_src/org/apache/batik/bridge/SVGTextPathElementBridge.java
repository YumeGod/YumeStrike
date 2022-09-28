package org.apache.batik.bridge;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.gvt.text.TextPath;
import org.apache.batik.parser.AWTPathProducer;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathParser;
import org.w3c.dom.Element;

public class SVGTextPathElementBridge extends AnimatableGenericSVGBridge implements ErrorConstants {
   public String getLocalName() {
      return "textPath";
   }

   public void handleElement(BridgeContext var1, Element var2) {
   }

   public TextPath createTextPath(BridgeContext var1, Element var2) {
      String var3 = XLinkSupport.getXLinkHref(var2);
      Element var4 = var1.getReferencedElement(var2, var3);
      if (var4 != null && "http://www.w3.org/2000/svg".equals(var4.getNamespaceURI()) && var4.getLocalName().equals("path")) {
         String var5 = var4.getAttributeNS((String)null, "d");
         Shape var6 = null;
         if (var5.length() != 0) {
            AWTPathProducer var7 = new AWTPathProducer();
            var7.setWindingRule(CSSUtilities.convertFillRule(var4));

            try {
               PathParser var8 = new PathParser();
               var8.setPathHandler(var7);
               var8.parse(var5);
            } catch (ParseException var18) {
               throw new BridgeException(var1, var4, var18, "attribute.malformed", new Object[]{"d"});
            } finally {
               var6 = var7.getShape();
            }

            var5 = var4.getAttributeNS((String)null, "transform");
            if (var5.length() != 0) {
               AffineTransform var20 = SVGUtilities.convertTransform(var4, "transform", var5, var1);
               var6 = var20.createTransformedShape(var6);
            }

            TextPath var21 = new TextPath(new GeneralPath(var6));
            var5 = var2.getAttributeNS((String)null, "startOffset");
            if (var5.length() > 0) {
               float var22 = 0.0F;
               int var9 = var5.indexOf(37);
               if (var9 != -1) {
                  float var10 = var21.lengthOfPath();
                  String var11 = var5.substring(0, var9);
                  float var12 = 0.0F;

                  try {
                     var12 = SVGUtilities.convertSVGNumber(var11);
                  } catch (NumberFormatException var17) {
                     var12 = -1.0F;
                  }

                  if (var12 < 0.0F) {
                     throw new BridgeException(var1, var2, "attribute.malformed", new Object[]{"startOffset", var5});
                  }

                  var22 = (float)((double)(var12 * var10) / 100.0);
               } else {
                  org.apache.batik.parser.UnitProcessor.Context var23 = UnitProcessor.createContext(var1, var2);
                  var22 = UnitProcessor.svgOtherLengthToUserSpace(var5, "startOffset", var23);
               }

               var21.setStartOffset(var22);
            }

            return var21;
         } else {
            throw new BridgeException(var1, var4, "attribute.missing", new Object[]{"d"});
         }
      } else {
         throw new BridgeException(var1, var2, "uri.badTarget", new Object[]{var3});
      }
   }
}

package org.apache.batik.extension.svg;

import java.awt.geom.GeneralPath;
import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.SVGDecoratedShapeElementBridge;
import org.apache.batik.bridge.SVGUtilities;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.parser.UnitProcessor;
import org.w3c.dom.Element;

public class BatikStarElementBridge extends SVGDecoratedShapeElementBridge implements BatikExtConstants {
   public String getNamespaceURI() {
      return "http://xml.apache.org/batik/ext";
   }

   public String getLocalName() {
      return "star";
   }

   public Bridge getInstance() {
      return new BatikStarElementBridge();
   }

   protected void buildShape(BridgeContext var1, Element var2, ShapeNode var3) {
      UnitProcessor.Context var4 = org.apache.batik.bridge.UnitProcessor.createContext(var1, var2);
      String var5 = var2.getAttributeNS((String)null, "cx");
      float var6 = 0.0F;
      if (var5.length() != 0) {
         var6 = org.apache.batik.bridge.UnitProcessor.svgHorizontalCoordinateToUserSpace(var5, "cx", var4);
      }

      var5 = var2.getAttributeNS((String)null, "cy");
      float var7 = 0.0F;
      if (var5.length() != 0) {
         var7 = org.apache.batik.bridge.UnitProcessor.svgVerticalCoordinateToUserSpace(var5, "cy", var4);
      }

      var5 = var2.getAttributeNS((String)null, "r");
      if (var5.length() == 0) {
         throw new BridgeException(var1, var2, "attribute.missing", new Object[]{"r", var5});
      } else {
         float var8 = org.apache.batik.bridge.UnitProcessor.svgOtherLengthToUserSpace(var5, "r", var4);
         var5 = var2.getAttributeNS((String)null, "ir");
         if (var5.length() == 0) {
            throw new BridgeException(var1, var2, "attribute.missing", new Object[]{"ir", var5});
         } else {
            float var9 = org.apache.batik.bridge.UnitProcessor.svgOtherLengthToUserSpace(var5, "ir", var4);
            int var10 = convertSides(var2, "sides", 3, var1);
            GeneralPath var11 = new GeneralPath();
            double var18 = 6.283185307179586 / (double)var10;

            for(int var22 = 0; var22 < var10; ++var22) {
               double var12 = (double)var22 * var18 - 1.5707963267948966;
               double var14 = (double)var6 + (double)var9 * Math.cos(var12);
               double var16 = (double)var7 - (double)var9 * Math.sin(var12);
               if (var22 == 0) {
                  var11.moveTo((float)var14, (float)var16);
               } else {
                  var11.lineTo((float)var14, (float)var16);
               }

               var12 = ((double)var22 + 0.5) * var18 - 1.5707963267948966;
               var14 = (double)var6 + (double)var8 * Math.cos(var12);
               var16 = (double)var7 - (double)var8 * Math.sin(var12);
               var11.lineTo((float)var14, (float)var16);
            }

            var11.closePath();
            var3.setShape(var11);
         }
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

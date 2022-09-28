package org.apache.batik.bridge;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.ext.awt.MultipleGradientPaint;
import org.apache.batik.ext.awt.RadialGradientPaint;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGRadialGradientElementBridge extends AbstractSVGGradientElementBridge {
   public String getLocalName() {
      return "radialGradient";
   }

   protected Paint buildGradient(Element var1, Element var2, GraphicsNode var3, MultipleGradientPaint.CycleMethodEnum var4, MultipleGradientPaint.ColorSpaceEnum var5, AffineTransform var6, Color[] var7, float[] var8, BridgeContext var9) {
      String var10 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "cx", var9);
      if (var10.length() == 0) {
         var10 = "50%";
      }

      String var11 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "cy", var9);
      if (var11.length() == 0) {
         var11 = "50%";
      }

      String var12 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "r", var9);
      if (var12.length() == 0) {
         var12 = "50%";
      }

      String var13 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "fx", var9);
      if (var13.length() == 0) {
         var13 = var10;
      }

      String var14 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "fy", var9);
      if (var14.length() == 0) {
         var14 = var11;
      }

      String var16 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "gradientUnits", var9);
      short var15;
      if (var16.length() == 0) {
         var15 = 2;
      } else {
         var15 = SVGUtilities.parseCoordinateSystem(var1, "gradientUnits", var16, var9);
      }

      SVGContext var17 = BridgeContext.getSVGContext(var2);
      if (var15 == 2 && var17 instanceof AbstractGraphicsNodeBridge) {
         Rectangle2D var18 = ((AbstractGraphicsNodeBridge)var17).getBBox();
         if (var18 != null && var18.getWidth() == 0.0 || var18.getHeight() == 0.0) {
            return null;
         }
      }

      if (var15 == 2) {
         var6 = SVGUtilities.toObjectBBox(var6, var3);
      }

      org.apache.batik.parser.UnitProcessor.Context var22 = UnitProcessor.createContext(var9, var1);
      float var19 = SVGUtilities.convertLength(var12, "r", var15, var22);
      if (var19 == 0.0F) {
         return var7[var7.length - 1];
      } else {
         Point2D var20 = SVGUtilities.convertPoint(var10, "cx", var11, "cy", var15, var22);
         Point2D var21 = SVGUtilities.convertPoint(var13, "fx", var14, "fy", var15, var22);
         return new RadialGradientPaint(var20, var19, var21, var8, var7, var4, RadialGradientPaint.SRGB, var6);
      }
   }
}

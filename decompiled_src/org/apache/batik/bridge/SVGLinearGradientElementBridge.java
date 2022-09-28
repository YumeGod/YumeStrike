package org.apache.batik.bridge;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.ext.awt.LinearGradientPaint;
import org.apache.batik.ext.awt.MultipleGradientPaint;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGLinearGradientElementBridge extends AbstractSVGGradientElementBridge {
   public String getLocalName() {
      return "linearGradient";
   }

   protected Paint buildGradient(Element var1, Element var2, GraphicsNode var3, MultipleGradientPaint.CycleMethodEnum var4, MultipleGradientPaint.ColorSpaceEnum var5, AffineTransform var6, Color[] var7, float[] var8, BridgeContext var9) {
      String var10 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "x1", var9);
      if (var10.length() == 0) {
         var10 = "0%";
      }

      String var11 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "y1", var9);
      if (var11.length() == 0) {
         var11 = "0%";
      }

      String var12 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "x2", var9);
      if (var12.length() == 0) {
         var12 = "100%";
      }

      String var13 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "y2", var9);
      if (var13.length() == 0) {
         var13 = "0%";
      }

      String var15 = SVGUtilities.getChainableAttributeNS(var1, (String)null, "gradientUnits", var9);
      short var14;
      if (var15.length() == 0) {
         var14 = 2;
      } else {
         var14 = SVGUtilities.parseCoordinateSystem(var1, "gradientUnits", var15, var9);
      }

      SVGContext var16 = BridgeContext.getSVGContext(var2);
      if (var14 == 2 && var16 instanceof AbstractGraphicsNodeBridge) {
         Rectangle2D var17 = ((AbstractGraphicsNodeBridge)var16).getBBox();
         if (var17 != null && var17.getWidth() == 0.0 || var17.getHeight() == 0.0) {
            return null;
         }
      }

      if (var14 == 2) {
         var6 = SVGUtilities.toObjectBBox(var6, var3);
      }

      org.apache.batik.parser.UnitProcessor.Context var20 = UnitProcessor.createContext(var9, var1);
      Point2D var18 = SVGUtilities.convertPoint(var10, "x1", var11, "y1", var14, var20);
      Point2D var19 = SVGUtilities.convertPoint(var12, "x2", var13, "y2", var14, var20);
      if (var18.getX() == var19.getX() && var18.getY() == var19.getY()) {
         return var7[var7.length - 1];
      } else {
         return new LinearGradientPaint(var18, var19, var8, var7, var4, var5, var6);
      }
   }
}

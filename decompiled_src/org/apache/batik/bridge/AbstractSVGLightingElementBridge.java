package org.apache.batik.bridge;

import java.awt.Color;
import java.util.StringTokenizer;
import org.apache.batik.ext.awt.image.DistantLight;
import org.apache.batik.ext.awt.image.Light;
import org.apache.batik.ext.awt.image.PointLight;
import org.apache.batik.ext.awt.image.SpotLight;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class AbstractSVGLightingElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   protected AbstractSVGLightingElementBridge() {
   }

   protected static Light extractLight(Element var0, BridgeContext var1) {
      Color var2 = CSSUtilities.convertLightingColor(var0, var1);

      for(Node var3 = var0.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3.getNodeType() == 1) {
            Element var4 = (Element)var3;
            Bridge var5 = var1.getBridge(var4);
            if (var5 != null && var5 instanceof AbstractSVGLightElementBridge) {
               return ((AbstractSVGLightElementBridge)var5).createLight(var1, var0, var4, var2);
            }
         }
      }

      return null;
   }

   protected static double[] convertKernelUnitLength(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "kernelUnitLength");
      if (var2.length() == 0) {
         return null;
      } else {
         double[] var3 = new double[2];
         StringTokenizer var4 = new StringTokenizer(var2, " ,");

         try {
            var3[0] = (double)SVGUtilities.convertSVGNumber(var4.nextToken());
            if (var4.hasMoreTokens()) {
               var3[1] = (double)SVGUtilities.convertSVGNumber(var4.nextToken());
            } else {
               var3[1] = var3[0];
            }
         } catch (NumberFormatException var6) {
            throw new BridgeException(var1, var0, var6, "attribute.malformed", new Object[]{"kernelUnitLength", var2});
         }

         if (!var4.hasMoreTokens() && !(var3[0] <= 0.0) && !(var3[1] <= 0.0)) {
            return var3;
         } else {
            throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"kernelUnitLength", var2});
         }
      }
   }

   public static class SVGFePointLightElementBridge extends AbstractSVGLightElementBridge {
      public String getLocalName() {
         return "fePointLight";
      }

      public Light createLight(BridgeContext var1, Element var2, Element var3, Color var4) {
         double var5 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "x", 0.0F, var1);
         double var7 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "y", 0.0F, var1);
         double var9 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "z", 0.0F, var1);
         return new PointLight(var5, var7, var9, var4);
      }
   }

   public static class SVGFeDistantLightElementBridge extends AbstractSVGLightElementBridge {
      public String getLocalName() {
         return "feDistantLight";
      }

      public Light createLight(BridgeContext var1, Element var2, Element var3, Color var4) {
         double var5 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "azimuth", 0.0F, var1);
         double var7 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "elevation", 0.0F, var1);
         return new DistantLight(var5, var7, var4);
      }
   }

   public static class SVGFeSpotLightElementBridge extends AbstractSVGLightElementBridge {
      public String getLocalName() {
         return "feSpotLight";
      }

      public Light createLight(BridgeContext var1, Element var2, Element var3, Color var4) {
         double var5 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "x", 0.0F, var1);
         double var7 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "y", 0.0F, var1);
         double var9 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "z", 0.0F, var1);
         double var11 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "pointsAtX", 0.0F, var1);
         double var13 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "pointsAtY", 0.0F, var1);
         double var15 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "pointsAtZ", 0.0F, var1);
         double var17 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "specularExponent", 1.0F, var1);
         double var19 = (double)AbstractSVGFilterPrimitiveElementBridge.convertNumber(var3, "limitingConeAngle", 90.0F, var1);
         return new SpotLight(var5, var7, var9, var11, var13, var15, var17, var19, var4);
      }
   }

   protected abstract static class AbstractSVGLightElementBridge extends AnimatableGenericSVGBridge {
      public abstract Light createLight(BridgeContext var1, Element var2, Element var3, Color var4);
   }
}

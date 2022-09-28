package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.batik.ext.awt.image.ComponentTransferFunction;
import org.apache.batik.ext.awt.image.ConcreteComponentTransferFunction;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.ComponentTransferRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGFeComponentTransferElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feComponentTransfer";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      Filter var8 = getIn(var2, var3, var4, var5, var7, var1);
      if (var8 == null) {
         return null;
      } else {
         Rectangle2D var9 = var8.getBounds2D();
         Rectangle2D var10 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var9, var6, var1);
         ComponentTransferFunction var11 = null;
         ComponentTransferFunction var12 = null;
         ComponentTransferFunction var13 = null;
         ComponentTransferFunction var14 = null;

         for(Node var15 = var2.getFirstChild(); var15 != null; var15 = var15.getNextSibling()) {
            if (var15.getNodeType() == 1) {
               Element var16 = (Element)var15;
               Bridge var17 = var1.getBridge(var16);
               if (var17 != null && var17 instanceof SVGFeFuncElementBridge) {
                  SVGFeFuncElementBridge var18 = (SVGFeFuncElementBridge)var17;
                  ComponentTransferFunction var19 = var18.createComponentTransferFunction(var2, var16);
                  if (var18 instanceof SVGFeFuncRElementBridge) {
                     var11 = var19;
                  } else if (var18 instanceof SVGFeFuncGElementBridge) {
                     var12 = var19;
                  } else if (var18 instanceof SVGFeFuncBElementBridge) {
                     var13 = var19;
                  } else if (var18 instanceof SVGFeFuncAElementBridge) {
                     var14 = var19;
                  }
               }
            }
         }

         ComponentTransferRable8Bit var20 = new ComponentTransferRable8Bit(var8, var14, var11, var12, var13);
         handleColorInterpolationFilters(var20, var2);
         PadRable8Bit var21 = new PadRable8Bit(var20, var10, PadMode.ZERO_PAD);
         updateFilterMap(var2, var21, var7);
         return var21;
      }
   }

   protected abstract static class SVGFeFuncElementBridge extends AnimatableGenericSVGBridge {
      public ComponentTransferFunction createComponentTransferFunction(Element var1, Element var2) {
         int var3 = convertType(var2, this.ctx);
         float var4;
         float var5;
         float[] var7;
         switch (var3) {
            case 0:
               return ConcreteComponentTransferFunction.getIdentityTransfer();
            case 1:
               var7 = convertTableValues(var2, this.ctx);
               if (var7 == null) {
                  return ConcreteComponentTransferFunction.getIdentityTransfer();
               }

               return ConcreteComponentTransferFunction.getTableTransfer(var7);
            case 2:
               var7 = convertTableValues(var2, this.ctx);
               if (var7 == null) {
                  return ConcreteComponentTransferFunction.getIdentityTransfer();
               }

               return ConcreteComponentTransferFunction.getDiscreteTransfer(var7);
            case 3:
               var4 = AbstractSVGFilterPrimitiveElementBridge.convertNumber(var2, "slope", 1.0F, this.ctx);
               var5 = AbstractSVGFilterPrimitiveElementBridge.convertNumber(var2, "intercept", 0.0F, this.ctx);
               return ConcreteComponentTransferFunction.getLinearTransfer(var4, var5);
            case 4:
               var4 = AbstractSVGFilterPrimitiveElementBridge.convertNumber(var2, "amplitude", 1.0F, this.ctx);
               var5 = AbstractSVGFilterPrimitiveElementBridge.convertNumber(var2, "exponent", 1.0F, this.ctx);
               float var6 = AbstractSVGFilterPrimitiveElementBridge.convertNumber(var2, "offset", 0.0F, this.ctx);
               return ConcreteComponentTransferFunction.getGammaTransfer(var4, var5, var6);
            default:
               throw new Error("invalid convertType:" + var3);
         }
      }

      protected static float[] convertTableValues(Element var0, BridgeContext var1) {
         String var2 = var0.getAttributeNS((String)null, "tableValues");
         if (var2.length() == 0) {
            return null;
         } else {
            StringTokenizer var3 = new StringTokenizer(var2, " ,");
            float[] var4 = new float[var3.countTokens()];

            try {
               for(int var5 = 0; var3.hasMoreTokens(); ++var5) {
                  var4[var5] = SVGUtilities.convertSVGNumber(var3.nextToken());
               }

               return var4;
            } catch (NumberFormatException var6) {
               throw new BridgeException(var1, var0, var6, "attribute.malformed", new Object[]{"tableValues", var2});
            }
         }
      }

      protected static int convertType(Element var0, BridgeContext var1) {
         String var2 = var0.getAttributeNS((String)null, "type");
         if (var2.length() == 0) {
            throw new BridgeException(var1, var0, "attribute.missing", new Object[]{"type"});
         } else if ("discrete".equals(var2)) {
            return 2;
         } else if ("identity".equals(var2)) {
            return 0;
         } else if ("gamma".equals(var2)) {
            return 4;
         } else if ("linear".equals(var2)) {
            return 3;
         } else if ("table".equals(var2)) {
            return 1;
         } else {
            throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"type", var2});
         }
      }
   }

   public static class SVGFeFuncBElementBridge extends SVGFeFuncElementBridge {
      public String getLocalName() {
         return "feFuncB";
      }
   }

   public static class SVGFeFuncGElementBridge extends SVGFeFuncElementBridge {
      public String getLocalName() {
         return "feFuncG";
      }
   }

   public static class SVGFeFuncRElementBridge extends SVGFeFuncElementBridge {
      public String getLocalName() {
         return "feFuncR";
      }
   }

   public static class SVGFeFuncAElementBridge extends SVGFeFuncElementBridge {
      public String getLocalName() {
         return "feFuncA";
      }
   }
}

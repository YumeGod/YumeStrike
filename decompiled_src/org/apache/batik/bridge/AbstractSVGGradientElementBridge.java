package org.apache.batik.bridge;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.ext.awt.MultipleGradientPaint;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class AbstractSVGGradientElementBridge extends AnimatableGenericSVGBridge implements PaintBridge, ErrorConstants {
   protected AbstractSVGGradientElementBridge() {
   }

   public Paint createPaint(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, float var5) {
      List var7 = extractStop(var2, var5, var1);
      if (var7 == null) {
         return null;
      } else {
         int var8 = var7.size();
         if (var8 == 1) {
            return ((Stop)var7.get(0)).color;
         } else {
            float[] var9 = new float[var8];
            Color[] var10 = new Color[var8];
            Iterator var11 = var7.iterator();

            for(int var12 = 0; var11.hasNext(); ++var12) {
               Stop var13 = (Stop)var11.next();
               var9[var12] = var13.offset;
               var10[var12] = var13.color;
            }

            MultipleGradientPaint.CycleMethodEnum var16 = MultipleGradientPaint.NO_CYCLE;
            String var6 = SVGUtilities.getChainableAttributeNS(var2, (String)null, "spreadMethod", var1);
            if (var6.length() != 0) {
               var16 = convertSpreadMethod(var2, var6, var1);
            }

            MultipleGradientPaint.ColorSpaceEnum var17 = CSSUtilities.convertColorInterpolation(var2);
            var6 = SVGUtilities.getChainableAttributeNS(var2, (String)null, "gradientTransform", var1);
            AffineTransform var14;
            if (var6.length() != 0) {
               var14 = SVGUtilities.convertTransform(var2, "gradientTransform", var6, var1);
            } else {
               var14 = new AffineTransform();
            }

            Paint var15 = this.buildGradient(var2, var3, var4, var16, var17, var14, var10, var9, var1);
            return var15;
         }
      }
   }

   protected abstract Paint buildGradient(Element var1, Element var2, GraphicsNode var3, MultipleGradientPaint.CycleMethodEnum var4, MultipleGradientPaint.ColorSpaceEnum var5, AffineTransform var6, Color[] var7, float[] var8, BridgeContext var9);

   protected static MultipleGradientPaint.CycleMethodEnum convertSpreadMethod(Element var0, String var1, BridgeContext var2) {
      if ("repeat".equals(var1)) {
         return MultipleGradientPaint.REPEAT;
      } else if ("reflect".equals(var1)) {
         return MultipleGradientPaint.REFLECT;
      } else if ("pad".equals(var1)) {
         return MultipleGradientPaint.NO_CYCLE;
      } else {
         throw new BridgeException(var2, var0, "attribute.malformed", new Object[]{"spreadMethod", var1});
      }
   }

   protected static List extractStop(Element var0, float var1, BridgeContext var2) {
      LinkedList var3 = new LinkedList();

      while(true) {
         List var4 = extractLocalStop(var0, var1, var2);
         if (var4 != null) {
            return var4;
         }

         String var5 = XLinkSupport.getXLinkHref(var0);
         if (var5.length() == 0) {
            return null;
         }

         String var6 = ((AbstractNode)var0).getBaseURI();
         ParsedURL var7 = new ParsedURL(var6, var5);
         if (contains(var3, var7)) {
            throw new BridgeException(var2, var0, "xlink.href.circularDependencies", new Object[]{var5});
         }

         var3.add(var7);
         var0 = var2.getReferencedElement(var0, var5);
      }
   }

   protected static List extractLocalStop(Element var0, float var1, BridgeContext var2) {
      LinkedList var3 = null;
      Stop var4 = null;

      for(Node var5 = var0.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
         if (var5.getNodeType() == 1) {
            Element var6 = (Element)var5;
            Bridge var7 = var2.getBridge(var6);
            if (var7 != null && var7 instanceof SVGStopElementBridge) {
               Stop var8 = ((SVGStopElementBridge)var7).createStop(var2, var0, var6, var1);
               if (var3 == null) {
                  var3 = new LinkedList();
               }

               if (var4 != null && var8.offset < var4.offset) {
                  var8.offset = var4.offset;
               }

               var3.add(var8);
               var4 = var8;
            }
         }
      }

      return var3;
   }

   private static boolean contains(List var0, ParsedURL var1) {
      Iterator var2 = var0.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(!var1.equals(var2.next()));

      return true;
   }

   public static class SVGStopElementBridge extends AnimatableGenericSVGBridge implements Bridge {
      public String getLocalName() {
         return "stop";
      }

      public Stop createStop(BridgeContext var1, Element var2, Element var3, float var4) {
         String var5 = var3.getAttributeNS((String)null, "offset");
         if (var5.length() == 0) {
            throw new BridgeException(var1, var3, "attribute.missing", new Object[]{"offset"});
         } else {
            float var6;
            try {
               var6 = SVGUtilities.convertRatio(var5);
            } catch (NumberFormatException var8) {
               throw new BridgeException(var1, var3, var8, "attribute.malformed", new Object[]{"offset", var5, var8});
            }

            Color var7 = CSSUtilities.convertStopColor(var3, var4, var1);
            return new Stop(var7, var6);
         }
      }
   }

   public static class Stop {
      public Color color;
      public float offset;

      public Stop(Color var1, float var2) {
         this.color = var1;
         this.offset = var2;
      }
   }
}

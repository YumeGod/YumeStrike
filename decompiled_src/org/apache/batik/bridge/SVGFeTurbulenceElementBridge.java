package org.apache.batik.bridge;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.TurbulenceRable8Bit;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public class SVGFeTurbulenceElementBridge extends AbstractSVGFilterPrimitiveElementBridge {
   public String getLocalName() {
      return "feTurbulence";
   }

   public Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, Filter var5, Rectangle2D var6, Map var7) {
      Filter var8 = getIn(var2, var3, var4, var5, var7, var1);
      if (var8 == null) {
         return null;
      } else {
         Rectangle2D var10 = SVGUtilities.convertFilterPrimitiveRegion(var2, var3, var4, var6, var6, var1);
         float[] var11 = convertBaseFrenquency(var2, var1);
         int var12 = convertInteger(var2, "numOctaves", 1, var1);
         int var13 = convertInteger(var2, "seed", 0, var1);
         boolean var14 = convertStitchTiles(var2, var1);
         boolean var15 = convertType(var2, var1);
         TurbulenceRable8Bit var16 = new TurbulenceRable8Bit(var10);
         var16.setBaseFrequencyX((double)var11[0]);
         var16.setBaseFrequencyY((double)var11[1]);
         var16.setNumOctaves(var12);
         var16.setSeed(var13);
         var16.setStitched(var14);
         var16.setFractalNoise(var15);
         handleColorInterpolationFilters(var16, var2);
         updateFilterMap(var2, var16, var7);
         return var16;
      }
   }

   protected static float[] convertBaseFrenquency(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "baseFrequency");
      if (var2.length() == 0) {
         return new float[]{0.001F, 0.001F};
      } else {
         float[] var3 = new float[2];
         StringTokenizer var4 = new StringTokenizer(var2, " ,");

         try {
            var3[0] = SVGUtilities.convertSVGNumber(var4.nextToken());
            if (var4.hasMoreTokens()) {
               var3[1] = SVGUtilities.convertSVGNumber(var4.nextToken());
            } else {
               var3[1] = var3[0];
            }

            if (var4.hasMoreTokens()) {
               throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"baseFrequency", var2});
            }
         } catch (NumberFormatException var6) {
            throw new BridgeException(var1, var0, var6, "attribute.malformed", new Object[]{"baseFrequency", var2});
         }

         if (!(var3[0] < 0.0F) && !(var3[1] < 0.0F)) {
            return var3;
         } else {
            throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"baseFrequency", var2});
         }
      }
   }

   protected static boolean convertStitchTiles(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "stitchTiles");
      if (var2.length() == 0) {
         return false;
      } else if ("stitch".equals(var2)) {
         return true;
      } else if ("noStitch".equals(var2)) {
         return false;
      } else {
         throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"stitchTiles", var2});
      }
   }

   protected static boolean convertType(Element var0, BridgeContext var1) {
      String var2 = var0.getAttributeNS((String)null, "type");
      if (var2.length() == 0) {
         return false;
      } else if ("fractalNoise".equals(var2)) {
         return true;
      } else if ("turbulence".equals(var2)) {
         return false;
      } else {
         throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"type", var2});
      }
   }
}

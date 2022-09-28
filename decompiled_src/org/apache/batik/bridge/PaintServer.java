package org.apache.batik.bridge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.svg.ICCColor;
import org.apache.batik.ext.awt.color.ICCColorSpaceExt;
import org.apache.batik.gvt.CompositeShapePainter;
import org.apache.batik.gvt.FillShapePainter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.Marker;
import org.apache.batik.gvt.MarkerShapePainter;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.gvt.ShapePainter;
import org.apache.batik.gvt.StrokeShapePainter;
import org.apache.batik.util.CSSConstants;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Element;

public abstract class PaintServer implements SVGConstants, CSSConstants, ErrorConstants {
   protected PaintServer() {
   }

   public static ShapePainter convertMarkers(Element var0, ShapeNode var1, BridgeContext var2) {
      Value var3 = CSSUtilities.getComputedStyle(var0, 36);
      Marker var4 = convertMarker(var0, var3, var2);
      var3 = CSSUtilities.getComputedStyle(var0, 35);
      Marker var5 = convertMarker(var0, var3, var2);
      var3 = CSSUtilities.getComputedStyle(var0, 34);
      Marker var6 = convertMarker(var0, var3, var2);
      if (var4 == null && var5 == null && var6 == null) {
         return null;
      } else {
         MarkerShapePainter var7 = new MarkerShapePainter(var1.getShape());
         var7.setStartMarker(var4);
         var7.setMiddleMarker(var5);
         var7.setEndMarker(var6);
         return var7;
      }
   }

   public static Marker convertMarker(Element var0, Value var1, BridgeContext var2) {
      if (var1.getPrimitiveType() == 21) {
         return null;
      } else {
         String var3 = var1.getStringValue();
         Element var4 = var2.getReferencedElement(var0, var3);
         Bridge var5 = var2.getBridge(var4);
         if (var5 != null && var5 instanceof MarkerBridge) {
            return ((MarkerBridge)var5).createMarker(var2, var4, var0);
         } else {
            throw new BridgeException(var2, var0, "css.uri.badTarget", new Object[]{var3});
         }
      }
   }

   public static ShapePainter convertFillAndStroke(Element var0, ShapeNode var1, BridgeContext var2) {
      Shape var3 = var1.getShape();
      if (var3 == null) {
         return null;
      } else {
         Paint var4 = convertFillPaint(var0, var1, var2);
         FillShapePainter var5 = new FillShapePainter(var3);
         var5.setPaint(var4);
         Stroke var6 = convertStroke(var0);
         if (var6 == null) {
            return var5;
         } else {
            Paint var7 = convertStrokePaint(var0, var1, var2);
            StrokeShapePainter var8 = new StrokeShapePainter(var3);
            var8.setStroke(var6);
            var8.setPaint(var7);
            CompositeShapePainter var9 = new CompositeShapePainter(var3);
            var9.addShapePainter(var5);
            var9.addShapePainter(var8);
            return var9;
         }
      }
   }

   public static ShapePainter convertStrokePainter(Element var0, ShapeNode var1, BridgeContext var2) {
      Shape var3 = var1.getShape();
      if (var3 == null) {
         return null;
      } else {
         Stroke var4 = convertStroke(var0);
         if (var4 == null) {
            return null;
         } else {
            Paint var5 = convertStrokePaint(var0, var1, var2);
            StrokeShapePainter var6 = new StrokeShapePainter(var3);
            var6.setStroke(var4);
            var6.setPaint(var5);
            return var6;
         }
      }
   }

   public static Paint convertStrokePaint(Element var0, GraphicsNode var1, BridgeContext var2) {
      Value var3 = CSSUtilities.getComputedStyle(var0, 51);
      float var4 = convertOpacity(var3);
      var3 = CSSUtilities.getComputedStyle(var0, 45);
      return convertPaint(var0, var1, var3, var4, var2);
   }

   public static Paint convertFillPaint(Element var0, GraphicsNode var1, BridgeContext var2) {
      Value var3 = CSSUtilities.getComputedStyle(var0, 16);
      float var4 = convertOpacity(var3);
      var3 = CSSUtilities.getComputedStyle(var0, 15);
      return convertPaint(var0, var1, var3, var4, var2);
   }

   public static Paint convertPaint(Element var0, GraphicsNode var1, Value var2, float var3, BridgeContext var4) {
      if (var2.getCssValueType() == 1) {
         switch (var2.getPrimitiveType()) {
            case 20:
               return convertURIPaint(var0, var1, var2, var3, var4);
            case 21:
               return null;
            case 25:
               return convertColor(var2, var3);
            default:
               throw new IllegalArgumentException("Paint argument is not an appropriate CSS value");
         }
      } else {
         Value var5 = var2.item(0);
         switch (var5.getPrimitiveType()) {
            case 20:
               Paint var6 = silentConvertURIPaint(var0, var1, var5, var3, var4);
               if (var6 != null) {
                  return var6;
               } else {
                  var5 = var2.item(1);
                  switch (var5.getPrimitiveType()) {
                     case 21:
                        return null;
                     case 25:
                        if (var2.getLength() == 2) {
                           return convertColor(var5, var3);
                        }

                        return convertRGBICCColor(var0, var5, (ICCColor)var2.item(2), var3, var4);
                     default:
                        throw new IllegalArgumentException("Paint argument is not an appropriate CSS value");
                  }
               }
            case 25:
               return convertRGBICCColor(var0, var5, (ICCColor)var2.item(1), var3, var4);
            default:
               throw new IllegalArgumentException("Paint argument is not an appropriate CSS value");
         }
      }
   }

   public static Paint silentConvertURIPaint(Element var0, GraphicsNode var1, Value var2, float var3, BridgeContext var4) {
      Paint var5 = null;

      try {
         var5 = convertURIPaint(var0, var1, var2, var3, var4);
      } catch (BridgeException var7) {
      }

      return var5;
   }

   public static Paint convertURIPaint(Element var0, GraphicsNode var1, Value var2, float var3, BridgeContext var4) {
      String var5 = var2.getStringValue();
      Element var6 = var4.getReferencedElement(var0, var5);
      Bridge var7 = var4.getBridge(var6);
      if (var7 != null && var7 instanceof PaintBridge) {
         return ((PaintBridge)var7).createPaint(var4, var6, var0, var1, var3);
      } else {
         throw new BridgeException(var4, var0, "css.uri.badTarget", new Object[]{var5});
      }
   }

   public static Color convertRGBICCColor(Element var0, Value var1, ICCColor var2, float var3, BridgeContext var4) {
      Color var5 = null;
      if (var2 != null) {
         var5 = convertICCColor(var0, var2, var3, var4);
      }

      if (var5 == null) {
         var5 = convertColor(var1, var3);
      }

      return var5;
   }

   public static Color convertICCColor(Element var0, ICCColor var1, float var2, BridgeContext var3) {
      String var4 = var1.getColorProfile();
      if (var4 == null) {
         return null;
      } else {
         SVGColorProfileElementBridge var5 = (SVGColorProfileElementBridge)var3.getBridge("http://www.w3.org/2000/svg", "color-profile");
         if (var5 == null) {
            return null;
         } else {
            ICCColorSpaceExt var6 = var5.createICCColorSpaceExt(var3, var0, var4);
            if (var6 == null) {
               return null;
            } else {
               int var7 = var1.getNumberOfColors();
               float[] var8 = new float[var7];
               if (var7 == 0) {
                  return null;
               } else {
                  for(int var9 = 0; var9 < var7; ++var9) {
                     var8[var9] = var1.getColor(var9);
                  }

                  float[] var10 = var6.intendedToRGB(var8);
                  return new Color(var10[0], var10[1], var10[2], var2);
               }
            }
         }
      }
   }

   public static Color convertColor(Value var0, float var1) {
      int var2 = resolveColorComponent(var0.getRed());
      int var3 = resolveColorComponent(var0.getGreen());
      int var4 = resolveColorComponent(var0.getBlue());
      return new Color(var2, var3, var4, Math.round(var1 * 255.0F));
   }

   public static Stroke convertStroke(Element var0) {
      Value var1 = CSSUtilities.getComputedStyle(var0, 52);
      float var2 = var1.getFloatValue();
      if (var2 == 0.0F) {
         return null;
      } else {
         var1 = CSSUtilities.getComputedStyle(var0, 48);
         int var3 = convertStrokeLinecap(var1);
         var1 = CSSUtilities.getComputedStyle(var0, 49);
         int var4 = convertStrokeLinejoin(var1);
         var1 = CSSUtilities.getComputedStyle(var0, 50);
         float var5 = convertStrokeMiterlimit(var1);
         var1 = CSSUtilities.getComputedStyle(var0, 46);
         float[] var6 = convertStrokeDasharray(var1);
         float var7 = 0.0F;
         if (var6 != null) {
            var1 = CSSUtilities.getComputedStyle(var0, 47);
            var7 = var1.getFloatValue();
            if (var7 < 0.0F) {
               float var8 = 0.0F;

               for(int var9 = 0; var9 < var6.length; ++var9) {
                  var8 += var6[var9];
               }

               if (var6.length % 2 != 0) {
                  var8 *= 2.0F;
               }

               if (var8 == 0.0F) {
                  var7 = 0.0F;
               } else {
                  while(var7 < 0.0F) {
                     var7 += var8;
                  }
               }
            }
         }

         return new BasicStroke(var2, var3, var4, var5, var6, var7);
      }
   }

   public static float[] convertStrokeDasharray(Value var0) {
      float[] var1 = null;
      if (var0.getCssValueType() == 2) {
         int var2 = var0.getLength();
         var1 = new float[var2];
         float var3 = 0.0F;

         for(int var4 = 0; var4 < var1.length; ++var4) {
            var1[var4] = var0.item(var4).getFloatValue();
            var3 += var1[var4];
         }

         if (var3 == 0.0F) {
            var1 = null;
         }
      }

      return var1;
   }

   public static float convertStrokeMiterlimit(Value var0) {
      float var1 = var0.getFloatValue();
      return var1 < 1.0F ? 1.0F : var1;
   }

   public static int convertStrokeLinecap(Value var0) {
      String var1 = var0.getStringValue();
      switch (var1.charAt(0)) {
         case 'b':
            return 0;
         case 'r':
            return 1;
         case 's':
            return 2;
         default:
            throw new IllegalArgumentException("Linecap argument is not an appropriate CSS value");
      }
   }

   public static int convertStrokeLinejoin(Value var0) {
      String var1 = var0.getStringValue();
      switch (var1.charAt(0)) {
         case 'b':
            return 2;
         case 'm':
            return 0;
         case 'r':
            return 1;
         default:
            throw new IllegalArgumentException("Linejoin argument is not an appropriate CSS value");
      }
   }

   public static int resolveColorComponent(Value var0) {
      float var1;
      switch (var0.getPrimitiveType()) {
         case 1:
            var1 = var0.getFloatValue();
            var1 = var1 > 255.0F ? 255.0F : (var1 < 0.0F ? 0.0F : var1);
            return Math.round(var1);
         case 2:
            var1 = var0.getFloatValue();
            var1 = var1 > 100.0F ? 100.0F : (var1 < 0.0F ? 0.0F : var1);
            return Math.round(255.0F * var1 / 100.0F);
         default:
            throw new IllegalArgumentException("Color component argument is not an appropriate CSS value");
      }
   }

   public static float convertOpacity(Value var0) {
      float var1 = var0.getFloatValue();
      return var1 < 0.0F ? 0.0F : (var1 > 1.0F ? 1.0F : var1);
   }
}

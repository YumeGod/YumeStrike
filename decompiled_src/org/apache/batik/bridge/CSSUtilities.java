package org.apache.batik.bridge;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.value.ListValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.svg.ICCColor;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.ext.awt.MultipleGradientPaint;
import org.apache.batik.ext.awt.image.renderable.ClipRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.filter.Mask;
import org.apache.batik.util.CSSConstants;
import org.apache.batik.util.XMLConstants;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

public abstract class CSSUtilities implements CSSConstants, ErrorConstants, XMLConstants {
   public static final Composite TRANSPARENT = AlphaComposite.getInstance(3, 0.0F);

   protected CSSUtilities() {
   }

   public static CSSEngine getCSSEngine(Element var0) {
      return ((SVGOMDocument)var0.getOwnerDocument()).getCSSEngine();
   }

   public static Value getComputedStyle(Element var0, int var1) {
      CSSEngine var2 = getCSSEngine(var0);
      return var2 == null ? null : var2.getComputedStyle((CSSStylableElement)var0, (String)null, var1);
   }

   public static int convertPointerEvents(Element var0) {
      Value var1 = getComputedStyle(var0, 40);
      String var2 = var1.getStringValue();
      switch (var2.charAt(0)) {
         case 'a':
            return 7;
         case 'f':
            return 5;
         case 'n':
            return 8;
         case 'p':
            return 4;
         case 's':
            return 6;
         case 'v':
            if (var2.length() == 7) {
               return 3;
            } else {
               switch (var2.charAt(7)) {
                  case 'f':
                     return 1;
                  case 'p':
                     return 0;
                  case 's':
                     return 2;
                  default:
                     throw new IllegalStateException("unexpected event, must be one of (p,f,s) is:" + var2.charAt(7));
               }
            }
         default:
            throw new IllegalStateException("unexpected event, must be one of (v,p,f,s,a,n) is:" + var2.charAt(0));
      }
   }

   public static Rectangle2D convertEnableBackground(Element var0) {
      Value var1 = getComputedStyle(var0, 14);
      if (var1.getCssValueType() != 2) {
         return null;
      } else {
         ListValue var2 = (ListValue)var1;
         int var3 = var2.getLength();
         switch (var3) {
            case 1:
               return CompositeGraphicsNode.VIEWPORT;
            case 5:
               float var4 = var2.item(1).getFloatValue();
               float var5 = var2.item(2).getFloatValue();
               float var6 = var2.item(3).getFloatValue();
               float var7 = var2.item(4).getFloatValue();
               return new Rectangle2D.Float(var4, var5, var6, var7);
            default:
               throw new IllegalStateException("Unexpected length:" + var3);
         }
      }
   }

   public static boolean convertColorInterpolationFilters(Element var0) {
      Value var1 = getComputedStyle(var0, 7);
      return "linearrgb" == var1.getStringValue();
   }

   public static MultipleGradientPaint.ColorSpaceEnum convertColorInterpolation(Element var0) {
      Value var1 = getComputedStyle(var0, 6);
      return "linearrgb" == var1.getStringValue() ? MultipleGradientPaint.LINEAR_RGB : MultipleGradientPaint.SRGB;
   }

   public static boolean isAutoCursor(Element var0) {
      Value var1 = getComputedStyle(var0, 10);
      boolean var2 = false;
      if (var1 != null) {
         if (var1.getCssValueType() == 1 && var1.getPrimitiveType() == 21 && var1.getStringValue().charAt(0) == 'a') {
            var2 = true;
         } else if (var1.getCssValueType() == 2 && var1.getLength() == 1) {
            Value var3 = var1.item(0);
            if (var3 != null && var3.getCssValueType() == 1 && var3.getPrimitiveType() == 21 && var3.getStringValue().charAt(0) == 'a') {
               var2 = true;
            }
         }
      }

      return var2;
   }

   public static Cursor convertCursor(Element var0, BridgeContext var1) {
      return var1.getCursorManager().convertCursor(var0);
   }

   public static RenderingHints convertShapeRendering(Element var0, RenderingHints var1) {
      Value var2 = getComputedStyle(var0, 42);
      String var3 = var2.getStringValue();
      int var4 = var3.length();
      if (var4 == 4 && var3.charAt(0) == 'a') {
         return var1;
      } else if (var4 < 10) {
         return var1;
      } else {
         if (var1 == null) {
            var1 = new RenderingHints((Map)null);
         }

         switch (var3.charAt(0)) {
            case 'c':
               var1.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
               var1.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
               break;
            case 'g':
               var1.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
               var1.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               var1.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
               break;
            case 'o':
               var1.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
               var1.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         }

         return var1;
      }
   }

   public static RenderingHints convertTextRendering(Element var0, RenderingHints var1) {
      Value var2 = getComputedStyle(var0, 55);
      String var3 = var2.getStringValue();
      int var4 = var3.length();
      if (var4 == 4 && var3.charAt(0) == 'a') {
         return var1;
      } else if (var4 < 13) {
         return var1;
      } else {
         if (var1 == null) {
            var1 = new RenderingHints((Map)null);
         }

         switch (var3.charAt(8)) {
            case 'c':
               var1.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
               var1.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
               var1.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               var1.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
               var1.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
               break;
            case 'l':
               var1.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
               var1.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
               var1.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               break;
            case 's':
               var1.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
               var1.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
               var1.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         }

         return var1;
      }
   }

   public static RenderingHints convertImageRendering(Element var0, RenderingHints var1) {
      Value var2 = getComputedStyle(var0, 30);
      String var3 = var2.getStringValue();
      int var4 = var3.length();
      if (var4 == 4 && var3.charAt(0) == 'a') {
         return var1;
      } else if (var4 < 13) {
         return var1;
      } else {
         if (var1 == null) {
            var1 = new RenderingHints((Map)null);
         }

         switch (var3.charAt(8)) {
            case 'q':
               var1.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
               var1.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
               break;
            case 's':
               var1.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
               var1.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
         }

         return var1;
      }
   }

   public static RenderingHints convertColorRendering(Element var0, RenderingHints var1) {
      Value var2 = getComputedStyle(var0, 9);
      String var3 = var2.getStringValue();
      int var4 = var3.length();
      if (var4 == 4 && var3.charAt(0) == 'a') {
         return var1;
      } else if (var4 < 13) {
         return var1;
      } else {
         if (var1 == null) {
            var1 = new RenderingHints((Map)null);
         }

         switch (var3.charAt(8)) {
            case 'q':
               var1.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
               var1.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
               break;
            case 's':
               var1.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
               var1.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
         }

         return var1;
      }
   }

   public static boolean convertDisplay(Element var0) {
      if (!(var0 instanceof CSSStylableElement)) {
         return true;
      } else {
         Value var1 = getComputedStyle(var0, 12);
         return var1.getStringValue().charAt(0) != 'n';
      }
   }

   public static boolean convertVisibility(Element var0) {
      Value var1 = getComputedStyle(var0, 57);
      return var1.getStringValue().charAt(0) == 'v';
   }

   public static Composite convertOpacity(Element var0) {
      Value var1 = getComputedStyle(var0, 38);
      float var2 = var1.getFloatValue();
      if (var2 <= 0.0F) {
         return TRANSPARENT;
      } else {
         return var2 >= 1.0F ? AlphaComposite.SrcOver : AlphaComposite.getInstance(3, var2);
      }
   }

   public static boolean convertOverflow(Element var0) {
      Value var1 = getComputedStyle(var0, 39);
      String var2 = var1.getStringValue();
      return var2.charAt(0) == 'h' || var2.charAt(0) == 's';
   }

   public static float[] convertClip(Element var0) {
      Value var1 = getComputedStyle(var0, 2);
      short var2 = var1.getPrimitiveType();
      switch (var2) {
         case 21:
            return null;
         case 24:
            float[] var3 = new float[]{var1.getTop().getFloatValue(), var1.getRight().getFloatValue(), var1.getBottom().getFloatValue(), var1.getLeft().getFloatValue()};
            return var3;
         default:
            throw new IllegalStateException("Unexpected primitiveType:" + var2);
      }
   }

   public static Filter convertFilter(Element var0, GraphicsNode var1, BridgeContext var2) {
      Value var3 = getComputedStyle(var0, 18);
      short var4 = var3.getPrimitiveType();
      switch (var4) {
         case 20:
            String var5 = var3.getStringValue();
            Element var6 = var2.getReferencedElement(var0, var5);
            Bridge var7 = var2.getBridge(var6);
            if (var7 != null && var7 instanceof FilterBridge) {
               return ((FilterBridge)var7).createFilter(var2, var6, var0, var1);
            }

            throw new BridgeException(var2, var0, "css.uri.badTarget", new Object[]{var5});
         case 21:
            return null;
         default:
            throw new IllegalStateException("Unexpected primitive type:" + var4);
      }
   }

   public static ClipRable convertClipPath(Element var0, GraphicsNode var1, BridgeContext var2) {
      Value var3 = getComputedStyle(var0, 3);
      short var4 = var3.getPrimitiveType();
      switch (var4) {
         case 20:
            String var5 = var3.getStringValue();
            Element var6 = var2.getReferencedElement(var0, var5);
            Bridge var7 = var2.getBridge(var6);
            if (var7 != null && var7 instanceof ClipBridge) {
               return ((ClipBridge)var7).createClip(var2, var6, var0, var1);
            }

            throw new BridgeException(var2, var0, "css.uri.badTarget", new Object[]{var5});
         case 21:
            return null;
         default:
            throw new IllegalStateException("Unexpected primitive type:" + var4);
      }
   }

   public static int convertClipRule(Element var0) {
      Value var1 = getComputedStyle(var0, 4);
      return var1.getStringValue().charAt(0) == 'n' ? 1 : 0;
   }

   public static Mask convertMask(Element var0, GraphicsNode var1, BridgeContext var2) {
      Value var3 = getComputedStyle(var0, 37);
      short var4 = var3.getPrimitiveType();
      switch (var4) {
         case 20:
            String var5 = var3.getStringValue();
            Element var6 = var2.getReferencedElement(var0, var5);
            Bridge var7 = var2.getBridge(var6);
            if (var7 != null && var7 instanceof MaskBridge) {
               return ((MaskBridge)var7).createMask(var2, var6, var0, var1);
            }

            throw new BridgeException(var2, var0, "css.uri.badTarget", new Object[]{var5});
         case 21:
            return null;
         default:
            throw new IllegalStateException("Unexpected primitive type:" + var4);
      }
   }

   public static int convertFillRule(Element var0) {
      Value var1 = getComputedStyle(var0, 17);
      return var1.getStringValue().charAt(0) == 'n' ? 1 : 0;
   }

   public static Color convertLightingColor(Element var0, BridgeContext var1) {
      Value var2 = getComputedStyle(var0, 33);
      return var2.getCssValueType() == 1 ? PaintServer.convertColor(var2, 1.0F) : PaintServer.convertRGBICCColor(var0, var2.item(0), (ICCColor)var2.item(1), 1.0F, var1);
   }

   public static Color convertFloodColor(Element var0, BridgeContext var1) {
      Value var2 = getComputedStyle(var0, 19);
      Value var3 = getComputedStyle(var0, 20);
      float var4 = PaintServer.convertOpacity(var3);
      return var2.getCssValueType() == 1 ? PaintServer.convertColor(var2, var4) : PaintServer.convertRGBICCColor(var0, var2.item(0), (ICCColor)var2.item(1), var4, var1);
   }

   public static Color convertStopColor(Element var0, float var1, BridgeContext var2) {
      Value var3 = getComputedStyle(var0, 43);
      Value var4 = getComputedStyle(var0, 44);
      var1 *= PaintServer.convertOpacity(var4);
      return var3.getCssValueType() == 1 ? PaintServer.convertColor(var3, var1) : PaintServer.convertRGBICCColor(var0, var3.item(0), (ICCColor)var3.item(1), var1, var2);
   }

   public static void computeStyleAndURIs(Element var0, Element var1, String var2) {
      int var3 = var2.indexOf(35);
      if (var3 != -1) {
         var2 = var2.substring(0, var3);
      }

      if (var2.length() != 0) {
         var1.setAttributeNS("http://www.w3.org/XML/1998/namespace", "base", var2);
      }

      CSSEngine var4 = getCSSEngine(var1);
      CSSEngine var5 = getCSSEngine(var0);
      var4.importCascadedStyleMaps(var0, var5, var1);
   }

   protected static int rule(CSSValue var0) {
      return ((CSSPrimitiveValue)var0).getStringValue().charAt(0) == 'n' ? 1 : 0;
   }
}

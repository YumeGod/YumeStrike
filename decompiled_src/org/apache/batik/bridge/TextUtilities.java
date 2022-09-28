package org.apache.batik.bridge;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.util.CSSConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class TextUtilities implements CSSConstants, ErrorConstants {
   public static String getElementContent(Element var0) {
      StringBuffer var1 = new StringBuffer();

      for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         switch (var2.getNodeType()) {
            case 1:
               var1.append(getElementContent((Element)var2));
            case 2:
            default:
               break;
            case 3:
            case 4:
               var1.append(var2.getNodeValue());
         }
      }

      return var1.toString();
   }

   public static ArrayList svgHorizontalCoordinateArrayToUserSpace(Element var0, String var1, String var2, BridgeContext var3) {
      org.apache.batik.parser.UnitProcessor.Context var4 = UnitProcessor.createContext(var3, var0);
      ArrayList var5 = new ArrayList();
      StringTokenizer var6 = new StringTokenizer(var2, ", ", false);

      while(var6.hasMoreTokens()) {
         var5.add(new Float(UnitProcessor.svgHorizontalCoordinateToUserSpace(var6.nextToken(), var1, var4)));
      }

      return var5;
   }

   public static ArrayList svgVerticalCoordinateArrayToUserSpace(Element var0, String var1, String var2, BridgeContext var3) {
      org.apache.batik.parser.UnitProcessor.Context var4 = UnitProcessor.createContext(var3, var0);
      ArrayList var5 = new ArrayList();
      StringTokenizer var6 = new StringTokenizer(var2, ", ", false);

      while(var6.hasMoreTokens()) {
         var5.add(new Float(UnitProcessor.svgVerticalCoordinateToUserSpace(var6.nextToken(), var1, var4)));
      }

      return var5;
   }

   public static ArrayList svgRotateArrayToFloats(Element var0, String var1, String var2, BridgeContext var3) {
      StringTokenizer var4 = new StringTokenizer(var2, ", ", false);
      ArrayList var5 = new ArrayList();

      while(var4.hasMoreTokens()) {
         try {
            String var6 = var4.nextToken();
            var5.add(new Float(Math.toRadians((double)SVGUtilities.convertSVGNumber(var6))));
         } catch (NumberFormatException var8) {
            throw new BridgeException(var3, var0, var8, "attribute.malformed", new Object[]{var1, var2});
         }
      }

      return var5;
   }

   public static Float convertFontSize(Element var0) {
      Value var1 = CSSUtilities.getComputedStyle(var0, 22);
      return new Float(var1.getFloatValue());
   }

   public static Float convertFontStyle(Element var0) {
      Value var1 = CSSUtilities.getComputedStyle(var0, 25);
      switch (var1.getStringValue().charAt(0)) {
         case 'n':
            return TextAttribute.POSTURE_REGULAR;
         default:
            return TextAttribute.POSTURE_OBLIQUE;
      }
   }

   public static Float convertFontStretch(Element var0) {
      Value var1 = CSSUtilities.getComputedStyle(var0, 24);
      String var2 = var1.getStringValue();
      switch (var2.charAt(0)) {
         case 'e':
            if (var2.charAt(6) == 'c') {
               return TextAttribute.WIDTH_CONDENSED;
            } else {
               if (var2.length() == 8) {
                  return TextAttribute.WIDTH_SEMI_EXTENDED;
               }

               return TextAttribute.WIDTH_EXTENDED;
            }
         case 's':
            if (var2.charAt(6) == 'c') {
               return TextAttribute.WIDTH_SEMI_CONDENSED;
            }

            return TextAttribute.WIDTH_SEMI_EXTENDED;
         case 'u':
            if (var2.charAt(6) == 'c') {
               return TextAttribute.WIDTH_CONDENSED;
            }

            return TextAttribute.WIDTH_EXTENDED;
         default:
            return TextAttribute.WIDTH_REGULAR;
      }
   }

   public static Float convertFontWeight(Element var0) {
      Value var1 = CSSUtilities.getComputedStyle(var0, 27);
      float var2 = var1.getFloatValue();
      switch ((int)var2) {
         case 100:
            return TextAttribute.WEIGHT_EXTRA_LIGHT;
         case 200:
            return TextAttribute.WEIGHT_LIGHT;
         case 300:
            return TextAttribute.WEIGHT_DEMILIGHT;
         case 400:
            return TextAttribute.WEIGHT_REGULAR;
         case 500:
            return TextAttribute.WEIGHT_SEMIBOLD;
         default:
            return TextAttribute.WEIGHT_BOLD;
      }
   }

   public static TextNode.Anchor convertTextAnchor(Element var0) {
      Value var1 = CSSUtilities.getComputedStyle(var0, 53);
      switch (var1.getStringValue().charAt(0)) {
         case 'm':
            return TextNode.Anchor.MIDDLE;
         case 's':
            return TextNode.Anchor.START;
         default:
            return TextNode.Anchor.END;
      }
   }

   public static Object convertBaselineShift(Element var0) {
      Value var1 = CSSUtilities.getComputedStyle(var0, 1);
      if (var1.getPrimitiveType() == 21) {
         String var2 = var1.getStringValue();
         switch (var2.charAt(2)) {
            case 'b':
               return TextAttribute.SUPERSCRIPT_SUB;
            case 'p':
               return TextAttribute.SUPERSCRIPT_SUPER;
            default:
               return null;
         }
      } else {
         return new Float(var1.getFloatValue());
      }
   }

   public static Float convertKerning(Element var0) {
      Value var1 = CSSUtilities.getComputedStyle(var0, 31);
      return var1.getPrimitiveType() == 21 ? null : new Float(var1.getFloatValue());
   }

   public static Float convertLetterSpacing(Element var0) {
      Value var1 = CSSUtilities.getComputedStyle(var0, 32);
      return var1.getPrimitiveType() == 21 ? null : new Float(var1.getFloatValue());
   }

   public static Float convertWordSpacing(Element var0) {
      Value var1 = CSSUtilities.getComputedStyle(var0, 58);
      return var1.getPrimitiveType() == 21 ? null : new Float(var1.getFloatValue());
   }
}

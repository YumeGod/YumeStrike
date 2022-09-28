package org.apache.batik.svggen;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SVGFont extends AbstractSVGConverter {
   public static final float EXTRA_LIGHT;
   public static final float LIGHT;
   public static final float DEMILIGHT;
   public static final float REGULAR;
   public static final float SEMIBOLD;
   public static final float MEDIUM;
   public static final float DEMIBOLD;
   public static final float BOLD;
   public static final float HEAVY;
   public static final float EXTRABOLD;
   public static final float ULTRABOLD;
   public static final float POSTURE_REGULAR;
   public static final float POSTURE_OBLIQUE;
   static final float[] fontStyles;
   static final String[] svgStyles;
   static final float[] fontWeights;
   static final String[] svgWeights;
   static Map logicalFontMap;
   static final int COMMON_FONT_SIZE = 100;
   final Map fontStringMap = new HashMap();

   public SVGFont(SVGGeneratorContext var1) {
      super(var1);
   }

   public void recordFontUsage(String var1, Font var2) {
      Font var3 = createCommonSizeFont(var2);
      String var4 = var3.getFamily() + var3.getStyle();
      CharListHelper var5 = (CharListHelper)this.fontStringMap.get(var4);
      if (var5 == null) {
         var5 = new CharListHelper();
      }

      for(int var6 = 0; var6 < var1.length(); ++var6) {
         char var7 = var1.charAt(var6);
         var5.add(var7);
      }

      this.fontStringMap.put(var4, var5);
   }

   private static Font createCommonSizeFont(Font var0) {
      HashMap var1 = new HashMap(var0.getAttributes());
      var1.put(TextAttribute.SIZE, new Float(100.0F));
      var1.remove(TextAttribute.TRANSFORM);
      return new Font(var1);
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      return this.toSVG(var1.getFont(), var1.getFontRenderContext());
   }

   public SVGFontDescriptor toSVG(Font var1, FontRenderContext var2) {
      FontRenderContext var3 = new FontRenderContext(new AffineTransform(), var2.isAntiAliased(), var2.usesFractionalMetrics());
      String var4 = "" + this.doubleString((double)var1.getSize2D());
      String var5 = weightToSVG(var1);
      String var6 = styleToSVG(var1);
      String var7 = familyToSVG(var1);
      Font var8 = createCommonSizeFont(var1);
      String var9 = var8.getFamily() + var8.getStyle();
      CharListHelper var10 = (CharListHelper)this.fontStringMap.get(var9);
      if (var10 == null) {
         return new SVGFontDescriptor(var4, var5, var6, var7, (Element)null);
      } else {
         Document var11 = this.generatorContext.domFactory;
         SVGFontDescriptor var12 = (SVGFontDescriptor)this.descMap.get(var9);
         Element var13;
         if (var12 != null) {
            var13 = var12.getDef();
         } else {
            var13 = var11.createElementNS("http://www.w3.org/2000/svg", "font");
            Element var14 = var11.createElementNS("http://www.w3.org/2000/svg", "font-face");
            String var15 = var7;
            if (var7.startsWith("'") && var7.endsWith("'")) {
               var15 = var7.substring(1, var7.length() - 1);
            }

            var14.setAttributeNS((String)null, "font-family", var15);
            var14.setAttributeNS((String)null, "font-weight", var5);
            var14.setAttributeNS((String)null, "font-style", var6);
            var14.setAttributeNS((String)null, "units-per-em", "100");
            var13.appendChild(var14);
            Element var16 = var11.createElementNS("http://www.w3.org/2000/svg", "missing-glyph");
            int[] var17 = new int[]{var8.getMissingGlyphCode()};
            GlyphVector var18 = var8.createGlyphVector(var3, var17);
            Shape var19 = var18.getGlyphOutline(0);
            GlyphMetrics var20 = var18.getGlyphMetrics(0);
            AffineTransform var21 = AffineTransform.getScaleInstance(1.0, -1.0);
            var19 = var21.createTransformedShape(var19);
            var16.setAttributeNS((String)null, "d", SVGPath.toSVGPathData(var19, this.generatorContext));
            var16.setAttributeNS((String)null, "horiz-adv-x", String.valueOf(var20.getAdvance()));
            var13.appendChild(var16);
            var13.setAttributeNS((String)null, "horiz-adv-x", String.valueOf(var20.getAdvance()));
            LineMetrics var22 = var8.getLineMetrics("By", var3);
            var14.setAttributeNS((String)null, "ascent", String.valueOf(var22.getAscent()));
            var14.setAttributeNS((String)null, "descent", String.valueOf(var22.getDescent()));
            var13.setAttributeNS((String)null, "id", this.generatorContext.idGenerator.generateID("font"));
         }

         String var25 = var10.getNewChars();
         var10.clearNewChars();

         for(int var26 = var25.length() - 1; var26 >= 0; --var26) {
            char var27 = var25.charAt(var26);
            String var29 = String.valueOf(var27);
            boolean var30 = false;
            NodeList var31 = var13.getChildNodes();

            for(int var32 = 0; var32 < var31.getLength(); ++var32) {
               if (var31.item(var32) instanceof Element) {
                  Element var34 = (Element)var31.item(var32);
                  if (var34.getAttributeNS((String)null, "unicode").equals(var29)) {
                     var30 = true;
                     break;
                  }
               }
            }

            if (var30) {
               break;
            }

            Element var33 = var11.createElementNS("http://www.w3.org/2000/svg", "glyph");
            GlyphVector var35 = var8.createGlyphVector(var3, "" + var27);
            Shape var36 = var35.getGlyphOutline(0);
            GlyphMetrics var23 = var35.getGlyphMetrics(0);
            AffineTransform var24 = AffineTransform.getScaleInstance(1.0, -1.0);
            var36 = var24.createTransformedShape(var36);
            var33.setAttributeNS((String)null, "d", SVGPath.toSVGPathData(var36, this.generatorContext));
            var33.setAttributeNS((String)null, "horiz-adv-x", String.valueOf(var23.getAdvance()));
            var33.setAttributeNS((String)null, "unicode", String.valueOf(var27));
            var13.appendChild(var33);
         }

         SVGFontDescriptor var28 = new SVGFontDescriptor(var4, var5, var6, var7, var13);
         if (var12 == null) {
            this.descMap.put(var9, var28);
            this.defSet.add(var13);
         }

         return var28;
      }
   }

   public static String familyToSVG(Font var0) {
      String var1 = var0.getFamily();
      String var2 = (String)logicalFontMap.get(var0.getName().toLowerCase());
      if (var2 != null) {
         var1 = var2;
      } else {
         var1 = '\'' + var1 + '\'';
      }

      return var1;
   }

   public static String styleToSVG(Font var0) {
      Map var1 = var0.getAttributes();
      Float var2 = (Float)var1.get(TextAttribute.POSTURE);
      if (var2 == null) {
         if (var0.isItalic()) {
            var2 = TextAttribute.POSTURE_OBLIQUE;
         } else {
            var2 = TextAttribute.POSTURE_REGULAR;
         }
      }

      float var3 = var2;
      boolean var4 = false;

      int var5;
      for(var5 = 0; var5 < fontStyles.length && !(var3 <= fontStyles[var5]); ++var5) {
      }

      return svgStyles[var5];
   }

   public static String weightToSVG(Font var0) {
      Map var1 = var0.getAttributes();
      Float var2 = (Float)var1.get(TextAttribute.WEIGHT);
      if (var2 == null) {
         if (var0.isBold()) {
            var2 = TextAttribute.WEIGHT_BOLD;
         } else {
            var2 = TextAttribute.WEIGHT_REGULAR;
         }
      }

      float var3 = var2;
      boolean var4 = false;

      int var5;
      for(var5 = 0; var5 < fontWeights.length && !(var3 <= fontWeights[var5]); ++var5) {
      }

      return svgWeights[var5];
   }

   static {
      EXTRA_LIGHT = TextAttribute.WEIGHT_EXTRA_LIGHT;
      LIGHT = TextAttribute.WEIGHT_LIGHT;
      DEMILIGHT = TextAttribute.WEIGHT_DEMILIGHT;
      REGULAR = TextAttribute.WEIGHT_REGULAR;
      SEMIBOLD = TextAttribute.WEIGHT_SEMIBOLD;
      MEDIUM = TextAttribute.WEIGHT_MEDIUM;
      DEMIBOLD = TextAttribute.WEIGHT_DEMIBOLD;
      BOLD = TextAttribute.WEIGHT_BOLD;
      HEAVY = TextAttribute.WEIGHT_HEAVY;
      EXTRABOLD = TextAttribute.WEIGHT_EXTRABOLD;
      ULTRABOLD = TextAttribute.WEIGHT_ULTRABOLD;
      POSTURE_REGULAR = TextAttribute.POSTURE_REGULAR;
      POSTURE_OBLIQUE = TextAttribute.POSTURE_OBLIQUE;
      fontStyles = new float[]{POSTURE_REGULAR + (POSTURE_OBLIQUE - POSTURE_REGULAR) / 2.0F};
      svgStyles = new String[]{"normal", "italic"};
      fontWeights = new float[]{EXTRA_LIGHT + (LIGHT - EXTRA_LIGHT) / 2.0F, LIGHT + (DEMILIGHT - LIGHT) / 2.0F, DEMILIGHT + (REGULAR - DEMILIGHT) / 2.0F, REGULAR + (SEMIBOLD - REGULAR) / 2.0F, SEMIBOLD + (MEDIUM - SEMIBOLD) / 2.0F, MEDIUM + (DEMIBOLD - MEDIUM) / 2.0F, DEMIBOLD + (BOLD - DEMIBOLD) / 2.0F, BOLD + (HEAVY - BOLD) / 2.0F, HEAVY + (EXTRABOLD - HEAVY) / 2.0F, EXTRABOLD + (ULTRABOLD - EXTRABOLD)};
      svgWeights = new String[]{"100", "200", "300", "normal", "500", "500", "600", "bold", "800", "800", "900"};
      logicalFontMap = new HashMap();
      logicalFontMap.put("dialog", "sans-serif");
      logicalFontMap.put("dialoginput", "monospace");
      logicalFontMap.put("monospaced", "monospace");
      logicalFontMap.put("serif", "serif");
      logicalFontMap.put("sansserif", "sans-serif");
      logicalFontMap.put("symbol", "'WingDings'");
   }

   private static class CharListHelper {
      private int nUsed = 0;
      private int[] charList = new int[40];
      private StringBuffer freshChars = new StringBuffer(40);

      CharListHelper() {
      }

      String getNewChars() {
         return this.freshChars.toString();
      }

      void clearNewChars() {
         this.freshChars = new StringBuffer(40);
      }

      boolean add(int var1) {
         int var2 = binSearch(this.charList, this.nUsed, var1);
         if (var2 >= 0) {
            return false;
         } else {
            if (this.nUsed == this.charList.length) {
               int[] var3 = new int[this.nUsed + 20];
               System.arraycopy(this.charList, 0, var3, 0, this.nUsed);
               this.charList = var3;
            }

            var2 = -var2 - 1;
            System.arraycopy(this.charList, var2, this.charList, var2 + 1, this.nUsed - var2);
            this.charList[var2] = var1;
            this.freshChars.append((char)var1);
            ++this.nUsed;
            return true;
         }
      }

      static int binSearch(int[] var0, int var1, int var2) {
         int var3 = 0;
         int var4 = var1 - 1;

         while(var3 <= var4) {
            int var5 = var3 + var4 >>> 1;
            int var6 = var0[var5];
            if (var6 < var2) {
               var3 = var5 + 1;
            } else {
               if (var6 <= var2) {
                  return var5;
               }

               var4 = var5 - 1;
            }
         }

         return -(var3 + 1);
      }
   }
}

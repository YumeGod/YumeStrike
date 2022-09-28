package org.apache.batik.bridge;

import java.awt.font.FontRenderContext;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTFontFace;
import org.apache.batik.gvt.font.GVTGlyphVector;
import org.apache.batik.gvt.font.GVTLineMetrics;
import org.apache.batik.gvt.font.Glyph;
import org.apache.batik.gvt.font.Kern;
import org.apache.batik.gvt.font.KerningTable;
import org.apache.batik.gvt.font.SVGGVTGlyphVector;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Element;

public final class SVGGVTFont implements GVTFont, SVGConstants {
   public static final AttributedCharacterIterator.Attribute PAINT_INFO;
   private float fontSize;
   private GVTFontFace fontFace;
   private String[] glyphUnicodes;
   private String[] glyphNames;
   private String[] glyphLangs;
   private String[] glyphOrientations;
   private String[] glyphForms;
   private Element[] glyphElements;
   private Element[] hkernElements;
   private Element[] vkernElements;
   private BridgeContext ctx;
   private Element textElement;
   private Element missingGlyphElement;
   private KerningTable hKerningTable;
   private KerningTable vKerningTable;
   private String language;
   private String orientation;
   private float scale;
   private GVTLineMetrics lineMetrics = null;

   public SVGGVTFont(float var1, GVTFontFace var2, String[] var3, String[] var4, String[] var5, String[] var6, String[] var7, BridgeContext var8, Element[] var9, Element var10, Element[] var11, Element[] var12, Element var13) {
      this.fontFace = var2;
      this.fontSize = var1;
      this.glyphUnicodes = var3;
      this.glyphNames = var4;
      this.glyphLangs = var5;
      this.glyphOrientations = var6;
      this.glyphForms = var7;
      this.ctx = var8;
      this.glyphElements = var9;
      this.missingGlyphElement = var10;
      this.hkernElements = var11;
      this.vkernElements = var12;
      this.scale = var1 / var2.getUnitsPerEm();
      this.textElement = var13;
      this.language = XMLSupport.getXMLLang(var13);
      Value var14 = CSSUtilities.getComputedStyle(var13, 59);
      if (var14.getStringValue().startsWith("tb")) {
         this.orientation = "v";
      } else {
         this.orientation = "h";
      }

      this.createKerningTables();
   }

   private void createKerningTables() {
      Kern[] var1 = new Kern[this.hkernElements.length];

      for(int var2 = 0; var2 < this.hkernElements.length; ++var2) {
         Element var3 = this.hkernElements[var2];
         SVGHKernElementBridge var4 = (SVGHKernElementBridge)this.ctx.getBridge(var3);
         Kern var5 = var4.createKern(this.ctx, var3, this);
         var1[var2] = var5;
      }

      this.hKerningTable = new KerningTable(var1);
      Kern[] var7 = new Kern[this.vkernElements.length];

      for(int var8 = 0; var8 < this.vkernElements.length; ++var8) {
         Element var9 = this.vkernElements[var8];
         SVGVKernElementBridge var10 = (SVGVKernElementBridge)this.ctx.getBridge(var9);
         Kern var6 = var10.createKern(this.ctx, var9, this);
         var7[var8] = var6;
      }

      this.vKerningTable = new KerningTable(var7);
   }

   public float getHKern(int var1, int var2) {
      if (var1 >= 0 && var1 < this.glyphUnicodes.length && var2 >= 0 && var2 < this.glyphUnicodes.length) {
         float var3 = this.hKerningTable.getKerningValue(var1, var2, this.glyphUnicodes[var1], this.glyphUnicodes[var2]);
         return var3 * this.scale;
      } else {
         return 0.0F;
      }
   }

   public float getVKern(int var1, int var2) {
      if (var1 >= 0 && var1 < this.glyphUnicodes.length && var2 >= 0 && var2 < this.glyphUnicodes.length) {
         float var3 = this.vKerningTable.getKerningValue(var1, var2, this.glyphUnicodes[var1], this.glyphUnicodes[var2]);
         return var3 * this.scale;
      } else {
         return 0.0F;
      }
   }

   public int[] getGlyphCodesForName(String var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.glyphNames.length; ++var3) {
         if (this.glyphNames[var3] != null && this.glyphNames[var3].equals(var1)) {
            var2.add(new Integer(var3));
         }
      }

      int[] var5 = new int[var2.size()];

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         var5[var4] = (Integer)var2.get(var4);
      }

      return var5;
   }

   public int[] getGlyphCodesForUnicode(String var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.glyphUnicodes.length; ++var3) {
         if (this.glyphUnicodes[var3] != null && this.glyphUnicodes[var3].equals(var1)) {
            var2.add(new Integer(var3));
         }
      }

      int[] var5 = new int[var2.size()];

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         var5[var4] = (Integer)var2.get(var4);
      }

      return var5;
   }

   private boolean languageMatches(String var1) {
      if (var1 != null && var1.length() != 0) {
         StringTokenizer var2 = new StringTokenizer(var1, ",");

         String var3;
         do {
            if (!var2.hasMoreTokens()) {
               return false;
            }

            var3 = var2.nextToken();
         } while(!var3.equals(this.language) && (!var3.startsWith(this.language) || var3.length() <= this.language.length() || var3.charAt(this.language.length()) != '-'));

         return true;
      } else {
         return true;
      }
   }

   private boolean orientationMatches(String var1) {
      return var1 != null && var1.length() != 0 ? var1.equals(this.orientation) : true;
   }

   private boolean formMatches(String var1, String var2, AttributedCharacterIterator var3, int var4) {
      if (var3 != null && var2 != null && var2.length() != 0) {
         var3.setIndex(var4);
         Integer var6 = (Integer)var3.getAttribute(GVTAttributedCharacterIterator.TextAttribute.ARABIC_FORM);
         if (var6 != null && !var6.equals(GVTAttributedCharacterIterator.TextAttribute.ARABIC_NONE)) {
            if (var1.length() > 1) {
               boolean var7 = true;

               for(int var8 = 1; var8 < var1.length(); ++var8) {
                  char var5 = var3.next();
                  if (var1.charAt(var8) != var5) {
                     var7 = false;
                     break;
                  }
               }

               var3.setIndex(var4);
               if (var7) {
                  var3.setIndex(var4 + var1.length() - 1);
                  Integer var9 = (Integer)var3.getAttribute(GVTAttributedCharacterIterator.TextAttribute.ARABIC_FORM);
                  var3.setIndex(var4);
                  if (var6 != null && var9 != null) {
                     if (var6.equals(GVTAttributedCharacterIterator.TextAttribute.ARABIC_TERMINAL) && var9.equals(GVTAttributedCharacterIterator.TextAttribute.ARABIC_INITIAL)) {
                        return var2.equals("isolated");
                     }

                     if (var6.equals(GVTAttributedCharacterIterator.TextAttribute.ARABIC_TERMINAL)) {
                        return var2.equals("terminal");
                     }

                     if (var6.equals(GVTAttributedCharacterIterator.TextAttribute.ARABIC_MEDIAL) && var9.equals(GVTAttributedCharacterIterator.TextAttribute.ARABIC_MEDIAL)) {
                        return var2.equals("medial");
                     }
                  }
               }
            }

            if (var6.equals(GVTAttributedCharacterIterator.TextAttribute.ARABIC_ISOLATED)) {
               return var2.equals("isolated");
            } else if (var6.equals(GVTAttributedCharacterIterator.TextAttribute.ARABIC_TERMINAL)) {
               return var2.equals("terminal");
            } else if (var6.equals(GVTAttributedCharacterIterator.TextAttribute.ARABIC_INITIAL)) {
               return var2.equals("initial");
            } else {
               return var6.equals(GVTAttributedCharacterIterator.TextAttribute.ARABIC_MEDIAL) ? var2.equals("medial") : false;
            }
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public boolean canDisplayGivenName(String var1) {
      for(int var2 = 0; var2 < this.glyphNames.length; ++var2) {
         if (this.glyphNames[var2] != null && this.glyphNames[var2].equals(var1) && this.languageMatches(this.glyphLangs[var2]) && this.orientationMatches(this.glyphOrientations[var2])) {
            return true;
         }
      }

      return false;
   }

   public boolean canDisplay(char var1) {
      for(int var2 = 0; var2 < this.glyphUnicodes.length; ++var2) {
         if (this.glyphUnicodes[var2].indexOf(var1) != -1 && this.languageMatches(this.glyphLangs[var2]) && this.orientationMatches(this.glyphOrientations[var2])) {
            return true;
         }
      }

      return false;
   }

   public int canDisplayUpTo(char[] var1, int var2, int var3) {
      StringCharacterIterator var4 = new StringCharacterIterator(new String(var1));
      return this.canDisplayUpTo((CharacterIterator)var4, var2, var3);
   }

   public int canDisplayUpTo(CharacterIterator var1, int var2, int var3) {
      AttributedCharacterIterator var4 = null;
      if (var1 instanceof AttributedCharacterIterator) {
         var4 = (AttributedCharacterIterator)var1;
      }

      char var5 = var1.setIndex(var2);

      for(int var6 = var2; var5 != '\uffff' && var6 < var3; var6 = var1.getIndex()) {
         boolean var7 = false;

         for(int var8 = 0; var8 < this.glyphUnicodes.length; ++var8) {
            if (this.glyphUnicodes[var8].indexOf(var5) == 0 && this.languageMatches(this.glyphLangs[var8]) && this.orientationMatches(this.glyphOrientations[var8]) && this.formMatches(this.glyphUnicodes[var8], this.glyphForms[var8], var4, var6)) {
               if (this.glyphUnicodes[var8].length() == 1) {
                  var7 = true;
                  break;
               }

               boolean var9 = true;

               for(int var10 = 1; var10 < this.glyphUnicodes[var8].length(); ++var10) {
                  var5 = var1.next();
                  if (this.glyphUnicodes[var8].charAt(var10) != var5) {
                     var9 = false;
                     break;
                  }
               }

               if (var9) {
                  var7 = true;
                  break;
               }

               var5 = var1.setIndex(var6);
            }
         }

         if (!var7) {
            return var6;
         }

         var5 = var1.next();
      }

      return -1;
   }

   public int canDisplayUpTo(String var1) {
      StringCharacterIterator var2 = new StringCharacterIterator(var1);
      return this.canDisplayUpTo((CharacterIterator)var2, 0, var1.length());
   }

   public GVTGlyphVector createGlyphVector(FontRenderContext var1, char[] var2) {
      StringCharacterIterator var3 = new StringCharacterIterator(new String(var2));
      return this.createGlyphVector(var1, (CharacterIterator)var3);
   }

   public GVTGlyphVector createGlyphVector(FontRenderContext var1, CharacterIterator var2) {
      AttributedCharacterIterator var3 = null;
      if (var2 instanceof AttributedCharacterIterator) {
         var3 = (AttributedCharacterIterator)var2;
      }

      ArrayList var4 = new ArrayList();

      for(char var5 = var2.first(); var5 != '\uffff'; var5 = var2.next()) {
         boolean var6 = false;

         for(int var7 = 0; var7 < this.glyphUnicodes.length; ++var7) {
            if (this.glyphUnicodes[var7].indexOf(var5) == 0 && this.languageMatches(this.glyphLangs[var7]) && this.orientationMatches(this.glyphOrientations[var7]) && this.formMatches(this.glyphUnicodes[var7], this.glyphForms[var7], var3, var2.getIndex())) {
               if (this.glyphUnicodes[var7].length() == 1) {
                  Element var17 = this.glyphElements[var7];
                  SVGGlyphElementBridge var19 = (SVGGlyphElementBridge)this.ctx.getBridge(var17);
                  TextPaintInfo var22 = null;
                  if (var3 != null) {
                     var22 = (TextPaintInfo)var3.getAttribute(PAINT_INFO);
                  }

                  Glyph var23 = var19.createGlyph(this.ctx, var17, this.textElement, var7, this.fontSize, this.fontFace, var22);
                  var4.add(var23);
                  var6 = true;
                  break;
               }

               int var8 = var2.getIndex();
               boolean var9 = true;

               for(int var10 = 1; var10 < this.glyphUnicodes[var7].length(); ++var10) {
                  var5 = var2.next();
                  if (this.glyphUnicodes[var7].charAt(var10) != var5) {
                     var9 = false;
                     break;
                  }
               }

               if (var9) {
                  Element var21 = this.glyphElements[var7];
                  SVGGlyphElementBridge var11 = (SVGGlyphElementBridge)this.ctx.getBridge(var21);
                  TextPaintInfo var12 = null;
                  if (var3 != null) {
                     var3.setIndex(var2.getIndex());
                     var12 = (TextPaintInfo)var3.getAttribute(PAINT_INFO);
                  }

                  Glyph var13 = var11.createGlyph(this.ctx, var21, this.textElement, var7, this.fontSize, this.fontFace, var12);
                  var4.add(var13);
                  var6 = true;
                  break;
               }

               var5 = var2.setIndex(var8);
            }
         }

         if (!var6) {
            SVGGlyphElementBridge var15 = (SVGGlyphElementBridge)this.ctx.getBridge(this.missingGlyphElement);
            TextPaintInfo var18 = null;
            if (var3 != null) {
               var3.setIndex(var2.getIndex());
               var18 = (TextPaintInfo)var3.getAttribute(PAINT_INFO);
            }

            Glyph var20 = var15.createGlyph(this.ctx, this.missingGlyphElement, this.textElement, -1, this.fontSize, this.fontFace, var18);
            var4.add(var20);
         }
      }

      int var14 = var4.size();
      Glyph[] var16 = (Glyph[])var4.toArray(new Glyph[var14]);
      return new SVGGVTGlyphVector(this, var16, var1);
   }

   public GVTGlyphVector createGlyphVector(FontRenderContext var1, int[] var2, CharacterIterator var3) {
      int var4 = var2.length;
      StringBuffer var5 = new StringBuffer(var4);

      for(int var6 = 0; var6 < var4; ++var6) {
         var5.append(this.glyphUnicodes[var2[var6]]);
      }

      StringCharacterIterator var7 = new StringCharacterIterator(var5.toString());
      return this.createGlyphVector(var1, (CharacterIterator)var7);
   }

   public GVTGlyphVector createGlyphVector(FontRenderContext var1, String var2) {
      StringCharacterIterator var3 = new StringCharacterIterator(var2);
      return this.createGlyphVector(var1, (CharacterIterator)var3);
   }

   public GVTFont deriveFont(float var1) {
      return new SVGGVTFont(var1, this.fontFace, this.glyphUnicodes, this.glyphNames, this.glyphLangs, this.glyphOrientations, this.glyphForms, this.ctx, this.glyphElements, this.missingGlyphElement, this.hkernElements, this.vkernElements, this.textElement);
   }

   public String getFamilyName() {
      return this.fontFace.getFamilyName();
   }

   protected GVTLineMetrics getLineMetrics(int var1, int var2) {
      if (this.lineMetrics != null) {
         return this.lineMetrics;
      } else {
         float var3 = this.fontFace.getUnitsPerEm();
         float var4 = this.fontSize / var3;
         float var5 = this.fontFace.getAscent() * var4;
         float var6 = this.fontFace.getDescent() * var4;
         float[] var7 = new float[]{0.0F, (var5 + var6) / 2.0F - var5, -var5};
         float var8 = this.fontFace.getStrikethroughPosition() * -var4;
         float var9 = this.fontFace.getStrikethroughThickness() * var4;
         float var10 = this.fontFace.getUnderlinePosition() * var4;
         float var11 = this.fontFace.getUnderlineThickness() * var4;
         float var12 = this.fontFace.getOverlinePosition() * -var4;
         float var13 = this.fontFace.getOverlineThickness() * var4;
         this.lineMetrics = new GVTLineMetrics(var5, 0, var7, var6, var3, var3, var2 - var1, var8, var9, var10, var11, var12, var13);
         return this.lineMetrics;
      }
   }

   public GVTLineMetrics getLineMetrics(char[] var1, int var2, int var3, FontRenderContext var4) {
      return this.getLineMetrics(var2, var3);
   }

   public GVTLineMetrics getLineMetrics(CharacterIterator var1, int var2, int var3, FontRenderContext var4) {
      return this.getLineMetrics(var2, var3);
   }

   public GVTLineMetrics getLineMetrics(String var1, FontRenderContext var2) {
      StringCharacterIterator var3 = new StringCharacterIterator(var1);
      return this.getLineMetrics((CharacterIterator)var3, 0, var1.length(), var2);
   }

   public GVTLineMetrics getLineMetrics(String var1, int var2, int var3, FontRenderContext var4) {
      StringCharacterIterator var5 = new StringCharacterIterator(var1);
      return this.getLineMetrics((CharacterIterator)var5, var2, var3, var4);
   }

   public float getSize() {
      return this.fontSize;
   }

   public String toString() {
      return this.fontFace.getFamilyName() + " " + this.fontFace.getFontWeight() + " " + this.fontFace.getFontStyle();
   }

   static {
      PAINT_INFO = GVTAttributedCharacterIterator.TextAttribute.PAINT_INFO;
   }
}

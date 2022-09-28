package org.apache.batik.gvt.font;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import org.apache.batik.gvt.text.ArabicTextHandler;

public class AWTGVTFont implements GVTFont {
   protected Font awtFont;
   protected float size;
   protected float scale;
   public static final float FONT_SIZE = 48.0F;
   static Map fontCache = new HashMap(11);

   public AWTGVTFont(Font var1) {
      this.size = var1.getSize2D();
      this.awtFont = var1.deriveFont(48.0F);
      this.scale = this.size / this.awtFont.getSize2D();
      initializeFontCache(this.awtFont);
   }

   public AWTGVTFont(Font var1, float var2) {
      this.size = var1.getSize2D() * var2;
      this.awtFont = var1.deriveFont(48.0F);
      this.scale = this.size / this.awtFont.getSize2D();
      initializeFontCache(this.awtFont);
   }

   public AWTGVTFont(Map var1) {
      Float var2 = (Float)var1.get(TextAttribute.SIZE);
      if (var2 != null) {
         this.size = var2;
         var1.put(TextAttribute.SIZE, new Float(48.0F));
         this.awtFont = new Font(var1);
      } else {
         this.awtFont = new Font(var1);
         this.size = this.awtFont.getSize2D();
      }

      this.scale = this.size / this.awtFont.getSize2D();
      initializeFontCache(this.awtFont);
   }

   public AWTGVTFont(String var1, int var2, int var3) {
      this.awtFont = new Font(var1, var2, 48);
      this.size = (float)var3;
      this.scale = (float)var3 / this.awtFont.getSize2D();
      initializeFontCache(this.awtFont);
   }

   public boolean canDisplay(char var1) {
      return this.awtFont.canDisplay(var1);
   }

   public int canDisplayUpTo(char[] var1, int var2, int var3) {
      return this.awtFont.canDisplayUpTo(var1, var2, var3);
   }

   public int canDisplayUpTo(CharacterIterator var1, int var2, int var3) {
      return this.awtFont.canDisplayUpTo(var1, var2, var3);
   }

   public int canDisplayUpTo(String var1) {
      return this.awtFont.canDisplayUpTo(var1);
   }

   public GVTGlyphVector createGlyphVector(FontRenderContext var1, char[] var2) {
      StringCharacterIterator var3 = new StringCharacterIterator(new String(var2));
      GlyphVector var4 = this.awtFont.createGlyphVector(var1, var2);
      return new AWTGVTGlyphVector(var4, this, this.scale, var3);
   }

   public GVTGlyphVector createGlyphVector(FontRenderContext var1, CharacterIterator var2) {
      if (var2 instanceof AttributedCharacterIterator) {
         AttributedCharacterIterator var3 = (AttributedCharacterIterator)var2;
         if (ArabicTextHandler.containsArabic(var3)) {
            String var4 = ArabicTextHandler.createSubstituteString(var3);
            return this.createGlyphVector(var1, var4);
         }
      }

      GlyphVector var5 = this.awtFont.createGlyphVector(var1, var2);
      return new AWTGVTGlyphVector(var5, this, this.scale, var2);
   }

   public GVTGlyphVector createGlyphVector(FontRenderContext var1, int[] var2, CharacterIterator var3) {
      return new AWTGVTGlyphVector(this.awtFont.createGlyphVector(var1, var2), this, this.scale, var3);
   }

   public GVTGlyphVector createGlyphVector(FontRenderContext var1, String var2) {
      StringCharacterIterator var3 = new StringCharacterIterator(var2);
      return new AWTGVTGlyphVector(this.awtFont.createGlyphVector(var1, var2), this, this.scale, var3);
   }

   public GVTFont deriveFont(float var1) {
      return new AWTGVTFont(this.awtFont, var1 / this.size);
   }

   public String getFamilyName() {
      return this.awtFont.getFamily();
   }

   public GVTLineMetrics getLineMetrics(char[] var1, int var2, int var3, FontRenderContext var4) {
      return new GVTLineMetrics(this.awtFont.getLineMetrics(var1, var2, var3, var4), this.scale);
   }

   public GVTLineMetrics getLineMetrics(CharacterIterator var1, int var2, int var3, FontRenderContext var4) {
      return new GVTLineMetrics(this.awtFont.getLineMetrics(var1, var2, var3, var4), this.scale);
   }

   public GVTLineMetrics getLineMetrics(String var1, FontRenderContext var2) {
      return new GVTLineMetrics(this.awtFont.getLineMetrics(var1, var2), this.scale);
   }

   public GVTLineMetrics getLineMetrics(String var1, int var2, int var3, FontRenderContext var4) {
      return new GVTLineMetrics(this.awtFont.getLineMetrics(var1, var2, var3, var4), this.scale);
   }

   public float getSize() {
      return this.size;
   }

   public float getHKern(int var1, int var2) {
      return 0.0F;
   }

   public float getVKern(int var1, int var2) {
      return 0.0F;
   }

   public static AWTGlyphGeometryCache.Value getGlyphGeometry(AWTGVTFont var0, char var1, GlyphVector var2, int var3, Point2D var4) {
      AWTGlyphGeometryCache var5 = (AWTGlyphGeometryCache)fontCache.get(var0.awtFont);
      AWTGlyphGeometryCache.Value var6 = var5.get(var1);
      if (var6 == null) {
         Shape var7 = var2.getGlyphOutline(var3);
         GlyphMetrics var8 = var2.getGlyphMetrics(var3);
         Rectangle2D var9 = var8.getBounds2D();
         if (AWTGVTGlyphVector.outlinesPositioned()) {
            AffineTransform var10 = AffineTransform.getTranslateInstance(-var4.getX(), -var4.getY());
            var7 = var10.createTransformedShape(var7);
         }

         var6 = new AWTGlyphGeometryCache.Value(var7, var9);
         var5.put(var1, var6);
      }

      return var6;
   }

   static void initializeFontCache(Font var0) {
      if (!fontCache.containsKey(var0)) {
         fontCache.put(var0, new AWTGlyphGeometryCache());
      }

   }

   static void putAWTGVTFont(AWTGVTFont var0) {
      fontCache.put(var0.awtFont, var0);
   }

   static AWTGVTFont getAWTGVTFont(Font var0) {
      return (AWTGVTFont)fontCache.get(var0);
   }
}

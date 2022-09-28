package org.apache.batik.gvt.font;

import java.awt.font.FontRenderContext;
import java.text.CharacterIterator;

public interface GVTFont {
   boolean canDisplay(char var1);

   int canDisplayUpTo(char[] var1, int var2, int var3);

   int canDisplayUpTo(CharacterIterator var1, int var2, int var3);

   int canDisplayUpTo(String var1);

   GVTGlyphVector createGlyphVector(FontRenderContext var1, char[] var2);

   GVTGlyphVector createGlyphVector(FontRenderContext var1, CharacterIterator var2);

   GVTGlyphVector createGlyphVector(FontRenderContext var1, int[] var2, CharacterIterator var3);

   GVTGlyphVector createGlyphVector(FontRenderContext var1, String var2);

   GVTFont deriveFont(float var1);

   String getFamilyName();

   GVTLineMetrics getLineMetrics(char[] var1, int var2, int var3, FontRenderContext var4);

   GVTLineMetrics getLineMetrics(CharacterIterator var1, int var2, int var3, FontRenderContext var4);

   GVTLineMetrics getLineMetrics(String var1, FontRenderContext var2);

   GVTLineMetrics getLineMetrics(String var1, int var2, int var3, FontRenderContext var4);

   float getSize();

   float getVKern(int var1, int var2);

   float getHKern(int var1, int var2);

   String toString();
}

package org.apache.batik.gvt.font;

import java.awt.font.FontRenderContext;
import java.text.AttributedCharacterIterator;

public interface AltGlyphHandler {
   GVTGlyphVector createGlyphVector(FontRenderContext var1, float var2, AttributedCharacterIterator var3);
}

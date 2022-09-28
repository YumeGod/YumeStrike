package org.apache.batik.gvt.text;

import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;

public class ConcreteTextLayoutFactory implements TextLayoutFactory {
   public TextSpanLayout createTextLayout(AttributedCharacterIterator var1, int[] var2, Point2D var3, FontRenderContext var4) {
      return new GlyphLayout(var1, var2, var3, var4);
   }
}

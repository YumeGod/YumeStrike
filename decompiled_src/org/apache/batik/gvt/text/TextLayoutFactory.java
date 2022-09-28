package org.apache.batik.gvt.text;

import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;

public interface TextLayoutFactory {
   TextSpanLayout createTextLayout(AttributedCharacterIterator var1, int[] var2, Point2D var3, FontRenderContext var4);
}

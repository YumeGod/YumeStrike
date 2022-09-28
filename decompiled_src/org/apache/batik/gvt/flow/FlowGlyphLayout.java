package org.apache.batik.gvt.flow;

import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import org.apache.batik.gvt.text.GlyphLayout;

public class FlowGlyphLayout extends GlyphLayout {
   public static final char SOFT_HYPHEN = '\u00ad';
   public static final char ZERO_WIDTH_SPACE = '\u200b';
   public static final char ZERO_WIDTH_JOINER = '\u200d';
   public static final char SPACE = ' ';

   public FlowGlyphLayout(AttributedCharacterIterator var1, int[] var2, Point2D var3, FontRenderContext var4) {
      super(var1, var2, var3, var4);
   }
}

package org.apache.batik.css.engine;

import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.engine.value.svg.OpacityManager;
import org.apache.batik.css.engine.value.svg.SVGColorManager;
import org.apache.batik.css.engine.value.svg12.LineHeightManager;
import org.apache.batik.css.engine.value.svg12.MarginLengthManager;
import org.apache.batik.css.engine.value.svg12.MarginShorthandManager;
import org.apache.batik.css.engine.value.svg12.TextAlignManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Document;

public class SVG12CSSEngine extends SVGCSSEngine {
   public static final ValueManager[] SVG_VALUE_MANAGERS = new ValueManager[]{new LineHeightManager(), new MarginLengthManager("indent"), new MarginLengthManager("margin-bottom"), new MarginLengthManager("margin-left"), new MarginLengthManager("margin-right"), new MarginLengthManager("margin-top"), new SVGColorManager("solid-color"), new OpacityManager("solid-opacity", true), new TextAlignManager()};
   public static final ShorthandManager[] SVG_SHORTHAND_MANAGERS = new ShorthandManager[]{new MarginShorthandManager()};
   public static final int LINE_HEIGHT_INDEX = 60;
   public static final int INDENT_INDEX = 61;
   public static final int MARGIN_BOTTOM_INDEX = 62;
   public static final int MARGIN_LEFT_INDEX = 63;
   public static final int MARGIN_RIGHT_INDEX = 64;
   public static final int MARGIN_TOP_INDEX = 65;
   public static final int SOLID_COLOR_INDEX = 66;
   public static final int SOLID_OPACITY_INDEX = 67;
   public static final int TEXT_ALIGN_INDEX = 68;
   public static final int FINAL_INDEX = 68;

   public SVG12CSSEngine(Document var1, ParsedURL var2, ExtendedParser var3, CSSContext var4) {
      super(var1, var2, var3, SVG_VALUE_MANAGERS, SVG_SHORTHAND_MANAGERS, var4);
      this.lineHeightIndex = 60;
   }

   public SVG12CSSEngine(Document var1, ParsedURL var2, ExtendedParser var3, ValueManager[] var4, ShorthandManager[] var5, CSSContext var6) {
      super(var1, var2, var3, mergeArrays(SVG_VALUE_MANAGERS, var4), mergeArrays(SVG_SHORTHAND_MANAGERS, var5), var6);
      this.lineHeightIndex = 60;
   }
}

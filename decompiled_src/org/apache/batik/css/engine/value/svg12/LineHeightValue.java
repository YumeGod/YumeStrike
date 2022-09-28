package org.apache.batik.css.engine.value.svg12;

import org.apache.batik.css.engine.value.FloatValue;

public class LineHeightValue extends FloatValue {
   protected boolean fontSizeRelative;

   public LineHeightValue(short var1, float var2, boolean var3) {
      super(var1, var2);
      this.fontSizeRelative = var3;
   }

   public boolean getFontSizeRelative() {
      return this.fontSizeRelative;
   }
}

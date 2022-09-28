package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.Value;

public class ColorInterpolationFiltersManager extends ColorInterpolationManager {
   public String getPropertyName() {
      return "color-interpolation-filters";
   }

   public Value getDefaultValue() {
      return SVGValueConstants.LINEARRGB_VALUE;
   }
}

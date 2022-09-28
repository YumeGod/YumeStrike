package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class ColorInterpolationManager extends IdentifierManager {
   protected static final StringMap values = new StringMap();

   public boolean isInheritedProperty() {
      return true;
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public int getPropertyType() {
      return 15;
   }

   public String getPropertyName() {
      return "color-interpolation";
   }

   public Value getDefaultValue() {
      return SVGValueConstants.SRGB_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("auto", ValueConstants.AUTO_VALUE);
      values.put("linearrgb", SVGValueConstants.LINEARRGB_VALUE);
      values.put("srgb", SVGValueConstants.SRGB_VALUE);
   }
}

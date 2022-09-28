package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class DominantBaselineManager extends IdentifierManager {
   protected static final StringMap values = new StringMap();

   public boolean isInheritedProperty() {
      return false;
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
      return "dominant-baseline";
   }

   public Value getDefaultValue() {
      return ValueConstants.AUTO_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("auto", ValueConstants.AUTO_VALUE);
      values.put("alphabetic", SVGValueConstants.ALPHABETIC_VALUE);
      values.put("central", SVGValueConstants.CENTRAL_VALUE);
      values.put("hanging", SVGValueConstants.HANGING_VALUE);
      values.put("ideographic", SVGValueConstants.IDEOGRAPHIC_VALUE);
      values.put("mathematical", SVGValueConstants.MATHEMATICAL_VALUE);
      values.put("middle", SVGValueConstants.MIDDLE_VALUE);
      values.put("no-change", SVGValueConstants.NO_CHANGE_VALUE);
      values.put("reset-size", SVGValueConstants.RESET_SIZE_VALUE);
      values.put("text-after-edge", SVGValueConstants.TEXT_AFTER_EDGE_VALUE);
      values.put("text-before-edge", SVGValueConstants.TEXT_BEFORE_EDGE_VALUE);
      values.put("text-bottom", SVGValueConstants.TEXT_BOTTOM_VALUE);
      values.put("text-top", SVGValueConstants.TEXT_TOP_VALUE);
      values.put("use-script", SVGValueConstants.USE_SCRIPT_VALUE);
   }
}

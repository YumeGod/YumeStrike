package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;

public class StrokeLinejoinManager extends IdentifierManager {
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
      return "stroke-linejoin";
   }

   public Value getDefaultValue() {
      return SVGValueConstants.MITER_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("miter", SVGValueConstants.MITER_VALUE);
      values.put("round", SVGValueConstants.ROUND_VALUE);
      values.put("bevel", SVGValueConstants.BEVEL_VALUE);
   }
}

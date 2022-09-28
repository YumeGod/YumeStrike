package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;

public class TextAnchorManager extends IdentifierManager {
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
      return "text-anchor";
   }

   public Value getDefaultValue() {
      return SVGValueConstants.START_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("start", SVGValueConstants.START_VALUE);
      values.put("middle", SVGValueConstants.MIDDLE_VALUE);
      values.put("end", SVGValueConstants.END_VALUE);
   }
}

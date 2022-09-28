package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class VisibilityManager extends IdentifierManager {
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
      return "visibility";
   }

   public Value getDefaultValue() {
      return ValueConstants.VISIBLE_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("visible", ValueConstants.VISIBLE_VALUE);
      values.put("hidden", ValueConstants.HIDDEN_VALUE);
      values.put("collapse", ValueConstants.COLLAPSE_VALUE);
   }
}

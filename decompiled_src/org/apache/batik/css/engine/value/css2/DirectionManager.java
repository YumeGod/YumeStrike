package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class DirectionManager extends IdentifierManager {
   protected static final StringMap values = new StringMap();

   public boolean isInheritedProperty() {
      return true;
   }

   public boolean isAnimatableProperty() {
      return false;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public int getPropertyType() {
      return 15;
   }

   public String getPropertyName() {
      return "direction";
   }

   public Value getDefaultValue() {
      return ValueConstants.LTR_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("ltr", ValueConstants.LTR_VALUE);
      values.put("rtl", ValueConstants.RTL_VALUE);
   }
}

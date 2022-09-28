package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class FontStyleManager extends IdentifierManager {
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
      return "font-style";
   }

   public Value getDefaultValue() {
      return ValueConstants.NORMAL_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("all", ValueConstants.ALL_VALUE);
      values.put("italic", ValueConstants.ITALIC_VALUE);
      values.put("normal", ValueConstants.NORMAL_VALUE);
      values.put("oblique", ValueConstants.OBLIQUE_VALUE);
   }
}

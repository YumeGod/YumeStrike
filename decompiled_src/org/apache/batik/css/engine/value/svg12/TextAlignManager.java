package org.apache.batik.css.engine.value.svg12;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class TextAlignManager extends IdentifierManager {
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
      return "text-align";
   }

   public Value getDefaultValue() {
      return ValueConstants.INHERIT_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("start", SVG12ValueConstants.START_VALUE);
      values.put("middle", SVG12ValueConstants.MIDDLE_VALUE);
      values.put("end", SVG12ValueConstants.END_VALUE);
      values.put("full", SVG12ValueConstants.FULL_VALUE);
   }
}

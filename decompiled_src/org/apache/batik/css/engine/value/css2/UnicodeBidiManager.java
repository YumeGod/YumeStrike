package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class UnicodeBidiManager extends IdentifierManager {
   protected static final StringMap values = new StringMap();

   public boolean isInheritedProperty() {
      return false;
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
      return "unicode-bidi";
   }

   public Value getDefaultValue() {
      return ValueConstants.NORMAL_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("bidi-override", ValueConstants.BIDI_OVERRIDE_VALUE);
      values.put("embed", ValueConstants.EMBED_VALUE);
      values.put("normal", ValueConstants.NORMAL_VALUE);
   }
}

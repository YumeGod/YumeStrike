package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;

public class WritingModeManager extends IdentifierManager {
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
      return "writing-mode";
   }

   public Value getDefaultValue() {
      return SVGValueConstants.LR_TB_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("lr", SVGValueConstants.LR_VALUE);
      values.put("lr-tb", SVGValueConstants.LR_TB_VALUE);
      values.put("rl", SVGValueConstants.RL_VALUE);
      values.put("rl-tb", SVGValueConstants.RL_TB_VALUE);
      values.put("tb", SVGValueConstants.TB_VALUE);
      values.put("tb-rl", SVGValueConstants.TB_RL_VALUE);
   }
}

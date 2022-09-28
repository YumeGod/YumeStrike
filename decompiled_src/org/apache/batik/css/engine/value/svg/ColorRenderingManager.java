package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class ColorRenderingManager extends IdentifierManager {
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
      return "color-rendering";
   }

   public Value getDefaultValue() {
      return ValueConstants.AUTO_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("auto", ValueConstants.AUTO_VALUE);
      values.put("optimizequality", SVGValueConstants.OPTIMIZEQUALITY_VALUE);
      values.put("optimizespeed", SVGValueConstants.OPTIMIZESPEED_VALUE);
   }
}

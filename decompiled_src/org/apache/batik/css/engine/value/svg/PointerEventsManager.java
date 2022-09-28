package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class PointerEventsManager extends IdentifierManager {
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
      return "pointer-events";
   }

   public Value getDefaultValue() {
      return SVGValueConstants.VISIBLEPAINTED_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("all", ValueConstants.ALL_VALUE);
      values.put("fill", SVGValueConstants.FILL_VALUE);
      values.put("fillstroke", SVGValueConstants.FILLSTROKE_VALUE);
      values.put("none", ValueConstants.NONE_VALUE);
      values.put("painted", ValueConstants.PAINTED_VALUE);
      values.put("stroke", ValueConstants.STROKE_VALUE);
      values.put("visible", ValueConstants.VISIBLE_VALUE);
      values.put("visiblefill", SVGValueConstants.VISIBLEFILL_VALUE);
      values.put("visiblefillstroke", SVGValueConstants.VISIBLEFILLSTROKE_VALUE);
      values.put("visiblepainted", SVGValueConstants.VISIBLEPAINTED_VALUE);
      values.put("visiblestroke", SVGValueConstants.VISIBLESTROKE_VALUE);
   }
}

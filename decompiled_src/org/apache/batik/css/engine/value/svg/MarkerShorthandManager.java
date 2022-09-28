package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.AbstractValueFactory;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class MarkerShorthandManager extends AbstractValueFactory implements ShorthandManager {
   public String getPropertyName() {
      return "marker";
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public void setValues(CSSEngine var1, ShorthandManager.PropertyHandler var2, LexicalUnit var3, boolean var4) throws DOMException {
      var2.property("marker-end", var3, var4);
      var2.property("marker-mid", var3, var4);
      var2.property("marker-start", var3, var4);
   }
}

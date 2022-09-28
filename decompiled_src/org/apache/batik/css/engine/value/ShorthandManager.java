package org.apache.batik.css.engine.value;

import org.apache.batik.css.engine.CSSEngine;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public interface ShorthandManager {
   String getPropertyName();

   boolean isAnimatableProperty();

   boolean isAdditiveProperty();

   void setValues(CSSEngine var1, PropertyHandler var2, LexicalUnit var3, boolean var4) throws DOMException;

   public interface PropertyHandler {
      void property(String var1, LexicalUnit var2, boolean var3);
   }
}

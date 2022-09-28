package org.apache.batik.css.engine.value;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.w3c.dom.DOMException;

public abstract class AbstractValueManager extends AbstractValueFactory implements ValueManager {
   public Value createFloatValue(short var1, float var2) throws DOMException {
      throw this.createDOMException();
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      throw this.createDOMException();
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      return (Value)(var6.getCssValueType() == 1 && var6.getPrimitiveType() == 20 ? new URIValue(var6.getStringValue(), var6.getStringValue()) : var6);
   }
}

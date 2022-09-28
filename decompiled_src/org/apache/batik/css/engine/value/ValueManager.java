package org.apache.batik.css.engine.value;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public interface ValueManager {
   String getPropertyName();

   boolean isInheritedProperty();

   boolean isAnimatableProperty();

   boolean isAdditiveProperty();

   int getPropertyType();

   Value getDefaultValue();

   Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException;

   Value createFloatValue(short var1, float var2) throws DOMException;

   Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException;

   Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6);
}

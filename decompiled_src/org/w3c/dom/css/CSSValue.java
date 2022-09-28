package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public interface CSSValue {
   short CSS_INHERIT = 0;
   short CSS_PRIMITIVE_VALUE = 1;
   short CSS_VALUE_LIST = 2;
   short CSS_CUSTOM = 3;

   String getCssText();

   void setCssText(String var1) throws DOMException;

   short getCssValueType();
}

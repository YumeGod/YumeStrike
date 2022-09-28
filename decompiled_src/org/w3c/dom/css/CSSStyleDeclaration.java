package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public interface CSSStyleDeclaration {
   String getCssText();

   void setCssText(String var1) throws DOMException;

   String getPropertyValue(String var1);

   CSSValue getPropertyCSSValue(String var1);

   String removeProperty(String var1) throws DOMException;

   String getPropertyPriority(String var1);

   void setProperty(String var1, String var2, String var3) throws DOMException;

   int getLength();

   String item(int var1);

   CSSRule getParentRule();
}

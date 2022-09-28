package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public interface CSSPageRule extends CSSRule {
   String getSelectorText();

   void setSelectorText(String var1) throws DOMException;

   CSSStyleDeclaration getStyle();
}

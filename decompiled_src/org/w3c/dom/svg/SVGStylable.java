package org.w3c.dom.svg;

import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

public interface SVGStylable {
   SVGAnimatedString getClassName();

   CSSStyleDeclaration getStyle();

   CSSValue getPresentationAttribute(String var1);
}

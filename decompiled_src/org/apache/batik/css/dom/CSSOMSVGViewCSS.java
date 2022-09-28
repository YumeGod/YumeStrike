package org.apache.batik.css.dom;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;

public class CSSOMSVGViewCSS extends CSSOMViewCSS {
   public CSSOMSVGViewCSS(CSSEngine var1) {
      super(var1);
   }

   public CSSStyleDeclaration getComputedStyle(Element var1, String var2) {
      return var1 instanceof CSSStylableElement ? new CSSOMSVGComputedStyle(this.cssEngine, (CSSStylableElement)var1, var2) : null;
   }
}

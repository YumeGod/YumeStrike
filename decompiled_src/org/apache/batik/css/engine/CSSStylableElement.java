package org.apache.batik.css.engine;

import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;

public interface CSSStylableElement extends Element {
   StyleMap getComputedStyleMap(String var1);

   void setComputedStyleMap(String var1, StyleMap var2);

   String getXMLId();

   String getCSSClass();

   ParsedURL getCSSBase();

   boolean isPseudoInstanceOf(String var1);

   StyleDeclarationProvider getOverrideStyleDeclarationProvider();
}

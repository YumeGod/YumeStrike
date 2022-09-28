package org.w3c.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;

public interface DOMImplementationCSS extends DOMImplementation {
   CSSStyleSheet createCSSStyleSheet(String var1, String var2) throws DOMException;
}

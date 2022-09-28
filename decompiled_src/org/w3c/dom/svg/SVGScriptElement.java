package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGScriptElement extends SVGElement, SVGURIReference, SVGExternalResourcesRequired {
   String getType();

   void setType(String var1) throws DOMException;
}

package org.w3c.dom.svg;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public interface SVGElement extends Element {
   String getId();

   void setId(String var1) throws DOMException;

   String getXMLbase();

   void setXMLbase(String var1) throws DOMException;

   SVGSVGElement getOwnerSVGElement();

   SVGElement getViewportElement();
}

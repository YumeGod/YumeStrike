package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGLangSpace {
   String getXMLlang();

   void setXMLlang(String var1) throws DOMException;

   String getXMLspace();

   void setXMLspace(String var1) throws DOMException;
}

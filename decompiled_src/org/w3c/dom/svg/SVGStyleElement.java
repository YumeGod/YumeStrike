package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGStyleElement extends SVGElement {
   String getXMLspace();

   void setXMLspace(String var1) throws DOMException;

   String getType();

   void setType(String var1) throws DOMException;

   String getMedia();

   void setMedia(String var1) throws DOMException;

   String getTitle();

   void setTitle(String var1) throws DOMException;
}

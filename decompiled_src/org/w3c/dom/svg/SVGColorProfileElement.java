package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGColorProfileElement extends SVGElement, SVGURIReference, SVGRenderingIntent {
   String getLocal();

   void setLocal(String var1) throws DOMException;

   String getName();

   void setName(String var1) throws DOMException;

   short getRenderingIntent();

   void setRenderingIntent(short var1) throws DOMException;
}

package org.w3c.dom.smil;

import org.w3c.dom.DOMException;

public interface ElementTimeControl {
   boolean beginElement() throws DOMException;

   boolean beginElementAt(float var1) throws DOMException;

   boolean endElement() throws DOMException;

   boolean endElementAt(float var1) throws DOMException;
}

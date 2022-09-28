package org.apache.batik.css.engine.value;

import org.w3c.dom.DOMException;

public interface Value {
   String getCssText();

   short getCssValueType();

   short getPrimitiveType();

   float getFloatValue() throws DOMException;

   String getStringValue() throws DOMException;

   Value getRed() throws DOMException;

   Value getGreen() throws DOMException;

   Value getBlue() throws DOMException;

   int getLength() throws DOMException;

   Value item(int var1) throws DOMException;

   Value getTop() throws DOMException;

   Value getRight() throws DOMException;

   Value getBottom() throws DOMException;

   Value getLeft() throws DOMException;

   String getIdentifier() throws DOMException;

   String getListStyle() throws DOMException;

   String getSeparator() throws DOMException;
}

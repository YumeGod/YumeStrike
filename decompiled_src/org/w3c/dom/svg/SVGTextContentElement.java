package org.w3c.dom.svg;

import org.w3c.dom.DOMException;
import org.w3c.dom.events.EventTarget;

public interface SVGTextContentElement extends SVGElement, SVGTests, SVGLangSpace, SVGExternalResourcesRequired, SVGStylable, EventTarget {
   short LENGTHADJUST_UNKNOWN = 0;
   short LENGTHADJUST_SPACING = 1;
   short LENGTHADJUST_SPACINGANDGLYPHS = 2;

   SVGAnimatedLength getTextLength();

   SVGAnimatedEnumeration getLengthAdjust();

   int getNumberOfChars();

   float getComputedTextLength();

   float getSubStringLength(int var1, int var2) throws DOMException;

   SVGPoint getStartPositionOfChar(int var1) throws DOMException;

   SVGPoint getEndPositionOfChar(int var1) throws DOMException;

   SVGRect getExtentOfChar(int var1) throws DOMException;

   float getRotationOfChar(int var1) throws DOMException;

   int getCharNumAtPosition(SVGPoint var1);

   void selectSubString(int var1, int var2) throws DOMException;
}

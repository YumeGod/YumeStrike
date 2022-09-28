package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGPathSegArcAbs extends SVGPathSeg {
   float getX();

   void setX(float var1) throws DOMException;

   float getY();

   void setY(float var1) throws DOMException;

   float getR1();

   void setR1(float var1) throws DOMException;

   float getR2();

   void setR2(float var1) throws DOMException;

   float getAngle();

   void setAngle(float var1) throws DOMException;

   boolean getLargeArcFlag();

   void setLargeArcFlag(boolean var1) throws DOMException;

   boolean getSweepFlag();

   void setSweepFlag(boolean var1) throws DOMException;
}

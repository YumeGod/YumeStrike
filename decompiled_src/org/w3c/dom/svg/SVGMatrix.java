package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGMatrix {
   float getA();

   void setA(float var1) throws DOMException;

   float getB();

   void setB(float var1) throws DOMException;

   float getC();

   void setC(float var1) throws DOMException;

   float getD();

   void setD(float var1) throws DOMException;

   float getE();

   void setE(float var1) throws DOMException;

   float getF();

   void setF(float var1) throws DOMException;

   SVGMatrix multiply(SVGMatrix var1);

   SVGMatrix inverse() throws SVGException;

   SVGMatrix translate(float var1, float var2);

   SVGMatrix scale(float var1);

   SVGMatrix scaleNonUniform(float var1, float var2);

   SVGMatrix rotate(float var1);

   SVGMatrix rotateFromVector(float var1, float var2) throws SVGException;

   SVGMatrix flipX();

   SVGMatrix flipY();

   SVGMatrix skewX(float var1);

   SVGMatrix skewY(float var1);
}

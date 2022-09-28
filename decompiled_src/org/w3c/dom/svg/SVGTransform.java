package org.w3c.dom.svg;

public interface SVGTransform {
   short SVG_TRANSFORM_UNKNOWN = 0;
   short SVG_TRANSFORM_MATRIX = 1;
   short SVG_TRANSFORM_TRANSLATE = 2;
   short SVG_TRANSFORM_SCALE = 3;
   short SVG_TRANSFORM_ROTATE = 4;
   short SVG_TRANSFORM_SKEWX = 5;
   short SVG_TRANSFORM_SKEWY = 6;

   short getType();

   SVGMatrix getMatrix();

   float getAngle();

   void setMatrix(SVGMatrix var1);

   void setTranslate(float var1, float var2);

   void setScale(float var1, float var2);

   void setRotate(float var1, float var2, float var3);

   void setSkewX(float var1);

   void setSkewY(float var1);
}

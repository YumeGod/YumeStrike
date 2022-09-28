package org.apache.batik.ext.awt.image.renderable;

public interface ColorMatrixRable extends FilterColorInterpolation {
   int TYPE_MATRIX = 0;
   int TYPE_SATURATE = 1;
   int TYPE_HUE_ROTATE = 2;
   int TYPE_LUMINANCE_TO_ALPHA = 3;

   Filter getSource();

   void setSource(Filter var1);

   int getType();

   float[][] getMatrix();
}

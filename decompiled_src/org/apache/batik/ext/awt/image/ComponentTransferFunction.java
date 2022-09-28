package org.apache.batik.ext.awt.image;

public interface ComponentTransferFunction {
   int IDENTITY = 0;
   int TABLE = 1;
   int DISCRETE = 2;
   int LINEAR = 3;
   int GAMMA = 4;

   int getType();

   float getSlope();

   float[] getTableValues();

   float getIntercept();

   float getAmplitude();

   float getExponent();

   float getOffset();
}

package org.apache.batik.ext.awt.geom;

public interface ExtendedPathIterator {
   int SEG_CLOSE = 4;
   int SEG_MOVETO = 0;
   int SEG_LINETO = 1;
   int SEG_QUADTO = 2;
   int SEG_CUBICTO = 3;
   int SEG_ARCTO = 4321;
   int WIND_EVEN_ODD = 0;
   int WIND_NON_ZERO = 1;

   int currentSegment();

   int currentSegment(double[] var1);

   int currentSegment(float[] var1);

   int getWindingRule();

   boolean isDone();

   void next();
}

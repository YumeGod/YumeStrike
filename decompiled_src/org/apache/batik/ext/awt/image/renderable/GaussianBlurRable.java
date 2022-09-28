package org.apache.batik.ext.awt.image.renderable;

public interface GaussianBlurRable extends FilterColorInterpolation {
   Filter getSource();

   void setSource(Filter var1);

   void setStdDeviationX(double var1);

   void setStdDeviationY(double var1);

   double getStdDeviationX();

   double getStdDeviationY();
}

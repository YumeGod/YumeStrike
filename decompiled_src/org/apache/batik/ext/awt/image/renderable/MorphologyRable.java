package org.apache.batik.ext.awt.image.renderable;

public interface MorphologyRable extends Filter {
   Filter getSource();

   void setSource(Filter var1);

   void setRadiusX(double var1);

   void setRadiusY(double var1);

   void setDoDilation(boolean var1);

   boolean getDoDilation();

   double getRadiusX();

   double getRadiusY();
}

package org.apache.batik.ext.awt.image.renderable;

public interface OffsetRable extends Filter {
   Filter getSource();

   void setSource(Filter var1);

   void setXoffset(double var1);

   double getXoffset();

   void setYoffset(double var1);

   double getYoffset();
}

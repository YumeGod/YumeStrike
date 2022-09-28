package org.apache.batik.ext.awt.image.renderable;

public interface FilterResRable extends Filter {
   Filter getSource();

   void setSource(Filter var1);

   int getFilterResolutionX();

   void setFilterResolutionX(int var1);

   int getFilterResolutionY();

   void setFilterResolutionY(int var1);
}

package org.apache.batik.ext.awt.image.renderable;

import java.awt.geom.Rectangle2D;

public interface FilterChainRable extends Filter {
   int getFilterResolutionX();

   void setFilterResolutionX(int var1);

   int getFilterResolutionY();

   void setFilterResolutionY(int var1);

   void setFilterRegion(Rectangle2D var1);

   Rectangle2D getFilterRegion();

   void setSource(Filter var1);

   Filter getSource();
}

package org.apache.batik.ext.awt.image.renderable;

import java.awt.geom.AffineTransform;

public interface AffineRable extends Filter {
   Filter getSource();

   void setSource(Filter var1);

   void setAffine(AffineTransform var1);

   AffineTransform getAffine();
}

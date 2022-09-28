package org.apache.batik.extension.svg;

import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.FilterColorInterpolation;

public interface BatikHistogramNormalizationFilter extends FilterColorInterpolation {
   Filter getSource();

   void setSource(Filter var1);

   float getTrim();

   void setTrim(float var1);
}

package org.apache.batik.ext.awt.image.renderable;

import java.awt.geom.Rectangle2D;
import org.apache.batik.ext.awt.image.Light;

public interface DiffuseLightingRable extends FilterColorInterpolation {
   Filter getSource();

   void setSource(Filter var1);

   Light getLight();

   void setLight(Light var1);

   double getSurfaceScale();

   void setSurfaceScale(double var1);

   double getKd();

   void setKd(double var1);

   Rectangle2D getLitRegion();

   void setLitRegion(Rectangle2D var1);

   double[] getKernelUnitLength();

   void setKernelUnitLength(double[] var1);
}

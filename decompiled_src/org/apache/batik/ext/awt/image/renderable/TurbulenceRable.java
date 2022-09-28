package org.apache.batik.ext.awt.image.renderable;

import java.awt.geom.Rectangle2D;

public interface TurbulenceRable extends FilterColorInterpolation {
   void setTurbulenceRegion(Rectangle2D var1);

   Rectangle2D getTurbulenceRegion();

   int getSeed();

   double getBaseFrequencyX();

   double getBaseFrequencyY();

   int getNumOctaves();

   boolean isStitched();

   boolean isFractalNoise();

   void setSeed(int var1);

   void setBaseFrequencyX(double var1);

   void setBaseFrequencyY(double var1);

   void setNumOctaves(int var1);

   void setStitched(boolean var1);

   void setFractalNoise(boolean var1);
}

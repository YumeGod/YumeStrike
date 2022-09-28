package org.apache.batik.ext.awt.image.renderable;

import java.awt.Point;
import java.awt.image.Kernel;
import org.apache.batik.ext.awt.image.PadMode;

public interface ConvolveMatrixRable extends FilterColorInterpolation {
   Filter getSource();

   void setSource(Filter var1);

   Kernel getKernel();

   void setKernel(Kernel var1);

   Point getTarget();

   void setTarget(Point var1);

   double getBias();

   void setBias(double var1);

   PadMode getEdgeMode();

   void setEdgeMode(PadMode var1);

   double[] getKernelUnitLength();

   void setKernelUnitLength(double[] var1);

   boolean getPreserveAlpha();

   void setPreserveAlpha(boolean var1);
}

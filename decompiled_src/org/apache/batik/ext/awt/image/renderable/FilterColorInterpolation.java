package org.apache.batik.ext.awt.image.renderable;

import java.awt.color.ColorSpace;

public interface FilterColorInterpolation extends Filter {
   boolean isColorSpaceLinear();

   void setColorSpaceLinear(boolean var1);

   ColorSpace getOperationColorSpace();
}

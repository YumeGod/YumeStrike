package org.apache.batik.ext.awt.image.renderable;

import org.apache.batik.ext.awt.image.ComponentTransferFunction;

public interface ComponentTransferRable extends FilterColorInterpolation {
   Filter getSource();

   void setSource(Filter var1);

   ComponentTransferFunction getAlphaFunction();

   void setAlphaFunction(ComponentTransferFunction var1);

   ComponentTransferFunction getRedFunction();

   void setRedFunction(ComponentTransferFunction var1);

   ComponentTransferFunction getGreenFunction();

   void setGreenFunction(ComponentTransferFunction var1);

   ComponentTransferFunction getBlueFunction();

   void setBlueFunction(ComponentTransferFunction var1);
}

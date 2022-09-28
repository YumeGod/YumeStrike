package org.apache.batik.ext.awt.image.renderable;

import java.util.List;
import org.apache.batik.ext.awt.image.ARGBChannel;

public interface DisplacementMapRable extends FilterColorInterpolation {
   int CHANNEL_R = 1;
   int CHANNEL_G = 2;
   int CHANNEL_B = 3;
   int CHANNEL_A = 4;

   void setSources(List var1);

   void setScale(double var1);

   double getScale();

   void setXChannelSelector(ARGBChannel var1);

   ARGBChannel getXChannelSelector();

   void setYChannelSelector(ARGBChannel var1);

   ARGBChannel getYChannelSelector();
}

package org.apache.batik.ext.awt.image.renderable;

import java.awt.Shape;

public interface ClipRable extends Filter {
   void setUseAntialiasedClip(boolean var1);

   boolean getUseAntialiasedClip();

   void setSource(Filter var1);

   Filter getSource();

   void setClipPath(Shape var1);

   Shape getClipPath();
}

package org.apache.batik.ext.awt.image.renderable;

import java.awt.geom.Rectangle2D;
import org.apache.batik.ext.awt.image.PadMode;

public interface PadRable extends Filter {
   Filter getSource();

   void setSource(Filter var1);

   void setPadRect(Rectangle2D var1);

   Rectangle2D getPadRect();

   void setPadMode(PadMode var1);

   PadMode getPadMode();
}

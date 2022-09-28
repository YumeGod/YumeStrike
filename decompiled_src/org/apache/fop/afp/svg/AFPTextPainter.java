package org.apache.fop.afp.svg;

import java.awt.Graphics2D;
import org.apache.fop.afp.AFPGraphics2D;
import org.apache.fop.svg.AbstractFOPTextPainter;
import org.apache.fop.svg.FOPTextHandler;

public class AFPTextPainter extends AbstractFOPTextPainter {
   public AFPTextPainter(FOPTextHandler nativeTextHandler) {
      super(nativeTextHandler);
   }

   protected boolean isSupportedGraphics2D(Graphics2D g2d) {
      return g2d instanceof AFPGraphics2D;
   }
}

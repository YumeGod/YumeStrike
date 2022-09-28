package org.apache.fop.render.ps;

import org.apache.xmlgraphics.java2d.ps.AbstractPSDocumentGraphics2D;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;

public class EPSTranscoder extends AbstractPSTranscoder {
   protected AbstractPSDocumentGraphics2D createDocumentGraphics2D() {
      return new EPSDocumentGraphics2D(false);
   }
}

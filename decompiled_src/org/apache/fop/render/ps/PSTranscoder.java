package org.apache.fop.render.ps;

import org.apache.xmlgraphics.java2d.ps.AbstractPSDocumentGraphics2D;
import org.apache.xmlgraphics.java2d.ps.PSDocumentGraphics2D;

public class PSTranscoder extends AbstractPSTranscoder {
   protected AbstractPSDocumentGraphics2D createDocumentGraphics2D() {
      return new PSDocumentGraphics2D(false);
   }
}

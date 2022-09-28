package org.apache.fop.afp;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractAFPPainter {
   protected static Log log = LogFactory.getLog("org.apache.xmlgraphics.afp");
   protected final DataStream dataStream;
   protected final AFPPaintingState paintingState;

   public AbstractAFPPainter(AFPPaintingState paintingState, DataStream dataStream) {
      this.paintingState = paintingState;
      this.dataStream = dataStream;
   }

   public abstract void paint(PaintingInfo var1) throws IOException;
}

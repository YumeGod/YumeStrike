package org.apache.fop.afp.ptoca;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.AFPLineDataInfo;

public class LineDataInfoProducer implements PtocaProducer, PtocaConstants {
   private static final Log log;
   private AFPLineDataInfo lineDataInfo;

   public LineDataInfoProducer(AFPLineDataInfo lineDataInfo) {
      this.lineDataInfo = lineDataInfo;
   }

   public void produce(PtocaBuilder builder) throws IOException {
      builder.setTextOrientation(this.lineDataInfo.getRotation());
      int x1 = ensurePositive(this.lineDataInfo.getX1());
      int y1 = ensurePositive(this.lineDataInfo.getY1());
      builder.absoluteMoveBaseline(y1);
      builder.absoluteMoveInline(x1);
      builder.setExtendedTextColor(this.lineDataInfo.getColor());
      int x2 = ensurePositive(this.lineDataInfo.getX2());
      int y2 = ensurePositive(this.lineDataInfo.getY2());
      int thickness = this.lineDataInfo.getThickness();
      if (y1 == y2) {
         builder.drawIaxisRule(x2 - x1, thickness);
      } else {
         if (x1 != x2) {
            log.error("Invalid axis rule: unable to draw line");
            return;
         }

         builder.drawBaxisRule(y2 - y1, thickness);
      }

   }

   private static int ensurePositive(int value) {
      return value < 0 ? 0 : value;
   }

   static {
      log = LogFactory.getLog(LineDataInfoProducer.class);
   }
}

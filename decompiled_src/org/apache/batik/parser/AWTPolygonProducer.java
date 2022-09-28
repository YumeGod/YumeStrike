package org.apache.batik.parser;

import java.awt.Shape;
import java.io.IOException;
import java.io.Reader;

public class AWTPolygonProducer extends AWTPolylineProducer {
   public static Shape createShape(Reader var0, int var1) throws IOException, ParseException {
      PointsParser var2 = new PointsParser();
      AWTPolygonProducer var3 = new AWTPolygonProducer();
      var3.setWindingRule(var1);
      var2.setPointsHandler(var3);
      var2.parse(var0);
      return var3.getShape();
   }

   public void endPoints() throws ParseException {
      this.path.closePath();
   }
}

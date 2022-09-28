package org.apache.batik.parser;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.io.Reader;

public class AWTPolylineProducer implements PointsHandler, ShapeProducer {
   protected GeneralPath path;
   protected boolean newPath;
   protected int windingRule;

   public static Shape createShape(Reader var0, int var1) throws IOException, ParseException {
      PointsParser var2 = new PointsParser();
      AWTPolylineProducer var3 = new AWTPolylineProducer();
      var3.setWindingRule(var1);
      var2.setPointsHandler(var3);
      var2.parse(var0);
      return var3.getShape();
   }

   public void setWindingRule(int var1) {
      this.windingRule = var1;
   }

   public int getWindingRule() {
      return this.windingRule;
   }

   public Shape getShape() {
      return this.path;
   }

   public void startPoints() throws ParseException {
      this.path = new GeneralPath(this.windingRule);
      this.newPath = true;
   }

   public void point(float var1, float var2) throws ParseException {
      if (this.newPath) {
         this.newPath = false;
         this.path.moveTo(var1, var2);
      } else {
         this.path.lineTo(var1, var2);
      }

   }

   public void endPoints() throws ParseException {
   }
}

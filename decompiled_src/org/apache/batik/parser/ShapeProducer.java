package org.apache.batik.parser;

import java.awt.Shape;

public interface ShapeProducer {
   Shape getShape();

   void setWindingRule(int var1);

   int getWindingRule();
}

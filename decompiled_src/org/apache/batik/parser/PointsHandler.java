package org.apache.batik.parser;

public interface PointsHandler {
   void startPoints() throws ParseException;

   void point(float var1, float var2) throws ParseException;

   void endPoints() throws ParseException;
}

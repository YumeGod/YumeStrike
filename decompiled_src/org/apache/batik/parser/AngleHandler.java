package org.apache.batik.parser;

public interface AngleHandler {
   void startAngle() throws ParseException;

   void angleValue(float var1) throws ParseException;

   void deg() throws ParseException;

   void grad() throws ParseException;

   void rad() throws ParseException;

   void endAngle() throws ParseException;
}

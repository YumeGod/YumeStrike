package org.apache.batik.parser;

public interface LengthListHandler extends LengthHandler {
   void startLengthList() throws ParseException;

   void endLengthList() throws ParseException;
}

package org.apache.batik.parser;

public interface NumberListHandler {
   void startNumberList() throws ParseException;

   void endNumberList() throws ParseException;

   void startNumber() throws ParseException;

   void endNumber() throws ParseException;

   void numberValue(float var1) throws ParseException;
}

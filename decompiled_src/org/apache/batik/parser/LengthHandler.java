package org.apache.batik.parser;

public interface LengthHandler {
   void startLength() throws ParseException;

   void lengthValue(float var1) throws ParseException;

   void em() throws ParseException;

   void ex() throws ParseException;

   void in() throws ParseException;

   void cm() throws ParseException;

   void mm() throws ParseException;

   void pc() throws ParseException;

   void pt() throws ParseException;

   void px() throws ParseException;

   void percentage() throws ParseException;

   void endLength() throws ParseException;
}

package org.apache.batik.parser;

public class DefaultErrorHandler implements ErrorHandler {
   public void error(ParseException var1) throws ParseException {
      throw var1;
   }
}

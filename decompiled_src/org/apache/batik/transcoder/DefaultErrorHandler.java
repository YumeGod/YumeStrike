package org.apache.batik.transcoder;

public class DefaultErrorHandler implements ErrorHandler {
   public void error(TranscoderException var1) throws TranscoderException {
      System.err.println("ERROR: " + var1.getMessage());
   }

   public void fatalError(TranscoderException var1) throws TranscoderException {
      throw var1;
   }

   public void warning(TranscoderException var1) throws TranscoderException {
      System.err.println("WARNING: " + var1.getMessage());
   }
}

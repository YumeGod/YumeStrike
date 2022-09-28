package org.apache.batik.transcoder;

public interface ErrorHandler {
   void error(TranscoderException var1) throws TranscoderException;

   void fatalError(TranscoderException var1) throws TranscoderException;

   void warning(TranscoderException var1) throws TranscoderException;
}

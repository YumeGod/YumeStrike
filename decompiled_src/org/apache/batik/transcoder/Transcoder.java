package org.apache.batik.transcoder;

import java.util.Map;

public interface Transcoder {
   void transcode(TranscoderInput var1, TranscoderOutput var2) throws TranscoderException;

   TranscodingHints getTranscodingHints();

   void addTranscodingHint(TranscodingHints.Key var1, Object var2);

   void removeTranscodingHint(TranscodingHints.Key var1);

   void setTranscodingHints(Map var1);

   void setTranscodingHints(TranscodingHints var1);

   void setErrorHandler(ErrorHandler var1);

   ErrorHandler getErrorHandler();
}

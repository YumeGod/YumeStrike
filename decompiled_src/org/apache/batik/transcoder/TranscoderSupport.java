package org.apache.batik.transcoder;

import java.util.Map;

public class TranscoderSupport {
   static final ErrorHandler defaultErrorHandler = new DefaultErrorHandler();
   protected TranscodingHints hints = new TranscodingHints();
   protected ErrorHandler handler;

   public TranscoderSupport() {
      this.handler = defaultErrorHandler;
   }

   public TranscodingHints getTranscodingHints() {
      return new TranscodingHints(this.hints);
   }

   public void addTranscodingHint(TranscodingHints.Key var1, Object var2) {
      this.hints.put(var1, var2);
   }

   public void removeTranscodingHint(TranscodingHints.Key var1) {
      this.hints.remove(var1);
   }

   public void setTranscodingHints(Map var1) {
      this.hints.putAll(var1);
   }

   public void setTranscodingHints(TranscodingHints var1) {
      this.hints = var1;
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.handler = var1;
   }

   public ErrorHandler getErrorHandler() {
      return this.handler;
   }
}

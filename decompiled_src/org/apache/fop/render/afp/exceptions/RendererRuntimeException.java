package org.apache.fop.render.afp.exceptions;

public class RendererRuntimeException extends NestedRuntimeException {
   public RendererRuntimeException(String msg) {
      super(msg);
   }

   public RendererRuntimeException(String msg, Throwable t) {
      super(msg, t);
   }
}

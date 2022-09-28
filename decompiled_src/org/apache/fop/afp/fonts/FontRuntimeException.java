package org.apache.fop.afp.fonts;

public class FontRuntimeException extends RuntimeException {
   private static final long serialVersionUID = -2217420523816384707L;

   public FontRuntimeException(String msg) {
      super(msg);
   }

   public FontRuntimeException(String msg, Throwable t) {
      super(msg, t);
   }
}

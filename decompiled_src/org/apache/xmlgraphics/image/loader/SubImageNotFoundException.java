package org.apache.xmlgraphics.image.loader;

public class SubImageNotFoundException extends ImageException {
   private static final long serialVersionUID = 3785613905389979249L;

   public SubImageNotFoundException(String s) {
      super(s);
   }

   public SubImageNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}

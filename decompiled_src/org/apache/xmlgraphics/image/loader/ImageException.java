package org.apache.xmlgraphics.image.loader;

public class ImageException extends Exception {
   private static final long serialVersionUID = 3785613905389979249L;

   public ImageException(String s) {
      super(s);
   }

   public ImageException(String message, Throwable cause) {
      super(message, cause);
   }
}

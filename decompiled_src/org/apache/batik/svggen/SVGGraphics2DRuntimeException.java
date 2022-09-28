package org.apache.batik.svggen;

public class SVGGraphics2DRuntimeException extends RuntimeException {
   private Exception embedded;

   public SVGGraphics2DRuntimeException(String var1) {
      this(var1, (Exception)null);
   }

   public SVGGraphics2DRuntimeException(Exception var1) {
      this((String)null, var1);
   }

   public SVGGraphics2DRuntimeException(String var1, Exception var2) {
      super(var1);
      this.embedded = var2;
   }

   public String getMessage() {
      String var1 = super.getMessage();
      if (var1 != null) {
         return var1;
      } else {
         return this.embedded != null ? this.embedded.getMessage() : null;
      }
   }

   public Exception getException() {
      return this.embedded;
   }
}

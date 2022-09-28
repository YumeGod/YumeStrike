package org.apache.batik.svggen;

import java.io.IOException;

public class SVGGraphics2DIOException extends IOException {
   private IOException embedded;

   public SVGGraphics2DIOException(String var1) {
      this(var1, (IOException)null);
   }

   public SVGGraphics2DIOException(IOException var1) {
      this((String)null, var1);
   }

   public SVGGraphics2DIOException(String var1, IOException var2) {
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

   public IOException getException() {
      return this.embedded;
   }
}

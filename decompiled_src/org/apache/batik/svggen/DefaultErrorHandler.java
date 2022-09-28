package org.apache.batik.svggen;

public class DefaultErrorHandler implements ErrorHandler {
   public void handleError(SVGGraphics2DIOException var1) throws SVGGraphics2DIOException {
      throw var1;
   }

   public void handleError(SVGGraphics2DRuntimeException var1) throws SVGGraphics2DRuntimeException {
      System.err.println(var1.getMessage());
   }
}

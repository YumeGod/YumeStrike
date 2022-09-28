package org.apache.batik.svggen;

public interface ErrorHandler {
   void handleError(SVGGraphics2DIOException var1) throws SVGGraphics2DIOException;

   void handleError(SVGGraphics2DRuntimeException var1) throws SVGGraphics2DRuntimeException;
}

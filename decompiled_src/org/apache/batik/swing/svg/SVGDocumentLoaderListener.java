package org.apache.batik.swing.svg;

public interface SVGDocumentLoaderListener {
   void documentLoadingStarted(SVGDocumentLoaderEvent var1);

   void documentLoadingCompleted(SVGDocumentLoaderEvent var1);

   void documentLoadingCancelled(SVGDocumentLoaderEvent var1);

   void documentLoadingFailed(SVGDocumentLoaderEvent var1);
}

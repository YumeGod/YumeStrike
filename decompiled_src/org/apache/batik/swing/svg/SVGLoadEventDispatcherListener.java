package org.apache.batik.swing.svg;

public interface SVGLoadEventDispatcherListener {
   void svgLoadEventDispatchStarted(SVGLoadEventDispatcherEvent var1);

   void svgLoadEventDispatchCompleted(SVGLoadEventDispatcherEvent var1);

   void svgLoadEventDispatchCancelled(SVGLoadEventDispatcherEvent var1);

   void svgLoadEventDispatchFailed(SVGLoadEventDispatcherEvent var1);
}

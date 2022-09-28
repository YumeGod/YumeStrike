package org.apache.batik.dom.svg12;

import org.w3c.dom.events.EventTarget;

public interface SVGGlobal extends Global {
   void startMouseCapture(EventTarget var1, boolean var2, boolean var3);

   void stopMouseCapture();
}

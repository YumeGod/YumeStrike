package org.apache.batik.bridge.svg12;

import org.apache.batik.bridge.BridgeUpdateHandler;
import org.w3c.dom.Element;

public interface SVG12BridgeUpdateHandler extends BridgeUpdateHandler {
   void handleBindingEvent(Element var1, Element var2);

   void handleContentSelectionChangedEvent(ContentSelectionChangedEvent var1);
}

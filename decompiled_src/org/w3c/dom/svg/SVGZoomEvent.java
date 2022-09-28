package org.w3c.dom.svg;

import org.w3c.dom.events.UIEvent;

public interface SVGZoomEvent extends UIEvent {
   SVGRect getZoomRectScreen();

   float getPreviousScale();

   SVGPoint getPreviousTranslate();

   float getNewScale();

   SVGPoint getNewTranslate();
}

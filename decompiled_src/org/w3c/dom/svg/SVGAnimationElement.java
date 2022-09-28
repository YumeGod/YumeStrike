package org.w3c.dom.svg;

import org.w3c.dom.DOMException;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.smil.ElementTimeControl;

public interface SVGAnimationElement extends SVGElement, SVGTests, SVGExternalResourcesRequired, ElementTimeControl, EventTarget {
   SVGElement getTargetElement();

   float getStartTime();

   float getCurrentTime();

   float getSimpleDuration() throws DOMException;
}

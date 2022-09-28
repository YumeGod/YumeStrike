package org.w3c.dom.svg;

import org.w3c.dom.events.EventTarget;

public interface SVGLineElement extends SVGElement, SVGTests, SVGLangSpace, SVGExternalResourcesRequired, SVGStylable, SVGTransformable, EventTarget {
   SVGAnimatedLength getX1();

   SVGAnimatedLength getY1();

   SVGAnimatedLength getX2();

   SVGAnimatedLength getY2();
}
